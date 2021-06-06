package com.github.mittyrobotics.core.math.geometry

import com.github.mittyrobotics.core.math.units.AngularVelocity
import kotlin.math.*

public data class Rotation(var radians: Double = 0.0) {
    public constructor(x: Double, y: Double) : this(atan2(y, x))
    public constructor(vector: Vector) : this(atan2(vector.y, vector.x))

    /**
     * Returns the tangent of the radians
     *
     * @return the tangent of the radians
     */
    public fun tan(): Double = tan(radians)

    /**
     * Returns the sine of the radians
     *
     * @return the sine of the radians
     */
    public fun sin(): Double = sin(radians)

    /**
     * Returns the cosine of the radians
     *
     * @return the cosine of the radians
     */
    public fun cos(): Double = cos(radians)

    /**
     * Returns the sinc of the radians
     *
     *
     * Sinc is sine of radians over radians
     *
     * @return the sinc of the radians
     */
    public fun sinc(): Double = if (radians == 0.0) {
        0.0
    } else sin() / radians

    public operator fun plus(other: Rotation): Rotation = Rotation(radians + other.radians)

    public operator fun minus(other: Rotation): Rotation = Rotation(radians - other.radians)

    public operator fun div(other: Rotation): Rotation = Rotation(radians / other.radians)

    public operator fun times(other: Rotation): Rotation = Rotation(radians * other.radians)

    public operator fun plusAssign(other: Rotation) {
        radians += other.radians
    }

    public operator fun minusAssign(other: Rotation) {
        radians -= other.radians
    }

    public operator fun divAssign(other: Rotation) {
        radians /= other.radians
    }

    public operator fun timesAssign(other: Rotation) {
        radians *= other.radians
    }

    override fun toString(): String = ("Rotation($radians)")
}