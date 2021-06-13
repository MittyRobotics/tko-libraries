package com.github.mittyrobotics.core.math.linalg

import space.kscience.kmath.real.RealMatrix
import space.kscience.kmath.real.toMatrix
import kotlin.math.abs
import kotlin.math.min


/**
 * Kotlin implementation of LU Decomposition from JAMA library
 * https://github.com/cstroe/jama/blob/master/src/main/java/Jama/LUDecomposition.java
 */
public class LUDecomposition(A: RealMatrix) {
    /** Array for internal storage of decomposition.
     * @serial internal array storage.
     */
    private val LU: Array<DoubleArray> = A.toDoubleArray()

    /** Row and column dimensions, and pivot sign.
     * @serial column dimension.
     * @serial row dimension.
     * @serial pivot sign.
     */
    private val m: Int = A.rowNum
    private val n: Int = A.colNum
    private var pivsign: Int

    /** Internal storage of pivot vector.
     * @serial pivot vector.
     */
    private val piv: IntArray = IntArray(m)

    /** Is the matrix nonsingular?
     * @return     true if U, and hence A, is nonsingular.
     */
    public val isNonsingular: Boolean
        get() {
            for (j in 0 until n) {
                if (LU[j][j] == 0.0) return false
            }
            return true
        }

    /** Return lower triangular factor
     * @return     L
     */
    public val l: RealMatrix
        get() {
            val X = zeros(m, n)
            val L: Array<DoubleArray> = X.toDoubleArray()
            for (i in 0 until m) {
                for (j in 0 until n) {
                    if (i > j) {
                        L[i][j] = LU[i][j]
                    } else if (i == j) {
                        L[i][j] = 1.0
                    } else {
                        L[i][j] = 0.0
                    }
                }
            }
            return X
        }

    /** Return upper triangular factor
     * @return     U
     */
    public val u: RealMatrix
        get() {
            val X = zeros(n, n)
            val U: Array<DoubleArray> = X.toDoubleArray()
            for (i in 0 until n) {
                for (j in 0 until n) {
                    if (i <= j) {
                        U[i][j] = LU[i][j]
                    } else {
                        U[i][j] = 0.0
                    }
                }
            }
            return X
        }

    /** Return pivot permutation vector
     * @return     piv
     */
    public val pivot: IntArray
        get() {
            val p = IntArray(m)
            for (i in 0 until m) {
                p[i] = piv[i]
            }
            return p
        }

    /** Return pivot permutation vector as a one-dimensional double array
     * @return     (double) piv
     */
    public val doublePivot: DoubleArray
        get() {
            val vals = DoubleArray(m)
            for (i in 0 until m) {
                vals[i] = piv[i].toDouble()
            }
            return vals
        }

    /** Determinant
     * @return     det(A)
     * @exception  IllegalArgumentException  Matrix must be square
     */
    public fun det(): Double {
        require(m == n) { "Matrix must be square." }
        var d = pivsign.toDouble()
        for (j in 0 until n) {
            d *= LU[j][j]
        }
        return d
    }

    /** Solve A*X = B
     * @param  B   A Matrix with as many rows as A and any number of columns.
     * @return     X so that L*U*X = B(piv,:)
     * @exception  IllegalArgumentException Matrix row dimensions must agree.
     * @exception  RuntimeException  Matrix is singular.
     */
    public fun solve(B: RealMatrix): RealMatrix {
        require(!(B.rowNum !== m)) { "Matrix row dimensions must agree." }
        if (!isNonsingular) {
            throw RuntimeException("Matrix is singular.")
        }

        // Copy right hand side with pivoting
        val nx: Int = B.colNum
        val Xmat: RealMatrix = B.subMatrix(piv, 0, nx - 1)
        val X: Array<DoubleArray> = Xmat.toDoubleArray()

        // Solve L*Y = B(piv,:)
        for (k in 0 until n) {
            for (i in k + 1 until n) {
                for (j in 0 until nx) {
                    X[i][j] -= X[k][j] * LU[i][k]
                }
            }
        }
        // Solve U*X = Y;
        for (k in n - 1 downTo 0) {
            for (j in 0 until nx) {
                X[k][j] /= LU[k][k]
            }
            for (i in 0 until k) {
                for (j in 0 until nx) {
                    X[i][j] -= X[k][j] * LU[i][k]
                }
            }
        }

        return X.toMatrix()
    }


    /** LU Decomposition
     * Structure to access L, U and piv.
     * @param  A Rectangular matrix
     */
    init {

        // Use a "left-looking", dot-product, Crout/Doolittle algorithm.
        for (i in 0 until m) {
            piv[i] = i
        }
        pivsign = 1
        var LUrowi: DoubleArray
        val LUcolj = DoubleArray(m)

        // Outer loop.
        for (j in 0 until n) {

            // Make a copy of the j-th column to localize references.
            for (i in 0 until m) {
                LUcolj[i] = LU[i][j]
            }

            // Apply previous transformations.
            for (i in 0 until m) {
                LUrowi = LU[i]

                // Most of the time is spent in the following dot product.
                val kmax: Int = min(i, j)
                var s = 0.0
                for (k in 0 until kmax) {
                    s += LUrowi[k] * LUcolj[k]
                }
                LUcolj[i] -= s
                LUrowi[j] = LUcolj[i]
            }

            // Find pivot and exchange if necessary.
            var p = j
            for (i in j + 1 until m) {
                if (abs(LUcolj[i]) > abs(LUcolj[p])) {
                    p = i
                }
            }
            if (p != j) {
                for (k in 0 until n) {
                    val t = LU[p][k]
                    LU[p][k] = LU[j][k]
                    LU[j][k] = t
                }
                val k = piv[p]
                piv[p] = piv[j]
                piv[j] = k
                pivsign = -pivsign
            }

            // Compute multipliers.
            if ((j < m) and (LU[j][j].toInt() != 0)) {
                for (i in j + 1 until m) {
                    LU[i][j] /= LU[j][j]
                }
            }
        }
    }
}