package com.github.mittyrobotics.core.math.linalg

public class Matrix(data: Array<DoubleArray>) {
    public var data: Array<DoubleArray> = data
        private set
    public val rows: Int = data.size
    public val cols: Int = if (rows != 0) {
        data[0].size
    } else {
        0
    }

    public operator fun plus(other: Matrix): Matrix = add(this, other)

    public operator fun plus(value: Double): Matrix = add(this, value)

    public operator fun minus(other: Matrix): Matrix = subtract(this, other)

    public operator fun minus(value: Double): Matrix = subtract(this, value)

    public operator fun times(value: Matrix): Matrix = dot(this, value)

    public operator fun times(value: Double): Matrix = times(this, value)

    public operator fun plusAssign(other: Matrix) {
        data = plus(other).data
    }

    public operator fun plusAssign(value: Double) {
        data = plus(value).data
    }

    public operator fun minusAssign(other: Matrix) {
        data = minus(other).data
    }

    public operator fun minusAssign(value: Double) {
        data = minus(value).data
    }

    public operator fun timesAssign(other: Matrix) {
        data = times(other).data
    }

    public operator fun timesAssign(value: Double) {
        data = times(value).data
    }

    public companion object {
        public fun zeros(rows: Int, cols: Int): Matrix = Matrix(Array(rows) { DoubleArray(cols) })
        public fun identity(n: Int): Matrix = Matrix(Array(n) { row ->
            DoubleArray(n) { col ->
                if (row == col) {
                    1.0
                } else {
                    0.0
                }
            }
        })
    }

    public override fun toString(): String = "Matrix(rows: $rows cols: $cols ${formatMatrixInString(data)})"
}
