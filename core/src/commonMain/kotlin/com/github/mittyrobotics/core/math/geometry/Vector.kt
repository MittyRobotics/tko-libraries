package com.github.mittyrobotics.core.math.geometry

import kotlin.math.pow
import kotlin.math.sqrt

public data class Vector(var x: Double = 0.0, var y: Double = 0.0) {

    /**
     * Calculates the distance from this [Vector] to `other`.
     *
     * @param other the [Vector] to find the distance to
     * @return the distance from this [Vector] to `other`.
     */
    public fun distance(other: Vector): Double = sqrt((other.x - x).pow(2.0) + (other.y - y).pow(2.0))

    /**
     * Calculates the magnitude of this [Vector] represented as a vector.
     *
     * In other words, this is the distance from this [Vector] to (0,0).
     *
     * @return the magnitude of this [Vector] represented as a vector.
     */
    public fun magnitude(): Double = sqrt(x.pow(2.0) + y.pow(2.0))

    public operator fun plus(other: Vector): Vector = Vector(x + other.x, y + other.y)

    public operator fun minus(other: Vector): Vector = Vector(x - other.x, y - other.y)

    public operator fun times(scalar: Double): Vector = Vector(x * scalar, y * scalar)

    public operator fun div(scalar: Double): Vector = Vector(x / scalar, y / scalar)

    public operator fun plusAssign(other: Vector) {
        x += other.x
        y += other.y
    }

    public operator fun minusAssign(other: Vector) {
        x -= other.x
        y -= other.y
    }

    public operator fun timesAssign(scalar: Double) {
        x *= scalar
        y *= scalar
    }

    public operator fun divAssign(scalar: Double) {
        x /= scalar
        y /= scalar
    }

    override fun toString(): String {
        return ("Vector($x,$y)")
    }
}

