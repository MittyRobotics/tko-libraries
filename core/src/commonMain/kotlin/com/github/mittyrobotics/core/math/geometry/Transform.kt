package com.github.mittyrobotics.core.math.geometry

import kotlin.math.pow
import kotlin.math.sqrt

public data class Transform(val vector: Vector = Vector(), val rotation: Rotation = Rotation()) {
    public operator fun plus(other: Transform): Transform = Transform(vector + other.vector, rotation + other.rotation)
    public operator fun minus(other: Transform): Transform = Transform(vector - other.vector, rotation - other.rotation)
    public operator fun times(other: Transform): Transform = Transform(vector * other.vector, rotation * other.rotation)
    public operator fun div(other: Transform): Transform = Transform(vector / other.vector, rotation / other.rotation)
    /**
     * Calculates the distance from this [Vector] to `other`.
     *
     * @param other the [Vector] to find the distance to
     * @return the distance from this [Vector] to `other`.
     */
    public fun distance(other: Transform): Double = sqrt((other.vector.x - vector.x).pow(2.0) + (other.vector.y - vector.y).pow(2.0))
    public fun rotateAround(origin: Vector, rotation: Rotation): Transform {
        val pos: Vector = (vector - origin).rotateBy(rotation) + origin
        val rot: Rotation = rotation + rotation

        return Transform(pos, rot)
    }

}
