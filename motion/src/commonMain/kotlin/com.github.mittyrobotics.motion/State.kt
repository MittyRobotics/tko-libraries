package com.github.mittyrobotics.motion

//Data classes dont support varargs :(
public class State(public vararg val states: Double){
    public operator fun get(i: Int): Double {
        return states[i]
    }
}
