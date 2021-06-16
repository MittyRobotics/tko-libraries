package com.github.mittyrobotics.core.math.spline

import com.github.mittyrobotics.core.math.geometry.Rotation
import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector
import kotlin.math.pow
import kotlin.math.sqrt

public class CubicHermiteSpline(
        public val transform0: Transform,
        public val transform1: Transform,
        public val velocity0: Vector = Vector(transform0.distance(transform1), transform0.rotation),
        public val velocity1: Vector = Vector(transform0.distance(transform1), transform1.rotation)
) : Parametric() {
    /**
     * Returns the [Vector] along the [Parametric] at `t` where `0 <= t <= 1`.
     *
     * @param t the parameter
     * @return the [Vector] at the parameter `t`.
     */
    override fun getVector(t: Double): Vector {
        //Cubic hermite spline equations https://rose-hulman.edu/~finn/CCLI/Notes/day09.pdf#page=2
        val h0: Double = 1 - 3 * t * t + 2 * t * t * t
        val h1: Double = t - 2 * t * t + t * t * t
        val h2: Double = -t * t + t * t * t
        val h3: Double = 3 * t * t - 2 * t * t * t

        return computeFromCoefficients(h0, h1, h2, h3)
    }

    /**
     * Returns the [Rotation] along the [Parametric] at `t` where `0 <= t <= 1`.
     *
     * @param t the parameter
     * @return the [Rotation] at the parameter `t`.
     */
    override fun getRotation(t: Double): Rotation = Rotation(getDerivative(t, 1))

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
    override fun getTransform(t: Double): Transform = Transform(getVector(t), getRotation(t))

    /**
     * Returns the curvature at point `t` on the [Parametric].
     *
     * @param t the parameter
     * @return the curvature at the parameter `t`.
     */
    override fun getCurvature(t: Double): Double {
        val firstDerivative: Vector = getDerivative(t, 1)
        val secondDerivative: Vector = getDerivative(t, 2)

        return (firstDerivative.x * secondDerivative.y - secondDerivative.x * firstDerivative.y) / sqrt(
            (firstDerivative.x.pow(
                2.0
            ) + firstDerivative.y.pow(2.0)).pow(3.0)
        )
    }

    /**
     * Returns the 'n'-th derivative of the [Parametric] in the form of a [Vector] containing the x and
     * y value at the parameter `t`.
     *
     * @param t the parameter
     * @param n the derivative degree
     * @return the 'n'-th derivative [Vector] at the parameter `t`.
     */
    override fun getDerivative(t: Double, n: Int): Vector {
        when (n) {
            1 -> {
                //First derivative of cubic hermite spline functions
                val h0: Double = 6 * t * t - 6 * t
                val h1: Double = 3 * t * t - 4 * t + 1
                val h2: Double = 3 * t * t - 2 * t
                val h3: Double = -6 * t * t + 6 * t

                return computeFromCoefficients(h0, h1, h2, h3)
            }
            2 -> {
                //Second derivative of cubic hermite spline functions
                val h0: Double = 12 * t - 6
                val h1: Double = 6 * t - 4
                val h2: Double = 6 * t - 2
                val h3: Double = 6 - 12 * t

                return computeFromCoefficients(h0, h1, h2, h3)
            }
            else -> {
                return Vector()
            }
        }
    }

    /**
     * Computes the [Vector] from the 4 base coefficients.
     *
     * @param h0 base coefficient 1
     * @param h1 base coefficient 2
     * @param h2 base coefficient 3
     * @param h3 base coefficient 4
     * @return the [Vector] containing the x and y values computed from the coefficients.
     */
    private fun computeFromCoefficients(h0: Double, h1: Double, h2: Double, h3: Double): Vector {
        val x: Double = h0 * transform0.vector.x + h1 * velocity0.x + h2 * velocity1.x + h3 * transform1.vector.x
        val y: Double = h0 * transform0.vector.y + h1 * velocity0.y + h2 * velocity1.y + h3 * transform1.vector.y
        return Vector(x, y)
    }
}