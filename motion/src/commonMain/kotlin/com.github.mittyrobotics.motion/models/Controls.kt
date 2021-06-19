package com.github.mittyrobotics.motion.models

import com.github.mittyrobotics.core.math.linalg.*
import com.github.mittyrobotics.core.math.linalg.Matrix.Companion.fill
import com.github.mittyrobotics.core.math.linalg.Matrix.Companion.zeros

/**
 * Simulates the step response for a system for an amount of time
 * @param sys linear system
 * @param time simulation time
 * @param stepMagnitude magnitude of step input
 * @param x0 initial state vector
 * @return array of triples in the form of Triple(state, input, time]) representing the response
 */
public fun step(sys: LinearSystem, time: Double, dt: Double = 0.01, stepMagnitude: Double = 1.0, x0: Matrix = zeros(sys.A.rows, 1)): Array<Triple<Matrix, Matrix, Double>>{
    return sim(sys, arrayOf(Pair(fill(sys.A.rows, 1, stepMagnitude), 0.0)), time, dt, x0)
}

/**
 * Simulates the system response for a given array of control inputs and timestamps
 * @param sys linear system
 * @param UT array of control inputs and timestamps in the form of a Pair(input, time)
 * @param x0 initial state vector
 * @return array of triples in the form of Triple(state, input, time]) representing the response
 */
public fun sim(sys: LinearSystem, UT: Array<Pair<Matrix, Double>>, x0: Matrix = zeros(sys.A.rows, 1)): Array<Triple<Matrix, Matrix, Double>>{
    val arr = mutableListOf<Triple<Matrix, Matrix, Double>>()

    var x = x0
    for(i in 1 until UT.size){
        val U = UT[i].first
        val T = UT[i].second
        val dt = T-UT[i-1].second
        x = simNext(sys, x, U, dt)
        arr.add(Triple(x, U, T))
    }
    return arr.toTypedArray()
}

/**
 * Simulates the system response for a given array of control inputs and timestamps
 * @param sys linear system
 * @param UT array of control inputs and timestamps in the form of a Pair(input, time)
 * @param time total simulation time
 * @param dt change in time per step
 * @param x0 initial state vector
 * @return array of triples in the form of Triple(state, input, time]) representing the response
 */
public fun sim(sys: LinearSystem, UT: Array<Pair<Matrix, Double>>, time: Double, dt: Double, x0: Matrix = zeros(sys.A.rows, 1)): Array<Triple<Matrix, Matrix, Double>>{
    val inputs = mutableListOf<Pair<Matrix, Double>>()
    var u = zeros(sys.B.cols, 1)
    var uti = 0
    for(i in 0 until (time/dt).toInt()){
        val t = i * dt
        if(uti < UT.size && t >= UT[uti].second){
            u = UT[uti].first
            uti++
        }
        inputs.add(Pair(u, t))
    }
    return sim(sys, inputs.toTypedArray(), x0)
}

/**
 * Simulates next state for given initial conditions and delta time
 *
 *  Zero-order hold from scipy lsim() function:
 *  Algorithm: to integrate from time 0 to time dt, we solve
 *  xdot = A x + B u,  x(0) = x0
 *  udot = 0,          u(0) = u0.
 *
 *  Solution is:
 *  [ x(dt) ]       [ A*dt   B*dt ] [ x0 ]
 *  [ u(dt) ] = exp [  0     0    ] [ u0 ]
 *
 *
 * @param sys linear system
 * @param x0 initial position
 * @param u control input
 * @param dt delta time
 * @return x state vector
 */
public fun simNext(sys: LinearSystem, x0: Matrix, u: Matrix, dt: Double): Matrix{
    val inputs = sys.B.cols
    val states = sys.A.rows

    val M = expm(vstack(hstack(sys.A * dt, sys.B * dt), zeros(inputs, states+inputs)))
    val H = vstack(x0, u)
    val K = M*H

    return K.subMatrix(endRow = states)
}