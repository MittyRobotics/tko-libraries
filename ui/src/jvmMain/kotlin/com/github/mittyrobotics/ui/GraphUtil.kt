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

import com.github.mittyrobotics.core.math.geometry.Rotation
import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector
import com.github.mittyrobotics.core.math.spline.Parametric
import kotlin.math.PI


public object GraphUtil {
    public fun populateSeries(series: XYSeriesWithRenderer, dataPoints: Array<Vector>): XYSeriesWithRenderer {
        for (i in dataPoints.indices) {
            series.add(dataPoints[i].x, dataPoints[i].y)
        }
        return series
    }

    public fun rectangle(centerTransform: Transform, width: Double, height: Double): Array<Vector> {
        val halfWidth = width / 2
        val halfHeight = height / 2

        // 0------3
        // |      |
        // 1------2
        val p0: Transform = (Transform(Vector(-halfHeight, halfWidth)) + centerTransform)
            .rotateAround(centerTransform.vector, centerTransform.rotation)
        val p1: Transform = (Transform(Vector(-halfHeight, -halfWidth)) + centerTransform)
            .rotateAround(centerTransform.vector, centerTransform.rotation)
        val p2: Transform = (Transform(Vector(halfHeight, -halfWidth)) + centerTransform)
            .rotateAround(centerTransform.vector, centerTransform.rotation)
        val p3: Transform = (Transform(Vector(halfHeight, halfWidth)) + centerTransform)
            .rotateAround(centerTransform.vector, centerTransform.rotation)
        return arrayOf<Vector>(
            p0.vector, p1.vector, p2.vector, p3.vector, p0.vector
        )
    }

    public fun arrow(transform: Transform, length: Double, arrowWidth: Double): Array<Vector> {
        val positions: ArrayList<Vector> = ArrayList<Vector>()

        //   ^
        //  /1\
        // 2 | 3
        //   |
        //   |
        //   |
        //   0

        val halfWidth = arrowWidth / 2.0
        val p0: Transform = transform
        val p1: Transform = transform + Transform(Vector(length, 0.0).rotateBy(transform.rotation))
        val p2: Transform = p1 + Transform(Vector(0.0, halfWidth).rotateBy(transform.rotation+ Rotation(PI/4)))
        val p3: Transform = p1 - Transform(Vector(0.0, halfWidth).rotateBy(transform.rotation- Rotation(PI/4)))
        positions.add(p0.vector)
        positions.add(p1.vector)
        positions.add(p2.vector)
        positions.add(p1.vector)
        positions.add(p3.vector)
        positions.add(p1.vector)
        return positions.toArray(arrayOfNulls<Vector>(0))
    }

    public fun parametric(
        parametric: Parametric, stepInterval: Double,
        arrowWidth: Double
    ): Array<Vector> {
        val positions: ArrayList<Vector> = ArrayList<Vector>()
        var t = 0.0
        while (t < 1) {
            val arrow: Array<Vector> = arrow(parametric.getTransform(t), 0.0, arrowWidth)
            for (p in arrow) {
                positions.add(p)
            }
            t += stepInterval
        }
        return positions.toArray(arrayOfNulls<Vector>(0))
    }

    public fun parametric(parametric: Parametric, parameterization: DoubleArray, arrowWidth: Double): Array<Vector> {
        val positions: ArrayList<Vector> = ArrayList<Vector>()
        for (i in parameterization.indices) {
            val arrow: Array<Vector> = arrow(parametric.getTransform(parameterization[i]), 0.0, arrowWidth)
            for (p in arrow) {
                positions.add(p)
            }
        }
        return positions.toArray(arrayOfNulls<Vector>(0))
    }
}