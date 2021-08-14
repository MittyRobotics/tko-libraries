package com.github.mittyrobotics.core.math.geometry

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sign

/**
 * Represents a 2d line on a standard cartesian coordinate plane going infinitely in both directions.
 */
public class Line {
    private val firstPoint: Vector2D
    private val secondPoint: Vector2D
    public val slope: Double
    public val yIntercept: Double

    /**
     * Constructs a [Line] given a slope and a y intercept
     *
     * @param slope      the slope
     * @param yIntercept the y intercept y value
     */
    public constructor(slope: Double, yIntercept: Double) {
        this.slope = slope
        this.yIntercept = yIntercept
        firstPoint = Vector2D(0.0, yIntercept)
        secondPoint = Vector2D(1.0, yIntercept + slope)
    }

    /**
     * Constructs a [Line] given a [Transform]
     *
     * @param transform the [Transform]
     */
    public constructor(transform: Transform) : this(transform.vector, transform.vector + Vector2D(transform.rotation.cos(), transform.rotation.sin()))

    /**
     * Constructs a [Line] given two [Position] points
     *
     * @param firstPoint  the first [Position]
     * @param secondPoint the second [Position]
     */
    public constructor(firstPoint: Vector2D, secondPoint: Vector2D) {
        this.firstPoint = firstPoint
        this.secondPoint = secondPoint
        var slope = (firstPoint.y - secondPoint.y) / (firstPoint.x - secondPoint.x)
        if (slope.isInfinite()) {
            slope = 2e16
        }
        this.slope=slope
        yIntercept = firstPoint.y - slope * firstPoint.x
    }

    /**
     * Finds the intersection [Position] between this [Line] and `other`.
     *
     *
     * If the two lines are parallel, it will return null.
     *
     * @return the intersection [Position] between the two [Line]s.
     */
    public fun getIntersection(other: Line): Vector2D? {
        val m1 = slope
        val m2 = other.slope
        val b1 = yIntercept
        val b2 = other.yIntercept

        //If the lines are parallel, return null
        if (m1 == m2) {
            return null
        }
        val x = (b2 - b1) / (m1 - m2)
        val y = m1 * x + b1
        return Vector2D(x, y)
    }

    /**
     * Finds the closest point on this [Line] to the `referencePosition`.
     *
     *
     * This is done by finding the line perpendicular to this line that intersects with the `referenceTransform`.
     * https://www.desmos.com/calculator/trqlffx7ha
     *
     * @param referencePosition the [Position] to find the closest point to.
     * @return the closest [Position] to the `referencePosition`.
     */
    public fun getClosestPoint(referencePosition: Vector2D): Vector2D {
        val m = slope

        //Get perpendicular slope
        val m1 = -1 / m

        //Create perpendicular line from position
        val parallelLine = getPerpendicularLine(referencePosition)

        //Return the intersection between the two lines
        return getIntersection(parallelLine)!!
    }

    /**
     * Returns the angle of this line in the form of a [Rotation].
     *
     * @return the [Rotation] of this line.
     */
    public val lineRotation: Rotation
        get() {
            var rads: Double = atan2(slope, 1.0)
            if (rads.isNaN()) {
                rads = 0.0
            }
            return Rotation(rads)
        }

    /**
     * Returns the perpendicular [Line] to this [Line] that passes through the `referencePosition`.
     *
     * @param referencePosition the [Position] that the perpendicular [Line] passes through.
     * @return the perpendicular [Line] to this [Line].
     */
    public fun getPerpendicularLine(referencePosition: Vector2D): Line {
        //Get perpendicular slope
        val m1 = -1 / slope
        return if (m1.isInfinite()) {
            Line(referencePosition, referencePosition + Vector2D(0.0, 1.0))
        } else {
            Line(referencePosition, Vector2D(1.0, m1) +  referencePosition)
        }
    }

    /**
     * Finds which side of a line the point is on
     *
     * @param point the [Position] to find which side of the [Line] it is on
     * @return a -1 for right side, +1 for left side
     */
    public fun findSide(point: Vector2D): Double {
        val x: Double = point.x
        val y: Double = point.y
        val x1: Double = firstPoint.x
        val y1: Double = firstPoint.y
        val x2: Double = secondPoint.x
        val y2: Double = secondPoint.y
        return -sign((x - x1) * (y2 - y1) - (y - y1) * (x2 - x1))
    }

    /**
     * Determines whether or not `point` is collinear with this [Line] given a `tolerance`.
     *
     * @param point     the [Position] to determine whether or not it is collinear.
     * @param tolerance the tolerance for how much the point can be off the [Line] to be classified as collinear.
     * @return whether or not the `point` is collinear with this [Line].
     */
    public fun isCollinear(point: Vector2D, tolerance: Double = 0.001): Boolean {
        val x1: Double = firstPoint.x
        val y1: Double = firstPoint.y
        val x2: Double = point.x
        val y2: Double = point.y
        val x3: Double = secondPoint.x
        val y3: Double = secondPoint.y
        val collinear = (y2 - y1) * (x3 - x2) - (y3 - y2) * (x2 - x1)
        return abs(collinear) < tolerance
    }
}