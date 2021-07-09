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

public fun drivetrain(
    motor: DCMotor,
    m: Double,
    G: Double,
    J: Double,
    r: Double,
    rb: Double
): LinearSystem {
    val C1 = -G.pow(2.0) * motor.kt / (motor.kv * motor.resistance * r.pow(2.0))
    val C2 = G * motor.kt / (motor.resistance * r)

    return LinearSystem(
        Matrix(
            arrayOf(
                doubleArrayOf((1.0 / m + rb.pow(2.0) / J) * C1, (1.0 / m - rb.pow(2.0) / J) * C1),
                doubleArrayOf((1.0 / m - rb.pow(2.0) / J) * C1, (1.0 / m + rb.pow(2.0) / J) * C1)
            )
        ),
        Matrix(
            arrayOf(
                doubleArrayOf((1.0 / m + rb.pow(2.0) / J) * C2, (1.0 / m - rb.pow(2.0) / J) * C2),
                doubleArrayOf((1.0 / m - rb.pow(2.0) / J) * C2, (1.0 / m + rb.pow(2.0) / J) * C2)
            )
        ),
        Matrix(arrayOf(doubleArrayOf(1.0, 0.0), doubleArrayOf(0.0, 1.0))),
        Matrix(arrayOf(doubleArrayOf(0.0, 0.0), doubleArrayOf(0.0, 0.0)))
    )
}
