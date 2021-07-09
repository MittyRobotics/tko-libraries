package com.github.mittyrobotics.motion.controllers

public class PID(public var p: Double = 0.0, public var i: Double = 0.0, public var d: Double = 0.0) {
    public var integral: Double = 0.0
    private var lastError = 0.0

    public fun calculate(goal: Double, measurement: Double, dt: Double): Double {
        val error = goal - measurement
        integral += error * dt
        val derivative = (error - lastError) / dt
        lastError = error
        return p * error + i * integral + d * derivative
    }

    public fun reset() {
        integral = 0.0
        lastError = 0.0
    }
}
