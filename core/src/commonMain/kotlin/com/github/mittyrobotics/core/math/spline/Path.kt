package com.github.mittyrobotics.core.math.spline

import com.github.mittyrobotics.core.math.geometry.Rotation
import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector2D
import kotlin.math.floor
import kotlin.math.min

public class Path(public val parametrics: Array<Parametric>) : Parametric() {
    override fun getVector(t: Double): Vector2D = getParametric(t).let { it.first.getVector(it.second) }
    override fun getRotation(t: Double): Rotation = getParametric(t).let { it.first.getRotation(it.second) }
    override fun getTransform(t: Double): Transform = getParametric(t).let { it.first.getTransform(it.second) }
    override fun getCurvature(t: Double): Double = getParametric(t).let { it.first.getCurvature(it.second) }
    override fun getDerivative(t: Double, n: Int): Vector2D =
        getParametric(t).let { it.first.getDerivative(it.second, n) }

    public fun getParametric(t: Double): Triple<Parametric, Double, Int> = when {
        t < 0 -> {
            Triple(parametrics[0], t, 0)
        }
        t > 1 -> {
            Triple(parametrics[parametrics.size - 1], t, parametrics.size - 1)
        }
        else -> {
            val t1 = t * parametrics.size
            val i = min(floor(t1).toInt(), parametrics.size - 1)
            Triple(parametrics[i], t1 - i, i)
        }
    }

    override fun getGaussianQuadratureLength(endParam: Double): Double = getParametric(endParam).let {
        var length = it.first.getGaussianQuadratureLength(it.second); parametrics.forEachIndexed { i, parametric ->
        if (i < it.third) {
            length += parametric.getGaussianQuadratureLength()
        }
    }; length
    }

    override fun getGaussianQuadratureLength(startParam: Double, endParam: Double): Double {
        val startParametric = getParametric(startParam)
        val endParametric = getParametric(endParam)
        var previousLength = 0.0
        for (i in startParametric.third + 1 until endParametric.third) {
            previousLength += parametrics[i].getGaussianQuadratureLength()
        }
        return if (startParametric.third == endParametric.third) {
            previousLength + endParametric.first.getGaussianQuadratureLength(
                startParametric.second,
                endParametric.second
            )
        } else {
            startParametric.first.getGaussianQuadratureLength(
                startParametric.second,
                1.0
            ) + endParametric.first.getGaussianQuadratureLength(0.0, endParametric.second)
        }
    }

    override fun getParameterFromLength(length: Double, splineLength: Double): Double {
        if (length < 0) {
            return 0.0
        }
        var totalLength = 0.0
        for (i in parametrics.indices) {
            val thisLength = parametrics[i].getGaussianQuadratureLength()
            totalLength += thisLength
            if (totalLength > length || i == parametrics.size - 1) {
                return toAbsoluteParameter(
                    parametrics[i].getParameterFromLength(length - (totalLength - thisLength)),
                    i
                )
            }
        }
        return 0.0
    }

    private fun toAbsoluteParameter(t: Double, i: Int): Double {
        return (t + i) / parametrics.size
    }

    private fun toRelativeParameter(t: Double, i: Int): Double {
        return t * parametrics.size - i
    }

    public companion object {
        public fun quinticHermitePath(waypoints: Array<Transform>): Path =
            Path(Array(waypoints.size - 1) { QuinticHermiteSpline(waypoints[it], waypoints[it + 1]) })

        public fun cubicHermitePath(waypoints: Array<Transform>): Path =
            Path(Array(waypoints.size - 1) { CubicHermiteSpline(waypoints[it], waypoints[it + 1]) })
    }
}