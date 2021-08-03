package com.github.mittyrobotics.core.math.kinematics

import com.github.mittyrobotics.core.math.geometry.Rotation
import com.github.mittyrobotics.core.math.geometry.Vector2D
import kotlin.math.*

public data class DifferentialDriveState(
    public val linear: Double,
    public val angular: Double,
    public val left: Double,
    public val right: Double,
    public val theta: Double,
    public val trackWidth: Double
) {

    public fun calculateVector(rotation: Rotation): Vector2D{
        return Vector2D(linear*rotation.cos(), linear*rotation.sin())
    }

    public companion object {
        public fun fromLinearAndAngular(linear: Double, angular: Double, trackWidth: Double): DifferentialDriveState {
            if(angular.absoluteValue < 2e-9){
                return DifferentialDriveState(linear, angular, linear, linear, 0.0, trackWidth)
            }
            else{
                val radius = linear / angular
                val left = angular * (radius - trackWidth / 2)
                val right = angular * (radius + trackWidth / 2)
                val theta = (right-left)/(2.0*trackWidth)
                return DifferentialDriveState(linear, angular, left, right, theta, trackWidth)
            }
        }

        public fun fromLinearAndRadius(linear: Double, radius: Double, trackWidth: Double): DifferentialDriveState =
            fromLinearAndAngular(linear, linear / radius, trackWidth)

        public fun fromAngularAndRadius(angular: Double, radius: Double, trackWidth: Double): DifferentialDriveState =
            fromLinearAndAngular(radius*angular, angular,  trackWidth)

        public fun fromLinearAndCurvature(linear: Double, curvature: Double, trackWidth: Double): DifferentialDriveState =
            fromLinearAndRadius(linear, 1.0/curvature, trackWidth)

        public fun fromAngularAndCurvature(angular: Double, curvature: Double, trackWidth: Double): DifferentialDriveState =
            fromAngularAndRadius(angular, 1.0/curvature,  trackWidth)

        public fun fromWheels(left: Double, right: Double, trackWidth: Double): DifferentialDriveState =
            DifferentialDriveState((left + right) / 2.0, (2 * (right - left)) / trackWidth, left, right, (right-left)/(2.0*trackWidth), trackWidth)

        public fun calculateLinear(angular: Double, radius: Double): Double = radius*angular

        public fun calculateAngular(linear: Double, radius: Double): Double = linear / radius
    }
}