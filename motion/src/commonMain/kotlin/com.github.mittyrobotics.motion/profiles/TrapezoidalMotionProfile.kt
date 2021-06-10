package com.github.mittyrobotics.motion.profiles

import com.github.mittyrobotics.motion.State
import kotlin.math.min
import kotlin.math.sqrt

public class TrapezoidalMotionProfile(
    initialState: State,
    finalState: State,
    maxStates: State,
    minState: State
) {
    private val am = maxStates.states[1]
    private val dm = minState.states[1]
    private val vi = initialState.states[1]
    private val vf = finalState.states[1]
    private val pi = initialState.states[0]
    private val pt = finalState.states[0] - pi
    private var vm = min(maxStates.states[0], sqrt(2*pt*am*dm*(dm+am))/(dm+am))
    private val ta = (vm - vi) / am
    private val xds = (vm - vf) / -dm
    private val yce = pt + pdb(xds)
    private val tc = (yce + vm * ta - pa(ta)) / vm
    private val h = tc - xds
    private val td = 2 * vf / dm + h

    public fun getStateAtTime(t: Double): State = when{
        t <= ta -> State(arrayOf(pa(t), dpa(t), ddpa()))
        t <= tc -> State(arrayOf(pc(t), dpc(), ddpc()))
        else -> State(arrayOf(pd(t), dpd(t), ddpd()))
    }

    private fun pa(t: Double) = am * (t * t) / 2 + vi * t + pi
    private fun dpa(t: Double) = am * t + vi
    private fun ddpa() = am
    private fun pc(t: Double) = vm * (t - ta) + pa(ta)
    private fun dpc() = vm
    private fun ddpc() = 0.0
    private fun pdb(t: Double) = -dm * (t * t) / 2 + vf * t
    private fun dpdb(t: Double) = -dm * t + vf
    private fun ddpdb() = -dm
    private fun pd(t: Double) = pdb(t - h) + pt
    private fun dpd(t: Double) = dpdb(t - h)
    private fun ddpd() = ddpdb()

    public fun totalTime(): Double = td
}