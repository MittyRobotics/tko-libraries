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
package com.github.mittyrobotics.ui

import com.github.mittyrobotics.ui.themes.DefaultDarkTheme
import com.github.mittyrobotics.ui.themes.GraphTheme
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
import java.awt.Shape
import java.text.DecimalFormat
import java.util.ArrayList
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.SwingUtilities

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
    private val seriesWithRenderers = ArrayList<XYSeriesWithRenderer>()
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

    public fun addSeries(series: XYSeriesWithRenderer): Int {
        seriesWithRenderers.add(series)
        update()
        return getSeriesIndexFromKey(series.key.toString())
    }

    public fun addSeries(series: XYSeries): Int {
        return addSeries(XYSeriesWithRenderer(series))
    }

    public fun setSeriesRenderer(index: Int, color: Color, showLines: Boolean, showPoints: Boolean, shape: Shape?) {
        seriesWithRenderers[index].setRenderer(color, showLines, showPoints, shape)
    }

    public fun setSeriesRenderer(key: String, color: Color, showLines: Boolean, showPoints: Boolean, shape: Shape?) {
        setSeriesRenderer(getSeriesIndexFromKey(key), color, showLines, showPoints, shape)
    }

    public fun addToSeries(index: Int, additionSeries: XYSeries) {
        for (i in 0 until additionSeries.itemCount) {
            val data: XYDataItem = additionSeries.getDataItem(i)
            defaultDataset.getSeries(index).add(data)
        }
    }

    public fun addToSeries(key: String, additionSeries: XYSeries) {
        addToSeries(getSeriesIndexFromKey(key), additionSeries)
    }

    public fun addToSeries(index: Int, dataItem: XYDataItem) {
        val series = XYSeries("___temporary___", false)
        series.add(dataItem)
        addToSeries(index, series)
    }

    public fun addToSeries(key: String, dataItem: XYDataItem) {
        addToSeries(getSeriesIndexFromKey(key), dataItem)
    }

    public fun changeSeries(seriesIndex: Int, newSeries: XYSeries) {
        SwingUtilities.invokeLater(Runnable {
            val itemCount: Int = defaultDataset.getSeries(seriesIndex).getItemCount()
            for (i in 0 until itemCount) {
                defaultDataset.getSeries(seriesIndex).remove(0)
            }
            for (i in 0 until newSeries.getItemCount()) {
                defaultDataset.getSeries(seriesIndex).add(newSeries.getDataItem(i))
            }
        })
    }

    public fun changeSeries(key: String, newSeries: XYSeries) {
        changeSeries(getSeriesIndexFromKey(key), newSeries)
    }

    public fun removeSeries(seriesIndex: Int) {
        seriesWithRenderers.removeAt(seriesIndex)
        update()
    }

    public fun removeSeries(key: String) {
        removeSeries(getSeriesIndexFromKey(key))
    }

    public fun getSeriesIndexFromKey(key: String): Int {
        val index: Int = defaultDataset.getSeriesIndex(key)
        if (index == -1) {
            addSeries(XYSeriesWithRenderer.Companion.withLines(key))
            return defaultDataset.getSeriesIndex(key)
        }
        return index
    }

    public fun update() {
        defaultDataset.removeAllSeries()
        for (i in seriesWithRenderers.indices) {
            defaultDataset.addSeries(seriesWithRenderers[i])
            defaultRenderer.setSeriesPaint(i, seriesWithRenderers[i].color)
            defaultRenderer.setSeriesLinesVisible(i, seriesWithRenderers[i].isShowLines)
            defaultRenderer.setSeriesShapesVisible(i, seriesWithRenderers[i].isShowShapes)
            defaultRenderer.setSeriesShape(i, seriesWithRenderers[i].shape)
        }
    }

    /**
     * Scales the graph uniformly to fit all points on it
     */
    public fun scaleGraphToFit() {
        var lowerBound = 9999.0
        var upperBound = -9999.0
        var leftBound = 9999.0
        var rightBound = -9999.0
        for (i in 0 until plot.getDatasetCount()) {
            for (a in 0 until plot.getDataset(i).getSeriesCount()) {
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
        val domain: NumberAxis = plot.getDomainAxis() as NumberAxis
        domain.setRange(lowerRange - 20, upperRange + 20)
        domain.isVerticalTickLabels = true
        val range: NumberAxis = plot.getRangeAxis() as NumberAxis
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
        val domain: NumberAxis = plot.getDomainAxis() as NumberAxis
        domain.setRange(leftBound, rightBound)
        domain.setVerticalTickLabels(true)
        val range: NumberAxis = plot.getRangeAxis() as NumberAxis
        range.setRange(lowerBound, upperBound)
    }

    /**
     * Scales the graph to a scale based on pixel size, each pixel representing `scale` units
     *
     * @param scale
     */
    public fun scaleGraphToScale(scale: Double, centerX: Double, centerY: Double) {
        val width: Double = getWidth().toDouble()
        val height: Double = getHeight().toDouble()
        val upperBound = centerY + height * scale / 2
        val lowerBound = centerY - height * scale / 2
        val leftBound = centerX - width * scale / 2
        val rightBound = centerX + width * scale / 2
        val domain: NumberAxis = plot.getDomainAxis() as NumberAxis
        domain.setRange(leftBound, rightBound)
        domain.setVerticalTickLabels(true)
        val range: NumberAxis = plot.getRangeAxis() as NumberAxis
        range.setRange(lowerBound, upperBound)
    }

    public fun setGraphTheme(theme: GraphTheme) {
        chartPanel.setBackground(theme.frameBackgroundColor)
        plot.setBackgroundPaint(theme.graphBackgroundColor)
        plot.setDomainGridlinePaint(theme.gridColor)
        plot.setRangeGridlinePaint(theme.gridColor)
        chart.setBackgroundPaint(theme.frameBackgroundColor)
        chart.getTitle().setPaint(theme.titleColor)
        chart.getLegend().setItemPaint(theme.labelColor)
        chart.getLegend().setBackgroundPaint(theme.frameBackgroundColor)
        plot.getDomainAxis().setLabelPaint(theme.labelColor)
        plot.getRangeAxis().setLabelPaint(theme.labelColor)
        plot.getDomainAxis().setTickLabelPaint(theme.tickLabelColor)
        plot.getRangeAxis().setTickLabelPaint(theme.tickLabelColor)
    }

    init {
        initUI()
        setGraphTheme(DefaultDarkTheme())
        isVisible = true
    }
}