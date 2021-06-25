package com.github.mittyrobotics.core.math.geometry

public data class Circle(val center: Vector2D, val radius: Double) {
    public val curvature: Double = 1/radius

    public companion object {
        public fun fromTangent(tangent: Transform, point: Vector2D): Circle {
            val a: Double = tangent.x
            val b: Double = tangent.y
            val c: Double = point.x
            val d: Double = point.y
            val theta: Rotation = tangent.rotation

            val cX =
                ((b + d) * (d - b) * theta.tan() - (a + c) * (a - c) * theta.tan() - 2 * (d - b) * theta.tan() * b - (2
                        * (d - b) * a)) / (2 * (theta.tan() * (c - a) + b - d))
            val cY =
                (b + d) / 2 - (c - a) / (d - b) * (cX - (a + c) * (theta.tan() * (c - a) + b - d) / (2 * (theta.tan()
                        * (c - a) + b - d)))

            var center = Vector2D(cX, cY)
            var distance: Double = center.distance(tangent.vector)

            if (distance.isNaN() || distance.isInfinite()) {
                center = Vector2D(2.0e16, 2.0e16)
                distance = 2e16
            }

            return Circle(center, distance)
        }
    }
}