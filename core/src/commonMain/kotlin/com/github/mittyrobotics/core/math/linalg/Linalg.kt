package com.github.mittyrobotics.core.math.linalg

import com.github.mittyrobotics.core.math.linalg.Matrix.Companion.identity
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.pow

public expect fun solve(A: Matrix, B: Matrix): Matrix

/**
 * Kotlin implementation of jblas expm function
 *
 * https://github.com/jblas-project/jblas/blob/main/src/main/java/org/jblas/MatrixFunctions.java#L406
 */
public fun expm(A: Matrix): Matrix {
    val c0 = 1.0
    val c1 = 0.5
    val c2 = 0.12
    val c3 = 0.01833333333333333
    val c4 = 0.0019927536231884053
    val c5 = 1.630434782608695E-4
    val c6 = 1.0351966873706E-5
    val c7 = 5.175983436853E-7
    val c8 = 2.0431513566525E-8
    val c9 = 6.306022705717593E-10
    val c10 = 1.4837700484041396E-11
    val c11 = 2.5291534915979653E-13
    val c12 = 2.8101705462199615E-15
    val c13 = 1.5440497506703084E-17

    val n = A.rows
    val j = max(0, 1 + floor(ln(A.max()) / ln(2.0)).toInt())
    val As = A / 2.0.pow(j)
    val As2 = As * As
    val As4 = As2 * As2
    val As6 = As4 * As2

    val I = identity(n)
    val U = c0 * I + c2 * As2 + c4 * As4 + (c6 * I + c8 * As2 + c10 * As4 + c12 * As6) * As6
    val V = c1 * I + c3 * As2 + c5 * As4 + (c7 * I + c9 * As2 + c11 * As4 + c13 * As6) * As6

    val AV = As * V
    val N = U + AV
    val D = U - AV

    var F = solve(D, N)

    for(i in 0 until j){
        F = F * F
    }

    return F
}

private operator fun Double.times(mat: Matrix): Matrix {
    return times(mat, this)
}

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

public fun max(A: Matrix): Double {
    var max = 0.0
    for(num in A.get2DData()){
        if(num > max){
            max = num
        }
    }
    return max
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