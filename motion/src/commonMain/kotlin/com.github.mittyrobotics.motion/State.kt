package com.github.mittyrobotics.motion

//Data classes dont support varargs :(
public class State(vararg states: Double) {
    public val states: MutableList<Double> = states.toMutableList()

    public operator fun get(i: Int): Double {
        return states[i]
    }

    public fun position(position: Double): State {
        addValue(0, position)
        return this
    }

    public fun velocity(velocity: Double): State {
        addValue(1, velocity)
        return this
    }

    public fun acceleration(acceleration: Double): State {
        addValue(2, acceleration)
        return this
    }

    public fun jerk(jerk: Double): State {
        addValue(3, jerk)
        return this
    }

    private fun addValue(index: Int, value: Double){
        for(i in states.lastIndex+1 until index+1){
            states.add(i, 0.0)
        }
        states[index] = value
    }
}
