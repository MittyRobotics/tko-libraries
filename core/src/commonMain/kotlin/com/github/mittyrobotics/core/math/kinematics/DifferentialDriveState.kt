package com.github.mittyrobotics.core.math.kinematics

import kotlin.math.*

public data class DifferentialDriveState(
    public val linear: Double,
    public val angular: Double,
    public val left: Double,
    public val right: Double,
    public val L: Double
) {

    public companion object {
        public fun fromLinearAndAngular(linear: Double, angular: Double, L: Double): DifferentialDriveState {
            if(angular.absoluteValue < 2e-9){
                return DifferentialDriveState(linear, angular, linear, linear, L)
            }
            else{
                val radius = linear / angular
                val left = angular * (radius - L / 2)
                val right = angular * (radius + L / 2)
                return DifferentialDriveState(linear, angular, left, right, L)
            }

        }

        public fun fromLinearAndRadius(linear: Double, radius: Double, L: Double): DifferentialDriveState =
            fromLinearAndAngular(linear, linear / radius, L)

        public fun fromAngularAndRadius(angular: Double, radius: Double, L: Double): DifferentialDriveState =
            fromLinearAndAngular(radius*angular, angular,  L)

        public fun fromLinearAndCurvature(linear: Double, curvature: Double, L: Double): DifferentialDriveState =
            fromLinearAndRadius(linear, 1.0/curvature, L)

        public fun fromAngularAndCurvature(angular: Double, curvature: Double, L: Double): DifferentialDriveState =
            fromAngularAndRadius(angular, 1.0/curvature,  L)

        public fun fromWheels(left: Double, right: Double, L: Double): DifferentialDriveState =
            fromLinearAndAngular((left + right) / 2.0, (2 * (right - left)) / L, L)

        public fun calculateLinear(angular: Double, radius: Double): Double = radius*angular

        public fun calculateAngular(linear: Double, radius: Double): Double = linear / radius
    }
}