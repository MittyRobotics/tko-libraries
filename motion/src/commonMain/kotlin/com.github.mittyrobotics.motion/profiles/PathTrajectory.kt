package com.github.mittyrobotics.motion.profiles

import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector2D
import com.github.mittyrobotics.core.math.kinematics.DifferentialDriveState
import com.github.mittyrobotics.core.math.spline.Parametric
import com.github.mittyrobotics.core.math.units.inches
import com.github.mittyrobotics.motion.State
import kotlin.math.*


public class PathTrajectory(
    public val path: Parametric,
    public val maxAcceleration: Double,
    public val maxVelocity: Double,
    public val maxAngularVelocity: Double = Double.POSITIVE_INFINITY,
    public val startVelocity: Double = 0.0,
    public val endVelocity: Double = 0.0,
    public val minVelocity: Double = 0.0,
    public val maxDeceleration: Double = maxAcceleration
) : GenerativeMotionProfile() {
    private var autoUpdateDistance = true

    /**
     * Returns the current traveled distance along the trajectory.
     */
    public var traveledDistance: Double = 0.0

    /**
     * Returns the total length of the trajectory (the path's gaussian quadrature length)
     */
    public val totalLength: Double = path.getGaussianQuadratureLength()

    /**
     * Returns the remaining distance of the trajectory
     */
    public val remainingDistance: Double
        get() = totalLength - traveledDistance
    private var previousVelocity: Double = startVelocity
    private val previewedVelocities: MutableList<Pair<Double, Double>> = mutableListOf()

    /**
     * Calculates the next [State] given a change in time since the last call to [next]
     *
     * @param dt delta time since last call to [next]
     */
    public override fun next(dt: Double): State {
        val distanceToEnd: Double = path.getGaussianQuadratureLength() - traveledDistance

        //Calculate trapezoidal velocity
        val maxVelocityToEnd = calculateMaxVelocityFromDistance(endVelocity, distanceToEnd, maxDeceleration)
        var velocity: Double = min(previousVelocity + maxAcceleration * dt, maxVelocityToEnd, maxVelocity)

        //Calculate preview distance based on distance it will take to slow down robot from current velocity to 0
        val previewDistance = calculateDistanceToSlowdown(previousVelocity, 0.0, maxDeceleration)

        //Get slowdown velocity at current point
        val curvature: Double = path.getCurvature(path.getParameterFromLength(traveledDistance))
        val slowdownVelocity = calculateSlowdownVelocity(curvature)

        //Preview slowdown velocity at future point
        val slowdownVelocityAtPreview = previewVelocity(traveledDistance + previewDistance)

        //Remove old array values that we have traveled past
        previewedVelocities.removeAll { it.second <= traveledDistance }
        //Add new preview velocity to array
        previewedVelocities.add(Pair(slowdownVelocityAtPreview, traveledDistance + previewDistance))

        //Get minimum velocity from the array required to slowdown to a future velocity
        val minVelocityToSlowdown = getMinVelFromArray(traveledDistance)

        //If min velocity to slowdown is less than the previous velocity, we want to slowdown
        if (minVelocityToSlowdown < previousVelocity) {
            velocity = min(velocity, minVelocityToSlowdown)
        }

        //If new velocity minus slowdown velocity is less than max deceleration, we want to use slowdown velocity instead. This avoids stepping glitches with the minVelocityToSlowdown.
        if (abs(velocity - slowdownVelocity) < maxDeceleration) {
            velocity = min(velocity, slowdownVelocity)
        }

        //store previous values
        storePreviousValues(velocity, dt)

        return State(velocity, DifferentialDriveState.calculateAngular(velocity, 1.0 / curvature))
    }

    /**
     * Returns the [Transform] on the path at the current trajectory position.
     *
     * A lookahead distance can be specified to preview a [Transform] at a distance ahead of the current trajectory
     * position.
     *
     * @return [Transform] at the current trajectory position + lookahead distance.
     */
    public fun getTransform(lookahead: Double = 0.0): Transform {
        val length = traveledDistance + lookahead
        if (length > totalLength) {
            val extraLength =   length - totalLength
            val lastTransform = path.getTransform(1.0)
            println(extraLength)
            return Transform(
                lastTransform.vector + Vector2D(
                    lastTransform.rotation.cos() * extraLength,
                    lastTransform.rotation.sin() * extraLength
                ), lastTransform.rotation
            )
        } else {
            return path.getTransform(path.getParameterFromLength(traveledDistance + lookahead))
        }
    }

    public fun isFinished(threshold: Double = 0.5.inches()): Boolean = abs(remainingDistance) < threshold

    private fun min(vararg values: Double): Double {
        var min = values[0]
        for (value in values) {
            min = kotlin.math.min(value, min)
        }
        return min
    }

    private fun storePreviousValues(velocity: Double, dt: Double) {
        previousVelocity = velocity
        if (autoUpdateDistance) {
            traveledDistance += velocity * dt
        }
    }

    private fun previewVelocity(distance: Double): Double =
        calculateSlowdownVelocity(path.getCurvature(path.getParameterFromLength(distance)))

    private fun calculateSlowdownVelocity(curvature: Double): Double =
        max(abs(DifferentialDriveState.calculateLinear(maxAngularVelocity, 1.0 / curvature)), minVelocity)

    private fun calculateDistanceToSlowdown(
        currentVelocity: Double,
        slowdownVelocity: Double,
        maxDeceleration: Double
    ): Double = (currentVelocity - slowdownVelocity).pow(2.0) / (2 * maxDeceleration)

    private fun calculateMaxVelocityFromDistance(
        endVelocity: Double,
        distance: Double,
        maxDeceleration: Double
    ): Double = if (distance > 0) {
        sqrt(endVelocity * endVelocity + 2 * maxDeceleration * distance)
    } else {
        0.0
    }

    private fun getMinVelFromArray(currentDistance: Double): Double = (previewedVelocities.minByOrNull {
        calculateMaxVelocityFromDistance(
            it.first,
            it.second - currentDistance,
            maxDeceleration
        )
    } ?: Pair(maxVelocity, 0.0)).let {
        calculateMaxVelocityFromDistance(
            it.first,
            it.second - currentDistance,
            maxDeceleration
        )
    }

    public fun manuallySetTraveledDistance(traveledDistance: Double) {
        this.autoUpdateDistance = false
        this.traveledDistance = traveledDistance
    }
}