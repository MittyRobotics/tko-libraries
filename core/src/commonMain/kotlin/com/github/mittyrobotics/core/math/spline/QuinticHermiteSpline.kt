package com.github.mittyrobotics.core.math.spline

import com.github.mittyrobotics.core.math.geometry.Rotation
import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector
import kotlin.math.pow
import kotlin.math.sqrt

public class QuinticHermiteSpline(
    public val transform0: Transform,
    public val transform1: Transform,
    public val velocity0: Vector = Vector(transform0.distance(transform1), transform0.rotation),
    public val velocity1: Vector = Vector(transform0.distance(transform1), transform1.rotation),
    public val acceleration0: Vector = Vector(),
    public val acceleration1: Vector = Vector()
) : Parametric() {
    public constructor(
        transform0: Transform,
        transform1: Transform,
        curvature0: Double,
        curvature1: Double
    ) : this(
        transform0,
        transform1,
        acceleration0 = Vector(
            getAccelerationMagnitudeFromCurvature(curvature0, transform0.distance(transform1)),
            transform0.rotation
        ),
        acceleration1 = Vector(
            getAccelerationMagnitudeFromCurvature(curvature1, transform0.distance(transform1)),
            transform1.rotation
        )
    )

    /**
     * Returns the [Vector] along the [Parametric] at `t` where `0 <= t <= 1`.
     *
     * @param t the parameter
     * @return the [Vector] at the parameter `t`.
     */
    override fun getVector(t: Double): Vector {
        //Quintic hermite spline equations https://rose-hulman.edu/~finn/CCLI/Notes/day09.pdf#page=4
        val h0 = -6 * t * t * t * t * t + 15 * t * t * t * t - 10 * t * t * t + 1
        val h1 = -3 * t * t * t * t * t + 8 * t * t * t * t - 6 * t * t * t + t
        val h2 = -(t * t * t * t * t) / 2 + 3 * t * t * t * t / 2 - 3 * t * t * t / 2 + t * t / 2
        val h3 = t * t * t * t * t / 2 - t * t * t * t + t * t * t / 2
        val h4 = -3 * t * t * t * t * t + 7 * t * t * t * t - 4 * t * t * t
        val h5 = 6 * t * t * t * t * t - 15 * t * t * t * t + 10 * t * t * t

        return computeFromCoefficients(h0, h1, h2, h3, h4, h5)
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
                //First derivative of quintic hermite spline functions
                val h0 = -30 * t * t * t * t + 60 * t * t * t - 30 * t * t
                val h1 = -15 * t * t * t * t + 32 * t * t * t - 18 * t * t + 1
                val h2 = -(5 * t * t * t * t) / 2 + 6 * t * t * t - 9 * t * t / 2 + t
                val h3 = 5 * t * t * t * t / 2 - 4 * t * t * t + 3 * t * t / 2
                val h4 = -15 * t * t * t * t + 28 * t * t * t - 12 * t * t
                val h5 = 30 * t * t * t * t - 60 * t * t * t + 30 * t * t

                return computeFromCoefficients(h0, h1, h2, h3, h4, h5)
            }
            2 -> {
                //Second derivative of quintic hermite spline functions
                val h0 = -120 * t * t * t + 180 * t * t - 60 * t
                val h1 = -60 * t * t * t + 96 * t * t - 36 * t
                val h2 = -10 * t * t * t + 18 * t * t - 9 * t + 1
                val h3 = t * (10 * t * t - 12 * t + 3)
                val h4 = -60 * t * t * t + 84 * t * t - 24 * t
                val h5 = 120 * t * t * t - 180 * t * t + 60 * t

                return computeFromCoefficients(h0, h1, h2, h3, h4, h5)
            }
            3 -> {
                //Third derivative of quintic hermite spline functions
                val h0 = -360 * t * t * t + 360 * t - 60
                val h1 = -180 * t * t + 192 * t - 36
                val h2 = -30 * t * t + 36 * t - 9
                val h3 = 30 * t * t - 24 * t + 3
                val h4 = -180 * t * t + 168 * t - 24
                val h5 = 360 * t * t - 360 * t + 60

                return computeFromCoefficients(h0, h1, h2, h3, h4, h5)
            }
            else -> {
                return Vector()
            }
        }
    }

    /**
     * Computes the [Position] from the 6 base coefficients.
     *
     * @param h0 base coefficient 1
     * @param h1 base coefficient 2
     * @param h2 base coefficient 3
     * @param h3 base coefficient 4
     * @param h4 base coefficient 5
     * @param h5 base coefficient 6
     * @return the [Position] containing the x and y values computed from the coefficients.
     */
    private fun computeFromCoefficients(
        h0: Double,
        h1: Double,
        h2: Double,
        h3: Double,
        h4: Double,
        h5: Double
    ): Vector {
        val x: Double =
            h0 * transform0.vector.x + h1 * velocity0.x + h2 * acceleration0.x + h3 * acceleration1.x + h4 * velocity1.x + h5 * transform1.vector.x
        val y: Double =
            h0 * transform0.vector.y + h1 * velocity0.y + h2 * acceleration0.y + h3 * acceleration1.y + h4 * velocity1.y + h5 * transform1.vector.y
        return Vector(x, y)
    }

    public companion object {
        /**
         * Returns the magnitude of the acceleration vector for a waypoint passed into the [QuinticHermiteSpline]
         * to achieve a certain curvature at that waypoint.
         *
         * @param curvature      the desired curvature of the waypoint
         * @param distanceBetweenPoints the distance between waypoints passed into the [QuinticHermiteSpline]
         * @return the magnitude of the acceleration vector for the waypoint to achieve the desired curvature value
         */
        public fun getAccelerationMagnitudeFromCurvature(curvature: Double, distanceBetweenPoints: Double): Double {
            return curvature * distanceBetweenPoints.pow(2.0)
        }
    }
}