package com.github.mittyrobotics.motion.controllers

import com.github.mittyrobotics.core.math.geometry.Circle
import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector2D
import com.github.mittyrobotics.core.math.kinematics.DifferentialDriveState

public fun purePursuit(
    robotTransform: Transform,
    lookaheadPoint: Vector2D,
    linearVelocity: Double,
    L: Double
): DifferentialDriveState = DifferentialDriveState.fromLinearAndRadius(
    linearVelocity,
    Circle.fromTangent(robotTransform, lookaheadPoint).radius,
    L
)
