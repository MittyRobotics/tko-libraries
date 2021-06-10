/*
 * MIT License
 *
 * Copyright (c) 2020 Mitty Robotics (Team 1351)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.mittyrobotics.ui

import com.github.mittyrobotics.core.math.geometry.Rotation
import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector
import com.github.mittyrobotics.core.math.spline.QuinticHermiteSpline
import com.github.mittyrobotics.motion.State
import com.github.mittyrobotics.motion.profiles.TrapezoidalMotionProfile
import kotlin.math.PI

public fun main() {
    val profile = TrapezoidalMotionProfile(State(arrayOf(0.0, 0.0)), State(arrayOf(10.0, 0.0)), State(arrayOf(50.0, 10.0)), State(arrayOf(5.0, 10.0)))
    val graph = MotorGraph()
    for(i in 0..100){
        val t = (i.toDouble()/100.0)*profile.totalTime
        val state = profile.getStateAtTime(t)
        graph.addPosition(state.states[0], t)
        graph.addVelocity(state.states[1], t)
        graph.addAcceleration(state.states[2], t)
    }
}
