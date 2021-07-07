package com.github.mittyrobotics.ui

import com.github.mittyrobotics.core.math.geometry.Rotation
import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector2D
import com.github.mittyrobotics.core.math.spline.Path
import com.github.mittyrobotics.motion.profiles.PathMotionProfile
import com.github.mittyrobotics.ui.graph.Graph
import kotlin.math.PI

public fun main() {
    val path = Path.quinticHermitePath(
        arrayOf(
            Transform(Vector2D(), Rotation(PI/2.0)),
            Transform(Vector2D(10.0, -5.0), Rotation(PI/2.0))
        )
    )
    val graph = Graph()
    graph.plotParametric(path, "Path")
    val velocities = mutableListOf<Vector2D>()
    val angularVelocities = mutableListOf<Vector2D>()
    val distancesRemaining = mutableListOf<Vector2D>()
    val curvatures = mutableListOf<Vector2D>()
    val profile = PathMotionProfile(path, 1.0, 10.0, 2.0, 0.0, 0.0)
    val dt = 0.02
    var distance = 0.0
    for (i in 0 until 1000) {
        val t = i * dt
        val state = profile.next(dt)
        val velocity = state[0]
        val angularVelocity = state[1]
        distance += velocity * dt
        velocities.add(Vector2D(t, velocity))
        angularVelocities.add(Vector2D(t, angularVelocity))
        distancesRemaining.add(Vector2D(t, path.getGaussianQuadratureLength() - distance))
        curvatures.add(Vector2D(t, path.getCurvature(path.getParameterFromLength(distance))))
    }

    graph.plot(velocities.toTypedArray(), "Linear Velocity")
    graph.plot(angularVelocities.toTypedArray(), "Angular Velocity")
    graph.plot(distancesRemaining.toTypedArray(), "Distance Remaining")
//    graph.plot(curvatures.toTypedArray(), "Curvature")
}