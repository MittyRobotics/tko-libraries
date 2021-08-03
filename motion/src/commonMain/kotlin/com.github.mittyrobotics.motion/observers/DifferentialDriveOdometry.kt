package com.github.mittyrobotics.motion.observers

import com.github.mittyrobotics.core.math.geometry.Rotation
import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector2D
import com.github.mittyrobotics.core.math.kinematics.DifferentialDriveState

public class DifferentialDriveOdometry(public val trackWidth: Double) {
    public val transform: Transform = Transform()
    public val calibratedAngle: Rotation = Rotation()

    public fun update(state: DifferentialDriveState, gyro: Rotation, dt: Double): Transform {
        transform.vector += state.calculateVector(gyro) * dt
        transform.radians = gyro.radians - calibratedAngle.radians
        return transform
    }

    public fun update(state: DifferentialDriveState, dt: Double): Transform {
        transform.radians += state.theta*dt
        transform.vector += state.calculateVector(transform.rotation) * dt
        return transform
    }

    public fun update(leftVelocity: Double, rightVelocity: Double, gyro: Rotation, dt: Double): Transform =
        update(DifferentialDriveState.fromWheels(leftVelocity, rightVelocity, trackWidth), gyro, dt)

    public fun calibrateGyro(currentAngle: Rotation, desiredAngle: Rotation){
        calibratedAngle.radians = (currentAngle-desiredAngle).radians
        transform.radians = desiredAngle.radians
    }

    public fun setVector(vector: Vector2D){
        transform.x = vector.x
        transform.y = vector.y
    }

    public fun setTransform(transform: Transform, currentAngle: Rotation){
        this.transform.x = transform.x
        this.transform.y = transform.y
        calibrateGyro(currentAngle, transform.rotation)
    }
}