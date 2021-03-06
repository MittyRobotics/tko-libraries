package com.github.mittyrobotics.motion.profiles

import com.github.mittyrobotics.core.math.kinematics.DifferentialDriveState
import com.github.mittyrobotics.core.math.spline.Parametric
import com.github.mittyrobotics.motion.State
import kotlin.math.*


public class PathMotionProfile(
    public val path: Parametric,
    public val maxAcceleration: Double,
    public val maxVelocity: Double,
    public val maxAngularVelocity: Double = Double.POSITIVE_INFINITY,
    public val startVelocity: Double = 0.0,
    public val endVelocity: Double = 0.0,
    public val minVelocity: Double = 0.0,
    public val maxDeceleration: Double = maxAcceleration
): GenerativeMotionProfile() {
    private var previousVelocity: Double = startVelocity
    private var traveledDistance: Double = 0.0
    private val previewedVelocities: MutableList<Pair<Double, Double>> = mutableListOf()

    /**
     * Calculates the next [State] given a change in time since the last call to [next]
     *
     * @param dt delta time since last call to [next]
     */
    public override fun next(dt: Double): State {
        //Calculate max velocity to end
        val distanceToEnd: Double = path.getGaussianQuadratureLength() - traveledDistance
        val maxVelocityToEnd = calculateMaxVelocityFromDistance(endVelocity, distanceToEnd, maxDeceleration)
        //Calculate initial trapezoidal velocity from safe velocity controller
        var velocity: Double = min(min(previousVelocity + maxAcceleration * dt, maxVelocityToEnd), maxVelocity)

        //Calculate preview distance to slowdown from current velocity to zero velocity
        val previewDistance = calculateDistanceToSlowdown(previousVelocity, 0.0, maxDeceleration)
        //Get curvature at current point and preview point
        val curvature: Double = path.getCurvature(path.getParameterFromLength(traveledDistance))
        val curvatureAtPreview: Double =
            path.getCurvature(path.getParameterFromLength(traveledDistance + previewDistance))
        //Calculate slowdown velocity at current point and preview point
        val slowdownVelocity = calculateSlowdownVelocity(curvature)
        val slowdownVelocityAtPreview = calculateSlowdownVelocity(curvatureAtPreview)

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

        previousVelocity = velocity
        traveledDistance += velocity*dt

        return State(velocity, DifferentialDriveState.calculateAngular(velocity, 1.0/curvature))
    }

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
}