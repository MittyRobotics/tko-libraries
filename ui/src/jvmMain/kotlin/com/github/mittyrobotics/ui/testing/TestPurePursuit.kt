package com.github.mittyrobotics.ui.testing

import com.github.mittyrobotics.core.math.geometry.Rotation
import com.github.mittyrobotics.core.math.geometry.Transform
import com.github.mittyrobotics.core.math.geometry.Vector2D
import com.github.mittyrobotics.core.math.kinematics.DifferentialDriveState
import com.github.mittyrobotics.core.math.linalg.Matrix
import com.github.mittyrobotics.core.math.spline.Path
import com.github.mittyrobotics.core.math.units.degrees
import com.github.mittyrobotics.core.math.units.inches
import com.github.mittyrobotics.core.math.units.pounds
import com.github.mittyrobotics.motion.controllers.PID
import com.github.mittyrobotics.motion.controllers.purePursuit
import com.github.mittyrobotics.motion.models.SystemResponse
import com.github.mittyrobotics.motion.models.drivetrain
import com.github.mittyrobotics.motion.models.motors.DCMotor
import com.github.mittyrobotics.motion.models.simNext
import com.github.mittyrobotics.motion.observers.DifferentialDriveOdometry
import com.github.mittyrobotics.motion.profiles.PathTrajectory
import com.github.mittyrobotics.ui.graph.Graph

public fun main() {
    //Pure pursuit controller constants
    val trackWidth = 20.0.inches()
    val lookaheadDistance = 10.0.inches()

    //Create drivetrain model
    val drivetrain = drivetrain(DCMotor.falcon500(2), 40.0.pounds(), 12.0, 1.268, 4.0.inches(), trackWidth)
    var x = Matrix.zeros(drivetrain.A.rows, 1)

    //Create path and trajectory
    val path = Path.quinticHermitePath(
        arrayOf(
            Transform(Vector2D(0.0, 0.0), Rotation(0.0)),
            Transform(Vector2D(10.0, 5.0), Rotation((-45.0).degrees())),
        )
    )
    val trajectory = PathTrajectory(path, 2.0, 5.0, 0.5)

    //Create drivetrain wheel controllers
    val feedForward = 12.0 / 5.69
    val PID = PID(1.0, 0.0, 0.0)

    val odometry = DifferentialDriveOdometry(trackWidth)

    val dt = 0.02

    //Create arrays to graph values
    val robotPositions = mutableListOf<Vector2D>()
    val xs = mutableListOf<Matrix>()
    val ys = mutableListOf<Matrix>()
    val us = mutableListOf<Matrix>()
    val ts = mutableListOf<Double>()

    var t = 0.0
    while (!trajectory.isFinished()) {
        t += dt
        //Calculate trajectory state
        val trajectoryState = trajectory.next(dt)

        //Get lookahead position
        val lookaheadPos = trajectory.getTransform(lookaheadDistance).vector


        //Calculate robot transform from odometry
        val robotPos =
            odometry.update(DifferentialDriveState.fromWheels(x.get2DData(3), x.get2DData(4), trackWidth), dt)

        //Calculate pure pursuit state
        val purePursuitState = purePursuit(robotPos, lookaheadPos, trajectoryState[0], trackWidth)

        //Calculate left and right wheel voltages
        val leftVoltage = PID.calculate(purePursuitState.left, x.get2DData(3), dt) + purePursuitState.left * feedForward
        val rightVoltage =
            PID.calculate(purePursuitState.right, x.get2DData(4), dt) + purePursuitState.right * feedForward
        val u = Matrix.column(doubleArrayOf(leftVoltage, rightVoltage))

        //Simulate drivetrain
        val simNext = simNext(drivetrain, x, u, dt)
        x = simNext.first

        //Populate arrays to graph
        xs.add(x)
        ys.add(simNext.second)
        us.add(u)
        ts.add(t)
        robotPositions.add(robotPos.vector.copy())
    }

    //Plot values
    Graph().plot(SystemResponse(xs.toTypedArray(), ys.toTypedArray(), us.toTypedArray(), ts.toTypedArray()), "System")
    Graph().also {
        it.plot(robotPositions.toTypedArray(), "Robot Position"); it.plotParametric(
        path,
        "Path"
    )
        it.scaleGraphToScale(0.02, 3.0, 0.0)
    }


}