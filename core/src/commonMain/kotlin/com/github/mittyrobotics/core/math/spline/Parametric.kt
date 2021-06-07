package com.github.mittyrobotics.core.math.spline

import com.github.mittyrobotics.core.math.geometry.Rotation
import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector
import kotlin.math.max
import kotlin.math.min


public abstract class Parametric {
    /**
     * Returns the [Vector] along the [Parametric] at `t` where `0 <= t <= 1`.
     *
     * @param t the parameter
     * @return the [Vector] at the parameter `t`.
     */
    public abstract fun getVector(t: Double): Vector

    /**
     * Returns the [Rotation] along the [Parametric] at `t` where `0 <= t <= 1`.
     *
     * @param t the parameter
     * @return the [Rotation] at the parameter `t`.
     */
    public abstract fun getRotation(t: Double): Rotation

    /**
     * Returns the [Transform] along the [Parametric] at `t` where `0 <= t <= 1`.
     *
     *
     * The [Transform] contains the [Vector] and [Rotation], with the [Rotation] being the
     * tangent angle at the [Vector].
     *
     * @param t the parameter
     * @return the [Transform] at the parameter `t`.
     */
    public abstract fun getTransform(t: Double): Transform

    /**
     * Returns the curvature at point `t` on the [Parametric].
     *
     * @param t the parameter
     * @return the curvature at the parameter `t`.
     */
    public abstract fun getCurvature(t: Double): Double

    /**
     * Returns the 'n'-th derivative of the [Parametric] in the form of a [Vector] containing the x and
     * y value at the parameter `t`.
     *
     * @param t the parameter
     * @param n the derivative degree
     * @return the 'n'-th derivative [Vector] at the parameter `t`.
     */
    public abstract fun getDerivative(t: Double, n: Int = 1): Vector

    /**
     * Computes the estimated length of the parametric by counting the length of each segment for every step. This is
     * slower but more accurate than the Gaussian quadrature method.
     *
     * @param steps the amount of segments it counts. Higher values are more accurate.
     * @return the estimated length of the parametric.
     */
    public fun getRawLength(steps: Double, startT: Double, endT: Double): Double {
        var length = 0.0
        var t = startT
        while (t < endT) {
            length += getVector(t).distance(getVector(t - (endT - startT) / steps))
            t += (endT - startT) / steps
        }
        return length
    }

    /**
     * Computes the estimated length of the parametric using 11-point Gaussian quadrature.
     *
     * https://en.wikipedia.org/wiki/Gaussian_quadrature
     *
     * @return the estimated length of the parametric.
     */
    public fun getGaussianQuadratureLength(): Double = getGaussianQuadratureLength(1.0)

    /**
     * Computes the estimated length of the parametric using 11-point Gaussian quadrature.
     *
     * https://en.wikipedia.org/wiki/Gaussian_quadrature
     *
     * @param endParam the ending parameter of the parametric.
     * @return the estimated length of the parametric.
     */
    public fun getGaussianQuadratureLength(endParam: Double): Double = getGaussianQuadratureLength(0.0, endParam)


    public fun getGaussianQuadratureLength(startParam: Double, endParam: Double): Double {
        //11-point Gaussian quadrature coefficients
        val coefficients = arrayOf(
            doubleArrayOf(0.0000000000000000, 0.2729250867779006),
            doubleArrayOf(-0.2695431559523450, 0.2628045445102467),
            doubleArrayOf(0.2695431559523450, 0.2628045445102467),
            doubleArrayOf(-0.5190961292068118, 0.2331937645919905),
            doubleArrayOf(0.5190961292068118, 0.2331937645919905),
            doubleArrayOf(-0.7301520055740494, 0.1862902109277343),
            doubleArrayOf(0.7301520055740494, 0.1862902109277343),
            doubleArrayOf(-0.8870625997680953, 0.1255803694649046),
            doubleArrayOf(0.8870625997680953, 0.1255803694649046),
            doubleArrayOf(-0.9782286581460570, 0.0556685671161737),
            doubleArrayOf(0.9782286581460570, 0.0556685671161737)
        )
        val halfParam = (endParam - startParam) / 2.0
        var length = 0.0
        for (i in coefficients.indices) {
            val alpha = startParam + halfParam * (1 + coefficients[i][0])
            length += getDerivative(alpha, 1).magnitude() * coefficients[i][1]
        }
        return length * halfParam
    }

    /**
     * Returns the parameter of the parametric at the length along the spline.
     *
     *
     * https://en.wikipedia.org/wiki/Newton%27s_method
     *
     * @param length length along the spline to get the parameter.
     * @return the parameter of the parametric at the length along the spline.
     */
    public fun getParameterFromLength(length: Double): Double {
        return getParameterFromLength(length, getGaussianQuadratureLength())
    }

    /**
     * Returns the parameter of the parametric at the length along the spline.
     *
     *
     * https://en.wikipedia.org/wiki/Newton%27s_method
     *
     * @param length length along the spline to get the parameter.
     * @return the parameter of the parametric at the length along the spline.
     */
    public fun getParameterFromLength(length: Double, splineLength: Double): Double {
        //Initial guess for the t value
        var t = length / splineLength

        //Newton-Raphson iterations to make more accurate estimation
        for (i in 0..4) {
            val tangentMagnitude: Double = getDerivative(t, 1).magnitude()
            if (tangentMagnitude > 0.0) {
                t -= (getGaussianQuadratureLength(t) - length) / tangentMagnitude
                t = min(1.0, max(t, -1.0))
            }
        }
        return t
    }
}