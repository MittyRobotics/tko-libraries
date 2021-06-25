package com.github.mittyrobotics.core.math.geometry

import kotlin.math.pow
import kotlin.math.sqrt

public data class Vector2D(var x: Double = 0.0, var y: Double = 0.0) {
    public constructor(magnitude: Double, direction: Rotation) : this(
        direction.cos() * magnitude,
        direction.sin() * magnitude
    )

    public constructor(
            magnitude: Double,
            direction: Rotation,
            origin: Vector2D
    ) : this(direction.cos() * magnitude + origin.x, direction.sin() * magnitude + origin.y)

    /**
     * Calculates the distance from this [Vector2D] to `other`.
     *
     * @param other the [Vector2D] to find the distance to
     * @return the distance from this [Vector2D] to `other`.
     */
    public fun distance(other: Vector2D): Double = sqrt((other.x - x).pow(2.0) + (other.y - y).pow(2.0))

    /**
     * Calculates the magnitude of this [Vector2D] represented as a vector.
     *
     * In other words, this is the distance from this [Vector2D] to (0,0).
     *
     * @return the magnitude of this [Vector2D] represented as a vector.
     */
    public fun magnitude(): Double = sqrt(x.pow(2.0) + y.pow(2.0))

    /**
     * Rotates this [Vector2D] by [Rotation] `rotation` around the origin (0,0) of a standard
     * cartesian coordinate plane.
     *
     * @param rotation the [Rotation] to rotate this [Vector2D] by
     * @return a new [Vector2D] containing the rotated coordinates
     */
    public fun rotateBy(rotation: Rotation): Vector2D =
        Vector2D(x * rotation.cos() - y * rotation.sin(), x * rotation.sin() + y * rotation.cos())


    public operator fun plus(other: Vector2D): Vector2D = Vector2D(x + other.x, y + other.y)

    public operator fun minus(other: Vector2D): Vector2D = Vector2D(x - other.x, y - other.y)

    public operator fun plus(value: Double): Vector2D = Vector2D(x + value, y + value)

    public operator fun minus(value: Double): Vector2D = Vector2D(x - value, y - value)

    public operator fun times(other: Vector2D): Vector2D = Vector2D(x * other.x, y * other.y)

    public operator fun div(other: Vector2D): Vector2D = Vector2D(x / other.x, y / other.y)

    public operator fun times(scalar: Double): Vector2D = Vector2D(x * scalar, y * scalar)

    public operator fun div(scalar: Double): Vector2D = Vector2D(x / scalar, y / scalar)

    public operator fun plusAssign(other: Vector2D) {
        x += other.x
        y += other.y
    }

    public operator fun minusAssign(other: Vector2D) {
        x -= other.x
        y -= other.y
    }

    public operator fun plusAssign(value: Double) {
        x += value
        y += value
    }

    public operator fun minusAssign(value: Double) {
        x -= value
        y -= value
    }

    public operator fun timesAssign(other: Vector2D) {
        x *= other.x
        y *= other.y
    }

    public operator fun divAssign(other: Vector2D) {
        x /= other.x
        y /= other.y
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

