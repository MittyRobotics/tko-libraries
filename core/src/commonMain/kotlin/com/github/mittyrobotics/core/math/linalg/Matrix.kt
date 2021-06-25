package com.github.mittyrobotics.core.math.linalg

public open class Matrix(data: Array<DoubleArray>) {
    public var data: Array<DoubleArray> = data
        private set
    public val rows: Int = data.size
    public val cols: Int = if (rows != 0) {
        data[0].size
    } else {
        0
    }

    public fun get2DData(rowMajor: Boolean = true): DoubleArray {
        val array = DoubleArray(rows * cols)
        if (rowMajor) {
            for (col in 0 until cols) {
                for (row in 0 until rows) {
                    array[col + cols * row] = data[row][col]
                }
            }
        } else {
            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    array[row + rows * col] = data[row][col]
                }
            }
        }
        return array
    }

    public operator fun get(index: Int): DoubleArray = data[index]

    public operator fun set(index: Int, value: DoubleArray) {
        data[index] = value
    }

    public operator fun plus(other: Matrix): Matrix = add(this, other)

    public operator fun plus(value: Double): Matrix = add(this, value)

    public operator fun minus(other: Matrix): Matrix = subtract(this, other)

    public operator fun minus(value: Double): Matrix = subtract(this, value)

    public operator fun times(other: Matrix): Matrix = multiply(this, other)

    public operator fun times(value: Double): Matrix = times(this, value)

    public operator fun div(value: Double): Matrix = times(this, 1.0 / value)

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

    public operator fun divAssign(value: Double) {
        data = div(value).data
    }

    public fun max(): Double = max(this)

    public fun expm(): Matrix = expm(this)

    public fun transpose(): Matrix = transpose(this)

    public fun subMatrix(startRow: Int = 0, startCol: Int = 0, endRow: Int = rows, endCol: Int = cols): Matrix =
        subMatrix(this, startRow, startCol, endRow, endCol)

    public companion object {
        public fun fromRowMajor(rows: Int, cols: Int, data: DoubleArray): Matrix {
            val array = Array(rows) { DoubleArray(cols) }
            for (col in 0 until cols) {
                for (row in 0 until rows) {
                    array[row][col] = data[col + cols * row]
                }
            }
            return Matrix(array)
        }

        public fun fromColMajor(rows: Int, cols: Int, data: DoubleArray): Matrix {
            val array = Array(rows) { DoubleArray(cols) }
            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    array[row][col] = data[row + rows * col]
                }
            }
            return Matrix(array)
        }

        public fun zeros(rows: Int, cols: Int): Matrix = fill(rows, cols, 0.0)
        public fun fill(rows: Int, cols: Int, value: Double): Matrix =
            Matrix(Array(rows) { DoubleArray(cols) { value } })

        public fun identity(n: Int): Matrix = Matrix(Array(n) { row ->
            DoubleArray(n) { col ->
                if (row == col) {
                    1.0
                } else {
                    0.0
                }
            }
        })

        public fun row(data: DoubleArray): Matrix = Matrix(Array(1) { data })
        public fun column(data: DoubleArray): Matrix = Matrix(Array(data.size) { doubleArrayOf(data[it]) })
    }

    public override fun toString(): String = "Matrix(rows: $rows cols: $cols ${formatMatrixInString(data)})"
}
