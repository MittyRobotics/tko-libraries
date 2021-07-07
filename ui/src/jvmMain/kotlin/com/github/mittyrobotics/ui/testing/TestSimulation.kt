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

import com.github.mittyrobotics.core.math.linalg.Matrix
import com.github.mittyrobotics.motion.models.LinearSystem
import com.github.mittyrobotics.motion.models.step
import com.github.mittyrobotics.ui.graph.Graph
import kotlin.math.pow

public fun main(){
    val Kp = 2.0
    val tau = 1.0
    val zeta = 0.25

    val A = Matrix(arrayOf(doubleArrayOf(0.0, 1.0), doubleArrayOf(-1.0/tau.pow(2.0), -2.0*zeta/tau)))
    val B = Matrix(arrayOf(doubleArrayOf(0.0), doubleArrayOf(Kp/tau.pow(2.0))))
    val C = Matrix(arrayOf(doubleArrayOf(1.0, 0.0)))
    val D = Matrix(arrayOf(doubleArrayOf(0.0)))
    val sys = LinearSystem(A, B, C, D)

    val sim = step(sys, 20.0)

    Graph().plotSystemResponse(sim, "Step Response")
}