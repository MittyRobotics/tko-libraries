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
package com.github.mittyrobotics.ui.themes

import com.github.mittyrobotics.ui.themes.GraphTheme
import java.awt.Color
import kotlin.jvm.JvmStatic
import com.github.mittyrobotics.ui.XYSeriesWithRenderer
import com.github.mittyrobotics.ui.GraphUtil
import kotlin.jvm.JvmOverloads
import javax.swing.JFrame
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.ChartPanel
import org.jfree.data.xy.XYSeriesCollection
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import javax.swing.BorderFactory
import org.jfree.chart.ChartFactory
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.axis.NumberAxis
import java.text.DecimalFormat
import org.jfree.data.xy.XYSeries
import java.awt.Shape
import org.jfree.data.xy.XYDataItem
import javax.swing.SwingUtilities
import java.lang.Runnable

public class DarkSeaGraphTheme : GraphTheme(
    Color(18, 23, 25),
    Color(0, 0, 0),
    Color(25, 27, 29),
    Color(194, 194, 194),
    Color(161, 161, 161),
    Color(113, 113, 113)
)