package com.github.mittyrobotics.motion.models

import com.github.mittyrobotics.core.math.linalg.*
import com.github.mittyrobotics.core.math.linalg.Matrix.Companion.fill
import com.github.mittyrobotics.core.math.linalg.Matrix.Companion.zeros

/**
 * Simulates the step response for a system for an amount of time
 * @param sys system model
 * @param time simulation time
 * @param stepMagnitude magnitude of step input
 * @param x0 initial state vector
 * @return array of triples in the form of Triple(state, input, time]) representing the response
 */
public fun step(
    sys: SystemModel,
    time: Double,
    dt: Double = 0.01,
    stepMagnitude: Double = 1.0,
    x0: Matrix = zeros(sys.A.rows, 1)
): SystemResponse {
    return sim(sys, arrayOf(Pair(fill(sys.A.rows, 1, stepMagnitude), 0.0)), time, dt, x0)
}

/**
 * Simulates the step response for a system for an amount of time
 * @param sys system model
 * @param time simulation time
 * @param stepMagnitude magnitude of step input
 * @param x0 initial state vector
 * @return array of triples in the form of Triple(state, input, time]) representing the response
 */
public fun sim(
    sys: SystemModel,
    input: Matrix,
    time: Double = 10.0,
    dt: Double = 0.01,
    x0: Matrix = zeros(sys.A.rows, 1)
): SystemResponse {
    return sim(sys, arrayOf(Pair(input, 0.0)), time, dt, x0)
}

/**
 * Simulates the system response for a given array of control inputs and timestamps
 * @param sys system model
 * @param UT array of control inputs and timestamps in the form of a Pair(input, time)
 * @param x0 initial state vector
 * @return array of triples in the form of Triple(state, input, time]) representing the response
 */
public fun sim(
    sys: SystemModel,
    UT: Array<Pair<Matrix, Double>>,
    x0: Matrix = zeros(sys.A.rows, 1)
): SystemResponse {
    val outputs = mutableListOf<Matrix>()
    val inputs = mutableListOf<Matrix>()
    val times = mutableListOf<Double>()

    var x = x0
    for (i in 1 until UT.size) {
        val U = UT[i].first
        val T = UT[i].second
        val dt = T - UT[i - 1].second
        x = simNext(sys, x, U, dt)
        outputs.add(x)
        inputs.add(U)
        times.add(T)
    }
    return SystemResponse(outputs.toTypedArray(), inputs.toTypedArray(), times.toTypedArray())
}

/**
 * Simulates the system response for a given array of control inputs and timestamps
 * @param sys system model
 * @param UT array of control inputs and timestamps in the form of a Pair(input, time)
 * @param time total simulation time
 * @param dt change in time per step
 * @param x0 initial state vector
 * @return array of triples in the form of Triple(state, input, time]) representing the response
 */
public fun sim(
    sys: SystemModel,
    UT: Array<Pair<Matrix, Double>>,
    time: Double,
    dt: Double,
    x0: Matrix = zeros(sys.A.rows, 1)
): SystemResponse {
    val inputs = mutableListOf<Pair<Matrix, Double>>()
    var u = zeros(sys.B.cols, 1)
    var uti = 0
    for (i in 0 until (time / dt).toInt()) {
        val t = i * dt
        if (uti < UT.size && t >= UT[uti].second) {
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
 * @param sys system model
 * @param x0 initial state vector
 * @param u control input
 * @param dt delta time
 * @return x state vector
 */
public fun simNext(sys: SystemModel, x0: Matrix, u: Matrix, dt: Double): Matrix {
    val inputs = sys.B.cols
    val states = sys.A.rows

    val y = sys.C*x0 + sys.D*u

    val M = expm(vstack(hstack(sys.A_(mapOf("y" to y)) * dt, sys.B_(mapOf("y" to y)) * dt), zeros(inputs, states + inputs)))
    val H = vstack(x0, u)
    val K = M * H

    return K.subMatrix(endRow = states)
}

public data class SystemResponse(
    public val outputs: Array<Matrix>,
    public val inputs: Array<Matrix>,
    public val times: Array<Double>
)