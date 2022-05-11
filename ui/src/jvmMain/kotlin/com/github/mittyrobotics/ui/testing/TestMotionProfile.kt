package com.github.mittyrobotics.ui.testing

import com.github.mittyrobotics.motion.State
<<<<<<< Updated upstream
import com.github.mittyrobotics.motion.profiles.TrapezoidalMotionProfile
import com.github.mittyrobotics.ui.graph.Graph
import kotlin.math.max

public fun main(){
    val initialState = State().position(0.0).velocity(0.0)
    val finalState = State().position(20.0).velocity(0.0)
    val maxState = State().position(3.0).velocity(1.0)
    val minState = State().position(3.0).velocity(1.0)
    val profile = TrapezoidalMotionProfile(initialState, finalState, maxState, minState)
    Graph().plot(profile, "Motion Profile")

}
=======
import com.github.mittyrobotics.motion.controllers.PID
import com.github.mittyrobotics.motion.models.SystemResponse
import com.github.mittyrobotics.motion.profiles.TrapezoidalMotionProfile
import com.github.mittyrobotics.ui.graph.Graph
import com.github.mittyrobotics.ui.graph.themes.PaperTheme
import java.awt.print.Paper
import java.lang.Math.pow
import kotlin.math.pow

/**
 * Simulated robotic system running a motion profiled PID control loop.
 *
 * Author: Owen Leather
 */
public fun main() {

    val dt = 0.001
    var t = 0.0
    var v = 140.0
    var x = 0.0

    while (t < 0.2) {
        val a = -1120.0* (1 + 4 * t).pow(-3.0)
        v += a*dt
        x += v*dt
        t += dt
    }


    println(v)
    println(x)
}

>>>>>>> Stashed changes
