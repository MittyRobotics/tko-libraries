package com.github.mittyrobotics.core.math.kinematics

import com.github.mittyrobotics.core.math.geometry.Rotation
import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector2D
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

public data class DifferentialDriveState(
    public val transform: Transform,
    public val linear: Double,
    public val angular: Double,
    public val left: Double,
    public val right: Double,
    public val L: Double
) {

    public companion object {
        public fun fromLinearAndAngular(linear: Double, angular: Double, L: Double): DifferentialDriveState {
            val radius = linear / angular
            val c = 2.0 * PI * radius
            val a = 2 * PI * linear / c
            val transform = Transform(Vector2D(radius * cos(a), radius * sin(a)), Rotation(PI/2.0 + a))
            val left = angular * (radius - L / 2)
            val right = angular * (radius + L / 2)
            return DifferentialDriveState(transform, linear, angular, left, right, L)
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
            fromLinearAndAngular((left + right) / 2.0, (2.0 * (right - (left + right) / 2.0)), L)

        public fun calculateLinear(angular: Double, radius: Double): Double = radius*angular

        public fun calculateAngular(linear: Double, radius: Double): Double = linear / radius
    }
}