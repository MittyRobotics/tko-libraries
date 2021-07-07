package com.github.mittyrobotics.motion.models

import com.github.mittyrobotics.core.math.linalg.Matrix
import com.github.mittyrobotics.motion.models.motors.DCMotor
import kotlin.math.pow

public fun elevator(motor: DCMotor, mass: Double, gearReduction: Double, pulleyRadius: Double): LinearSystem =
    LinearSystem(
        Matrix(
            arrayOf(
                doubleArrayOf(0.0, 1.0),
                doubleArrayOf(
                    0.0,
                    -gearReduction.pow(2.0) * motor.kt / (motor.resistance * pulleyRadius.pow(2.0) * mass * motor.kv)
                )
            )
        ),
        Matrix(
            arrayOf(
                doubleArrayOf(0.0),
                doubleArrayOf(gearReduction * motor.kt / (motor.resistance * pulleyRadius * mass))
            )
        ),
        Matrix(arrayOf(doubleArrayOf(1.0, 0.0))),
        Matrix(arrayOf(doubleArrayOf(0.0)))
    )