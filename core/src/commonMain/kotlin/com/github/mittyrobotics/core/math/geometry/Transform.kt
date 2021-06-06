package com.github.mittyrobotics.core.math.geometry

public data class Transform(val vector: Vector = Vector(), val rotation: Rotation = Rotation()) {
    public operator fun plus(other: Transform): Transform = Transform(vector + other.vector, rotation + other.rotation)
    public operator fun minus(other: Transform): Transform = Transform(vector - other.vector, rotation - other.rotation)
    public operator fun times(other: Transform): Transform = Transform(vector * other.vector, rotation * other.rotation)
    public operator fun div(other: Transform): Transform = Transform(vector / other.vector, rotation / other.rotation)
}