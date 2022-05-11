package com.github.mittyrobotics.motion.controllers

import kotlin.math.abs

public class PID(public var p: Double = 0.0, public var i: Double = 0.0, public var d: Double = 0.0) {
    public var integral: Double = 0.0
    private var lastError = 0.0

    public fun calculate(goal: Double, measurement: Double, dt: Double): Double = calculate(goal-measurement, dt)

    public fun calculate(error: Double, dt: Double): Double {
        val derivative = (error - lastError) / dt
        lastError = error
        val pOut = p * error
        val iOut = i * integral
        val dOut = d * derivative
        val pdOut = pOut+dOut
        if(abs(pdOut) <= 12){
            integral += error * dt
            return pdOut + iOut
        }
        return pdOut
    }


    public fun reset() {
        integral = 0.0
        lastError = 0.0
    }
}
