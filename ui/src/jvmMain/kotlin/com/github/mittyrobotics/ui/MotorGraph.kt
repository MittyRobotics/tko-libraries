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

import kotlin.jvm.JvmOverloads
import org.jfree.data.xy.XYDataItem

public class MotorGraph @JvmOverloads constructor(name: String = "Graph", yName: String = "y", xName: String = "x") :
    Graph(name, yName, xName) {
    private val positionKey = "Position"
    private val velocityKey = "Velocity"
    private val accelerationKey = "Acceleration"
    private val voltageKey = "Voltage"
    private val setpointKey = "Setpoint"
    private val errorKey = "Error"

    public fun addMotorValues(position: Double, velocity: Double, acceleration: Double, voltage: Double, time: Double) {
        addPosition(position, time)
        addVelocity(velocity, time)
        addAcceleration(acceleration, time)
        addVoltage(voltage, time)
    }

    public fun addPosition(position: Double, time: Double) {
        //convert to inches
        addToSeries(positionKey, XYDataItem(time, position))
    }

    public fun addVelocity(velocity: Double, time: Double) {
        //convert to inches
        addToSeries(velocityKey, XYDataItem(time, velocity))
    }

    public fun addAcceleration(acceleration: Double, time: Double) {
        //convert to inches
        addToSeries(accelerationKey, XYDataItem(time, acceleration))
    }

    public fun addVoltage(voltage: Double, time: Double) {
        //convert to inches
        addToSeries(voltageKey, XYDataItem(time, voltage))
    }

    public fun addSetpoint(setpoint: Double, time: Double) {
        //convert to inches
        addToSeries(setpointKey, XYDataItem(time, setpoint))
    }

    public fun addError(error: Double, time: Double) {
        //convert to inches
        addToSeries(errorKey, XYDataItem(time, error))
    }

    init {
        val positionSeries = XYSeriesWithRenderer(positionKey)
        positionSeries.isShowShapes = true
        val velocitySeries = XYSeriesWithRenderer(velocityKey)
        velocitySeries.isShowShapes = true
        val accelerationSeries = XYSeriesWithRenderer(accelerationKey)
        accelerationSeries.isShowShapes = true
        val voltageSeries = XYSeriesWithRenderer(voltageKey)
        voltageSeries.isShowShapes = true
        val setpointSeries = XYSeriesWithRenderer(setpointKey)
        setpointSeries.isShowShapes = true
        val errorSeries = XYSeriesWithRenderer(errorKey)
        errorSeries.isShowShapes = true
        addSeries(positionSeries)
        addSeries(velocitySeries)
        addSeries(accelerationSeries)
        addSeries(voltageSeries)
        addSeries(setpointSeries)
        addSeries(errorSeries)
    }
}