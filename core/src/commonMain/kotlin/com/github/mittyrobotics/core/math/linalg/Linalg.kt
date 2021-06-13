package com.github.mittyrobotics.core.math.linalg

import space.kscience.kmath.linear.RealMatrixContext.dot
import space.kscience.kmath.misc.UnstableKMathAPI
import space.kscience.kmath.real.*
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.pow

/**
 * Kotlin implementation of jblas expm function
 *
 * https://github.com/jblas-project/jblas/blob/main/src/main/java/org/jblas/MatrixFunctions.java#L406
 */
@UnstableKMathAPI
public fun RealMatrix.expm(): RealMatrix {
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

    val n = this.rowNum
    val j = max(0, 1 + floor(ln(max()!!) / ln(2.0)).toInt())
    val As = this / 2.0.pow(j)
    val As2 = As.dot(As)
    val As4 = As2.dot(As2)
    val As6 = As4.dot(As2)


    val I = identity(n)
    val U = c0 * I + c2 * As2 + c4 * As4 + (c6 * I + c8 * As2 + c10 * As4 + c12 * As6) * As6
    val V = c1 * I + c3 * As2 + c5 * As4 + (c7 * I + c9 * As2 + c11 * As4 + c13 * As6) * As6


    val AV = As .dot(V)
    var N = U + AV
    var D = U - AV

    D *= 100.0
    N *= 100.0

    var F = LUDecomposition(D).solve(N)

    for(i in 0 until j){
        F = F.dot(F)
    }

    return F
}

public fun identity(n: Int): RealMatrix{
    val mat = Array(n){DoubleArray(n)}
    for(i in 0 until n){
        mat[i][i] = 1.0
    }
    return mat.toMatrix()
}

public fun zeros(rows: Int, cols: Int): RealMatrix{
    val mat = Array(rows){DoubleArray(cols)}
    for(row in 0 until rows){
        for(col in 0 until cols){
            mat[row][col] = 0.0
        }
    }
    return mat.toMatrix()
}

public fun RealMatrix.toDoubleArray(): Array<DoubleArray>{
    val arr = Array(rowNum){DoubleArray(colNum)}
    for(row in 0 until rows.size){
        for(col in 0 until rows[row].size){
            arr[row][col] = rows[row][col]
        }
    }
    return arr
}

public fun RealMatrix.copy(): RealMatrix{
    val mat = Array(rowNum){DoubleArray(colNum)}
    for(row in 0 until rowNum){
        for(col in 0 until colNum){
            mat[row][col] = rows[row][col]
        }
    }
    return mat.toMatrix()
}

public fun RealMatrix.subMatrix(i0: Int, i1: Int, j0: Int, j1: Int): RealMatrix {
    val X = zeros(i1 - i0 + 1, j1 - j0 + 1)
    val B: Array<DoubleArray> = X.toDoubleArray()
    try {
        for (i in i0..i1) {
            for (j in j0..j1) {
                B[i - i0][j - j0] = toDoubleArray()[i][j]
            }
        }
    } catch (e: Exception) {
    }
    return B.toMatrix()
}

public fun RealMatrix.subMatrix(r: IntArray, c: IntArray): RealMatrix {
    val X = zeros(r.size, c.size)
    val B: Array<DoubleArray> = X.toDoubleArray()
    try {
        for (i in r.indices) {
            for (j in c.indices) {
                B[i][j] = toDoubleArray()[r[i]][c[j]]
            }
        }
    } catch (e: Exception) {
    }
    return B.toMatrix()
}

public fun RealMatrix.subMatrix(i0: Int, i1: Int, c: IntArray): RealMatrix {
    val X = zeros(i1 - i0 + 1, c.size)
    val B: Array<DoubleArray> = X.toDoubleArray()
    try {
        for (i in i0..i1) {
            for (j in c.indices) {
                B[i - i0][j] = this.toDoubleArray()[i][c[j]]
            }
        }
    } catch (e: Exception) {
    }
    return B.toMatrix()
}

public fun RealMatrix.subMatrix(r: IntArray, j0: Int, j1: Int): RealMatrix {
    val X = zeros(r.size, j1 - j0 + 1)
    val B: Array<DoubleArray> = X.toDoubleArray()
    try {
        for (i in r.indices) {
            for (j in j0..j1) {
                B[i][j - j0] = this.toDoubleArray()[r[i]][j]
            }
        }
    } catch (e: Exception) {
    }
    return B.toMatrix()
}
