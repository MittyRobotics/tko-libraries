package com.github.mittyrobotics.ui.testing

import com.github.mittyrobotics.motion.State
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