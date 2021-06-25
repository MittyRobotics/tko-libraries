package com.github.mittyrobotics.ui

import com.github.mittyrobotics.core.math.geometry.Rotation
import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector2D
import com.github.mittyrobotics.core.math.spline.Path
import com.github.mittyrobotics.motion.profiles.PathFollowerMotionProfile
import com.github.mittyrobotics.ui.GraphUtil.populateSeries
import kotlin.math.PI

public fun main(){
    val path = Path.quinticHermitePath(arrayOf(Transform(), Transform(Vector2D(10.0, -5.0)), Transform(Vector2D(10.0, 5.0), Rotation(0.0))))
    val graph = Graph()
    graph.addSeries(populateSeries(XYSeriesWithRenderer("Path", isShowShapes = false), GraphUtil.parametric(path, .01, .1)))
    val velocities = mutableListOf<Vector2D>()
    val distancesRemaining = mutableListOf<Vector2D>()
    val curvatures = mutableListOf<Vector2D>()
    val profile = PathFollowerMotionProfile(path, 1.0, 10.0, .5)
    val dt = 0.02
    var distance = 0.0
    var currentVelocity = profile.startVelocity
    for(i in 0 until 1000){
        val t = i*dt
        val velocity = profile.calculate(currentVelocity, distance, dt)
        currentVelocity = velocity
        distance += currentVelocity*dt
        velocities.add(Vector2D(t, velocity))
        distancesRemaining.add(Vector2D(t, path.getGaussianQuadratureLength()-distance))
        curvatures.add(Vector2D(t, path.getCurvature(path.getParameterFromLength(distance))))
    }

    graph.addSeries(populateSeries(XYSeriesWithRenderer("Velocity"), velocities.toTypedArray()))
    graph.addSeries(populateSeries(XYSeriesWithRenderer("Distance Remaining"), distancesRemaining.toTypedArray()))
    graph.addSeries(populateSeries(XYSeriesWithRenderer("Curvature"), curvatures.toTypedArray()))
}