package com.github.mittyrobotics.motion.observers

import com.github.mittyrobotics.core.math.geometry.Rotation
import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector2D

public fun knownHeight(realHeight: Double, pitch: Rotation, yaw:Rotation): RobotRelative3DVisionTarget = TODO()
public fun knownDimension(realDimension: Double, screenDimension: Double, yaw: Rotation): RobotRelative3DVisionTarget = TODO()
public fun calculateDistanceFromPitch(realHeight: Double, pitch: Rotation): Double = TODO()
public fun calculateDistanceFromScreenSize(realDimension: Double, screenDimension: Double): Double = TODO()
public fun estimateCameraPositionFromVisionTarget(robotRelativeTarget: RobotRelative3DVisionTarget, targetFieldTransform: Transform): Transform = TODO()
public fun transformCameraToRobotPosition(cameraFieldTransform: Transform, cameraTransformOnRobot: Transform): Transform = TODO()

public data class RobotRelative3DVisionTarget(val position: Vector2D, val yawRot: Rotation)