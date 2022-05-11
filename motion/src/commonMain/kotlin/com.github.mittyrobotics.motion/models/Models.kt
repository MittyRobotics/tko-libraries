package com.github.mittyrobotics.motion.models

import com.github.mittyrobotics.core.math.linalg.Matrix
import com.github.mittyrobotics.motion.models.motors.DCMotor
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

/**
 * Creates an elevator [SystemModel].
 *
 * https://file.tavsys.net/control/controls-engineering-in-frc.pdf#page=79
 *
 * @param motor motor
 * @param m mass
 * @param G gear reduction
 * @param r pulley radius
 */
public fun elevator(motor: DCMotor, m: Double, G: Double, r: Double): SystemModel =
    SystemModel(
        Matrix(
            arrayOf(
                doubleArrayOf(0.0, 1.0),
                doubleArrayOf(
                    0.0,
                    -G.pow(2.0) * motor.kt / (motor.resistance * r.pow(2.0) * m * motor.kv)
                )
            )
        ),
        Matrix(
            arrayOf(
                doubleArrayOf(0.0),
                doubleArrayOf(G * motor.kt / (motor.resistance * r * m))
            )
        ),
        Matrix(arrayOf(doubleArrayOf(1.0, 0.0))),
        Matrix(arrayOf(doubleArrayOf(0.0)))
    )

public fun flywheel(motor: DCMotor, G: Double, J: Double): SystemModel =
    SystemModel(
        Matrix(
            arrayOf(
                doubleArrayOf(-G*G*motor.kt/(motor.kv*motor.resistance*J))
            )
        ),
        Matrix(
            arrayOf(
                doubleArrayOf(G*motor.kt/(motor.resistance*J))
            )
        ),
        Matrix(
            arrayOf(
                doubleArrayOf(1.1)
            )
        ),
        Matrix(
            arrayOf(
                doubleArrayOf(0.0)
            )
        )
    )

/**
 * Creates a drivetrain [SystemModel].
 *
 * https://file.tavsys.net/control/controls-engineering-in-frc.pdf#page=91
 *
 * @param motor motor
 * @param m mass
 * @param G gear reduction
 * @param J moment of inertia
 * @param r wheel radius
 * @param tw track width
 */
public fun drivetrain(
    motor: DCMotor,
    m: Double,
    G: Double,
    J: Double,
    r: Double,
    tw: Double
): SystemModel {
    val C1 = -G.pow(2.0) * motor.kt / (motor.kv * motor.resistance * r.pow(2.0))
    val C2 = G * motor.kt / (motor.resistance * r)

    fun Map<String, Matrix>.theta(): Double = get("y")?.get2DData(0) ?: 0.0
    fun Map<String, Matrix>.vl(): Double = get("y")?.get2DData(1) ?: 0.0
    fun Map<String, Matrix>.vr(): Double = get("y")?.get2DData(2) ?: 0.0

    val v: (Map<String, Matrix>) -> Double = { (it.vl() + it.vr()) / 2.0 }

    return SystemModel({
        Matrix(
            arrayOf(
                doubleArrayOf(0.0, 0.0, -v(it) * sin(it.theta()), cos(it.theta()) / 2.0, cos(it.theta()) / 2.0),
                doubleArrayOf(0.0, 0.0, v(it) * cos(it.theta()), sin(it.theta()) / 2.0, sin(it.theta()) / 2.0),
                doubleArrayOf(0.0, 0.0, 0.0, -1.0 / (2.0 * tw), 1 / (2.0 * tw)),
                doubleArrayOf(0.0, 0.0, 0.0, (1.0 / m + tw.pow(2.0) / J) * C1, (1.0 / m - tw.pow(2.0) / J) * C1),
                doubleArrayOf(0.0, 0.0, 0.0, (1.0 / m - tw.pow(2.0) / J) * C1, (1.0 / m + tw.pow(2.0) / J) * C1)
            )
        )
    },
        {
            Matrix(
                arrayOf(
                    doubleArrayOf(0.0, 0.0),
                    doubleArrayOf(0.0, 0.0),
                    doubleArrayOf(0.0, 0.0),
                    doubleArrayOf((1.0 / m + tw.pow(2.0) / J) * C2, (1.0 / m - tw.pow(2.0) / J) * C2),
                    doubleArrayOf((1.0 / m - tw.pow(2.0) / J) * C2, (1.0 / m + tw.pow(2.0) / J) * C2)
                )
            )
        },
        {
            Matrix(
                arrayOf(
                    doubleArrayOf(0.0, 0.0, 1.0, 0.0, 0.0),
                    doubleArrayOf(0.0, 0.0, 0.0, 1.0, 0.0),
                    doubleArrayOf(0.0, 0.0, 0.0, 0.0, 1.0)
                )
            )
        },
        {
            Matrix(
                arrayOf(
                    doubleArrayOf(0.0, 0.0),
                    doubleArrayOf(0.0, 0.0),
                    doubleArrayOf(0.0, 0.0)
                )
            )
        }
    )
}
