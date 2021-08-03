package com.github.mittyrobotics.core.math.geometry

public data class Transform(val vector: Vector2D = Vector2D(), val rotation: Rotation = Rotation()) {
    var x: Double
        get() = vector.x
        set(value){
            vector.x = value
        }
    var y: Double
        get() = vector.y
        set(value){
            vector.y = value
        }
    var radians: Double
        get() = rotation.radians
        set(value){
            rotation.radians = value
        }

    public operator fun plus(other: Transform): Transform = Transform(vector + other.vector, rotation + other.rotation)
    public operator fun minus(other: Transform): Transform = Transform(vector - other.vector, rotation - other.rotation)
    public operator fun times(other: Transform): Transform = Transform(vector * other.vector, rotation * other.rotation)
    public operator fun div(other: Transform): Transform = Transform(vector / other.vector, rotation / other.rotation)

    /**
     * Calculates the distance from this [Vector2D] to `other`.
     *
     * @param other the [Vector2D] to find the distance to
     * @return the distance from this [Vector2D] to `other`.
     */
    public fun distance(other: Transform): Double = other.vector.distance(this.vector)
    public fun rotateAround(origin: Vector2D, rotation: Rotation): Transform =
        Transform((vector - origin).rotateBy(rotation) + origin, rotation + rotation)
}
