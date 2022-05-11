package com.github.mittyrobotics.ui.testing

import com.github.mittyrobotics.core.math.linalg.Matrix
import com.github.mittyrobotics.motion.controllers.PID
import com.github.mittyrobotics.motion.models.SystemResponse
import com.github.mittyrobotics.motion.models.flywheel
import com.github.mittyrobotics.motion.models.motors.DCMotor
import com.github.mittyrobotics.motion.models.sim
import com.github.mittyrobotics.motion.models.simNext
import com.github.mittyrobotics.ui.graph.Graph

public fun main(){
    val singleFalconFlywheel = flywheel(DCMotor.falcon500(1), 2.0, 0.00119616458347)
    val dualFalconFlywheel = flywheel(DCMotor.falcon500(2), 1.0,0.00119616458347)
    val singleFalconFlywheel12VResponse = sim(singleFalconFlywheel, Matrix.fill(1,1,12.0), dt=0.0001)
    val dualFalconFlywheel12VResponse = sim(dualFalconFlywheel, Matrix.fill(1, 1, 12.0), dt=0.0001)


    Graph("Flywheel Response").let {
        it.plot(singleFalconFlywheel12VResponse.also { it.x.forEach { it *= 9.549297 }}.also { it.u.first() *= 200.0 }, "Single Falcon Flywheel Step Response")
        it.plot(dualFalconFlywheel12VResponse.also { it.x.forEach { it *= 9.549297 }}.also { it.u.first() *= 200.0 }, "Dual Falcon Flywheel Step Response")
    }

    val graph = Graph("Flywheel Step Response")
    for(i in 0 until 2){
        val dt = 0.0001

        var lastX = Matrix.zeros(1, 1)

        val PID = PID(0.1, 0.0, 0.0)

        val xs = mutableListOf<Matrix>()
        val ys = mutableListOf<Matrix>()
        val us = mutableListOf<Matrix>()
        val ts = mutableListOf<Double>()

        val setpoint = 3000
        for(t in 0 until (10/dt).toInt()){
            val pid = Math.max(Math.min(PID.calculate(setpoint/9.549297, lastX.get2DData(0), dt) + setpoint*(12.0/if(i==1){6418}else{3210}), 12.0), -12.0)
            val u = Matrix.fill(1, 1, pid)
            val response = simNext(if(i==0){singleFalconFlywheel}else{dualFalconFlywheel}, lastX, u, dt)
            lastX = response.first
            xs.add(response.first)
            ys.add(response.second)
            us.add(u)
            ts.add(t*dt)
        }


            graph.plot(SystemResponse(xs.toTypedArray(), ys.toTypedArray(), us.toTypedArray(), ts.toTypedArray()).also { it.x.forEach { it *= 9.549297 }}.also { it.u.forEach { it *= 200.0 } }, "${i+1} Falcon Flywheel Step Response")

    }
}