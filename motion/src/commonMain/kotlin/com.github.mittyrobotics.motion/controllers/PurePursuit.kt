package com.github.mittyrobotics.motion.controllers

import com.github.mittyrobotics.core.math.geometry.Circle
import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector2D
import com.github.mittyrobotics.core.math.kinematics.DifferentialDriveState

/**
 * Constructs a Pure Pursuit Controller
 * @param L robot track width (distance between left and right wheels)
 */
public class PurePursuit(public val L: Double) {
    public fun calculate(
        robotTransform: Transform,
        lookaheadPoint: Vector2D,
        linearVelocity: Double
    ): DifferentialDriveState = DifferentialDriveState.fromLinearAndRadius(
        linearVelocity,
        Circle.fromTangent(robotTransform, lookaheadPoint).radius,
        L
    )
}