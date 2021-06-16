package com.github.mittyrobotics.motion.models.motors

import kotlin.math.PI

/**
 * Motor class.
 *
 * Data for specific motors can be found from: https://motors.vex.com/
 */
public data class DCMotor(
    public val stallTorque: Double,
    public val stallCurrent: Double,
    public val freeSpeed: Double,
    public val freeCurrent: Double,
    public val numMotors: Int
) {
    public val resistance: Double = 12.0 / stallCurrent
    public val kv: Double = freeSpeed / 60.0 * (2.0 * PI) / (12.0 - resistance * freeCurrent)
    public val kt: Double = numMotors.toDouble() * stallTorque / stallCurrent

    public companion object {
        public fun falcon500(numMotors: Int): DCMotor = DCMotor(4.69, 257.0, 6380.0, 1.5, numMotors)
        public fun neo(numMotors: Int): DCMotor = DCMotor(3.36, 166.0, 5880.0, 1.3, numMotors)
        public fun cim(numMotors: Int): DCMotor = DCMotor(2.42, 133.0, 5310.0, 2.7, numMotors)
        public fun pro775(numMotors: Int): DCMotor = DCMotor(.71, 134.0, 18730.0, 0.7, numMotors)
    }
}