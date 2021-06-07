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

import java.awt.Color
import kotlin.jvm.JvmOverloads
import org.jfree.data.xy.XYSeries
import java.awt.Shape

public class XYSeriesWithRenderer @JvmOverloads constructor(
    key: Comparable<*>,
    public var color: Color? = null,
    public var isShowLines: Boolean = true,
    public var isShowShapes: Boolean = true,
    public var shape: Shape? = null
) : XYSeries(key, false, true) {

    public constructor(series: XYSeries) : this(series.key, null, true, true, null) {
        for (i in 0 until series.itemCount) {
            this.add(series.getDataItem(i))
        }
    }

    public fun setRenderer(color: Color?, showLines: Boolean, showShapes: Boolean, shape: Shape?) {
        this.color = color
        isShowLines = showLines
        isShowShapes = showShapes
        this.shape = shape
    }

    public companion object {
        public fun withLines(key: Comparable<*>): XYSeriesWithRenderer {
            val series = XYSeriesWithRenderer(key)
            series.isShowLines = true
            series.isShowShapes = false
            return series
        }

        public fun withShapes(key: Comparable<*>): XYSeriesWithRenderer {
            val series = XYSeriesWithRenderer(key)
            series.isShowLines = false
            series.isShowShapes = true
            return series
        }
    }
}