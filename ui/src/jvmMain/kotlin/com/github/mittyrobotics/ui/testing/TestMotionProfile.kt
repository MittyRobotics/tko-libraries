package com.github.mittyrobotics.ui.testing

import com.github.mittyrobotics.core.math.geometry.Vector2D
import com.github.mittyrobotics.motion.State
import com.github.mittyrobotics.motion.models.SystemResponse
import com.github.mittyrobotics.motion.profiles.TrapezoidalMotionProfile
import com.github.mittyrobotics.ui.graph.Graph

public fun main(){
    val motionProfile = TrapezoidalMotionProfile(State(0.0, 0.0), State(259.49, 0.0), State(100.0, 100.0), State(100.0, 100.0))
    Graph().plot(Array(100){ Vector2D((it/100.0)*motionProfile.totalTime, motionProfile.getStateAtTime((it/100.0)*motionProfile.totalTime)[0])}, "System")
}
