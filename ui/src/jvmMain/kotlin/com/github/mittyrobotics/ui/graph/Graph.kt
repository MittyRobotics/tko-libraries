/*
 * MIT License
 *
 * Copyright (c) 2020 Mitty Robotics (Team 1351)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.mittyrobotics.ui.graph

import com.github.mittyrobotics.core.math.geometry.Rotation
import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector2D
import com.github.mittyrobotics.core.math.linalg.Matrix
import com.github.mittyrobotics.core.math.spline.Parametric
import com.github.mittyrobotics.motion.models.SystemResponse
import com.github.mittyrobotics.ui.graph.themes.DefaultDarkTheme
import com.github.mittyrobotics.ui.graph.themes.GraphTheme
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.xy.XYDataItem
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.Color
import java.text.DecimalFormat
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.SwingUtilities
import kotlin.math.PI

public open class Graph @JvmOverloads constructor(
    private val titleName: String = "Graph",
    private val yAxisName: String = "y",
    private val xAxisName: String = "x"
) : JFrame() {
    private lateinit var chart: JFreeChart
    private lateinit var plot: XYPlot
    private lateinit var chartPanel: ChartPanel
    private val defaultDataset: XYSeriesCollection = XYSeriesCollection()
    private val defaultRenderer: XYLineAndShapeRenderer = XYLineAndShapeRenderer()
    private val dataList = ArrayList<GraphData>()
    private fun initUI() {
        val chart: JFreeChart = createChart()
        val chartPanel = ChartPanel(chart)
        chartPanel.border = BorderFactory.createEmptyBorder(0, 0, 0, 30)
        add(chartPanel)
        pack()
        title = titleName
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        this.chartPanel = chartPanel
    }

    private fun createChart(): JFreeChart {
        val chart: JFreeChart = ChartFactory.createXYLineChart(
            titleName,
            xAxisName,
            yAxisName,
            defaultDataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        )
        val plot: XYPlot = chart.xyPlot
        plot.backgroundPaint = Color.white
        plot.isRangeGridlinesVisible = true
        plot.rangeGridlinePaint = Color.BLACK
        plot.isDomainGridlinesVisible = true
        plot.domainGridlinePaint = Color.BLACK
        (plot.domainAxis as NumberAxis).numberFormatOverride = DecimalFormat()
        (plot.rangeAxis as NumberAxis).numberFormatOverride = DecimalFormat()
        this.chart = chart
        this.plot = plot
        plot.renderer = defaultRenderer
        return chart
    }

    public fun plot(
        data: Array<Vector2D>,
        name: String,
        lines: Boolean = true,
        points: Boolean = false,
        color: Color? = null
    ) {
        dataList.add(GraphData(data, name, lines, points, color))
        update()
    }

    public fun plot(
        data: SystemResponse,
        name: String,
        lines: Boolean = true,
        points: Boolean = false,
        color: Color? = null
    ) {
        for (i in data.inputs.first().get2DData().indices) {
            dataList.add(
                GraphData(
                    Array(data.inputs.size) { Vector2D(data.times[it], data.inputs[it].get2DData()[i]) },
                    "$name Input $i",
                    lines,
                    points,
                    color
                )
            )
        }
        for (i in data.outputs.first().get2DData().indices) {
            dataList.add(
                GraphData(
                    Array(data.outputs.size) { Vector2D(data.times[it], data.outputs[it].get2DData()[i]) },
                    "$name State $i",
                    lines,
                    points,
                    color
                )
            )
        }
        update()
    }

    public fun plotParametric(
        parametric: Parametric,
        name: String,
        stepInterval: Double = 0.01,
        arrowWidth: Double = 0.0,
        lines: Boolean = true,
        points: Boolean = false,
        color: Color? = null
    ) {
        dataList.add(GraphData(parametric(parametric, stepInterval, arrowWidth), name, lines, points, color))
        update()
    }

    public fun update() {
        SwingUtilities.invokeLater {
            defaultDataset.removeAllSeries()
            for (i in dataList.indices) {
                defaultDataset.addSeries(
                    XYSeries(
                        dataList[i].name,
                        false
                    ).also { series -> dataList[i].data.forEach { series.add(XYDataItem(it.x, it.y)) } })
                defaultRenderer.setSeriesPaint(i, dataList[i].color)
                defaultRenderer.setSeriesLinesVisible(i, dataList[i].lines)
                defaultRenderer.setSeriesShapesVisible(i, dataList[i].points)
            }
        }
    }

    /**
     * Scales the graph uniformly to fit all points on it
     */
    public fun scaleGraphToFit() {
        var lowerBound = Double.POSITIVE_INFINITY
        var upperBound = Double.NEGATIVE_INFINITY
        var leftBound = Double.POSITIVE_INFINITY
        var rightBound = Double.NEGATIVE_INFINITY
        for (i in 0 until plot.datasetCount) {
            for (a in 0 until plot.getDataset(i).seriesCount) {
                for (j in 0 until plot.getDataset(i).getItemCount(a)) {
                    if (plot.getDataset(i).getYValue(a, j) < lowerBound) {
                        lowerBound = plot.getDataset(i).getYValue(a, j)
                    }
                    if (plot.getDataset(i).getYValue(a, j) > upperBound) {
                        upperBound = plot.getDataset(i).getYValue(a, j)
                    }
                    if (plot.getDataset(i).getXValue(a, j) < leftBound) {
                        leftBound = plot.getDataset(i).getXValue(a, j)
                    }
                    if (plot.getDataset(i).getXValue(a, j) > rightBound) {
                        rightBound = plot.getDataset(i).getXValue(a, j)
                    }
                }
            }
        }
        var lowerRange: Double = if (lowerBound < leftBound) {
            lowerBound
        } else {
            leftBound
        }
        var upperRange: Double = if (upperBound > rightBound) {
            upperBound
        } else {
            rightBound
        }
        val domain: NumberAxis = plot.domainAxis as NumberAxis
        domain.setRange(lowerRange - 20, upperRange + 20)
        domain.isVerticalTickLabels = true
        val range: NumberAxis = plot.rangeAxis as NumberAxis
        range.setRange(lowerRange - 20, upperRange + 20)
    }

    /**
     * Sizes the graph to an upper, lower, left, and right bound coordinate
     *
     * @param lowerBound
     * @param upperBound
     * @param leftBound
     * @param rightBound
     */
    public fun resizeGraph(lowerBound: Double, upperBound: Double, leftBound: Double, rightBound: Double) {
        val domain: NumberAxis = plot.domainAxis as NumberAxis
        domain.setRange(leftBound, rightBound)
        domain.isVerticalTickLabels = true
        val range: NumberAxis = plot.rangeAxis as NumberAxis
        range.setRange(lowerBound, upperBound)
    }

    /**
     * Scales the graph to a scale based on pixel size, each pixel representing `scale` units
     *
     * @param scale
     */
    public fun scaleGraphToScale(scale: Double, centerX: Double, centerY: Double) {
        val width: Double = width.toDouble()
        val height: Double = height.toDouble()
        val upperBound = centerY + height * scale / 2
        val lowerBound = centerY - height * scale / 2
        val leftBound = centerX - width * scale / 2
        val rightBound = centerX + width * scale / 2
        val domain: NumberAxis = plot.domainAxis as NumberAxis
        domain.setRange(leftBound, rightBound)
        domain.isVerticalTickLabels = true
        val range: NumberAxis = plot.rangeAxis as NumberAxis
        range.setRange(lowerBound, upperBound)
    }

    public fun setGraphTheme(theme: GraphTheme) {
        chartPanel.background = theme.frameBackgroundColor
        plot.backgroundPaint = theme.graphBackgroundColor
        plot.domainGridlinePaint = theme.gridColor
        plot.rangeGridlinePaint = theme.gridColor
        chart.backgroundPaint = theme.frameBackgroundColor
        chart.title.paint = theme.titleColor
        chart.legend.itemPaint = theme.labelColor
        chart.legend.backgroundPaint = theme.frameBackgroundColor
        plot.domainAxis.labelPaint = theme.labelColor
        plot.rangeAxis.labelPaint = theme.labelColor
        plot.domainAxis.tickLabelPaint = theme.tickLabelColor
        plot.rangeAxis.tickLabelPaint = theme.tickLabelColor
    }

    init {
        initUI()
        setGraphTheme(DefaultDarkTheme())
        isVisible = true
    }

    public companion object {
        public fun rectangle(centerTransform: Transform, width: Double, height: Double): Array<Vector2D> {
            val halfWidth = width / 2
            val halfHeight = height / 2

            // 0------3
            // |      |
            // 1------2
            val p0: Transform = (Transform(Vector2D(-halfHeight, halfWidth)) + centerTransform)
                .rotateAround(centerTransform.vector, centerTransform.rotation)
            val p1: Transform = (Transform(Vector2D(-halfHeight, -halfWidth)) + centerTransform)
                .rotateAround(centerTransform.vector, centerTransform.rotation)
            val p2: Transform = (Transform(Vector2D(halfHeight, -halfWidth)) + centerTransform)
                .rotateAround(centerTransform.vector, centerTransform.rotation)
            val p3: Transform = (Transform(Vector2D(halfHeight, halfWidth)) + centerTransform)
                .rotateAround(centerTransform.vector, centerTransform.rotation)
            return arrayOf<Vector2D>(
                p0.vector, p1.vector, p2.vector, p3.vector, p0.vector
            )
        }

        public fun arrow(transform: Transform, length: Double, arrowWidth: Double): Array<Vector2D> {
            val positions: ArrayList<Vector2D> = ArrayList<Vector2D>()

            //   ^
            //  /1\
            // 2 | 3
            //   |
            //   |
            //   |
            //   0

            val halfWidth = arrowWidth / 2.0
            val p0: Transform = transform
            val p1: Transform = transform + Transform(Vector2D(length, 0.0).rotateBy(transform.rotation))
            val p2: Transform = p1 + Transform(Vector2D(0.0, halfWidth).rotateBy(transform.rotation + Rotation(PI / 4)))
            val p3: Transform = p1 - Transform(Vector2D(0.0, halfWidth).rotateBy(transform.rotation - Rotation(PI / 4)))
            positions.add(p0.vector)
            positions.add(p1.vector)
            positions.add(p2.vector)
            positions.add(p1.vector)
            positions.add(p3.vector)
            positions.add(p1.vector)
            return positions.toArray(arrayOfNulls<Vector2D>(0))
        }

        public fun parametric(
            parametric: Parametric, stepInterval: Double = 0.01,
            arrowWidth: Double = 0.0
        ): Array<Vector2D> {
            val positions: ArrayList<Vector2D> = ArrayList<Vector2D>()
            var t = 0.0
            while (t < 1) {
                if (arrowWidth == 0.0) {
                    positions.add(parametric.getVector(t))
                } else {
                    val arrow: Array<Vector2D> = arrow(parametric.getTransform(t), 0.0, arrowWidth)
                    for (p in arrow) {
                        positions.add(p)
                    }
                }
                t += stepInterval
            }
            return positions.toArray(arrayOfNulls<Vector2D>(0))
        }

        public fun parametric(
            parametric: Parametric,
            parameterization: DoubleArray,
            arrowWidth: Double
        ): Array<Vector2D> {
            val positions: ArrayList<Vector2D> = ArrayList<Vector2D>()
            for (i in parameterization.indices) {
                val arrow: Array<Vector2D> = arrow(parametric.getTransform(parameterization[i]), 0.0, arrowWidth)
                for (p in arrow) {
                    positions.add(p)
                }
            }
            return positions.toArray(arrayOfNulls<Vector2D>(0))
        }
    }
}