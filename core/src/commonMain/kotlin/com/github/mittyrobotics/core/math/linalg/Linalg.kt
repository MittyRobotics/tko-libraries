package com.github.mittyrobotics.core.math.linalg

public expect fun solve(A: Matrix, B: Matrix): Matrix

public fun dot(mat: Matrix, mat1: Matrix): Matrix {
    require(mat.cols == mat1.rows) { "Dimensions do not match for dot product: ($mat.rows, $mat.cols) x (${mat1.rows}, ${mat1.cols})" }
    val product = Array(mat.rows) { DoubleArray(mat1.cols) }
    for (i in 0 until mat.rows) {
        for (j in 0 until mat1.cols) {
            for (k in 0 until mat.cols) {
                product[i][j] += mat[i][k] * mat1[k][j]
            }
        }
    }
    return Matrix(product)
}

public fun times(mat: Matrix, value: Double): Matrix = Matrix(Array(mat.rows) { row -> DoubleArray(mat.cols) { col -> mat[row][col] * value } })

public fun add(mat: Matrix, value: Double): Matrix = Matrix(Array(mat.rows) { row -> DoubleArray(mat.cols) { col -> mat[row][col] + value } })

public fun subtract(mat: Matrix, value: Double): Matrix = Matrix(Array(mat.rows) { row -> DoubleArray(mat.cols) { col -> mat[row][col] - value } })

public fun add(mat: Matrix, mat1: Matrix): Matrix {
    require((mat.rows == mat1.rows) and (mat.cols == mat1.cols)) { "Dimensions do not match for matrix addition: ($mat.rows, $mat.cols) x (${mat1.rows}, ${mat1.cols})" }
    return Matrix(Array(mat.rows) { row -> DoubleArray(mat.cols) { col -> mat[row][col] + mat1[row][col] } })
}

public fun subtract(mat: Matrix, mat1: Matrix): Matrix {
    require((mat.rows == mat1.rows) and (mat.cols == mat1.cols)) { "Dimensions do not match for matrix subtraction: ($mat.rows, $mat.cols) x (${mat1.rows}, ${mat1.cols})" }
    return Matrix(Array(mat.rows) { row -> DoubleArray(mat.cols) { col -> mat[row][col] - mat1[row][col] } })
}

public fun formatMatrixInString(array: Array<DoubleArray>): String{
    var string = ""
    for (row in 0 until array.size) {
        string += "\n{"
        for (col in 0 until array[row].size) {
            string += if (col == array[row].size - 1) {
                array[row][col]
            } else {
                "" + array[row][col] + ", "
            }
        }
        string += "}"
    }
    return string
}