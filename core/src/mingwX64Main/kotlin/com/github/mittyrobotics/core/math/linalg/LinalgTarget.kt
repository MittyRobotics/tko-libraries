package com.github.mittyrobotics.core.math.linalg

import kotlinx.cinterop.*
import libOpenBLAS.*


public actual fun solve(A: Matrix, B: Matrix): Matrix {
    memScoped {
        val ipiv = nativeHeap.allocArray<IntVar>(B.rows)

        val a =  nativeHeap.allocArray<DoubleVar>(A.rows * A.cols)
        val b = nativeHeap.allocArray<DoubleVar>(B.rows * B.cols)
        val aArray = A.get2DData()
        val bArray = B.get2DData()
        for (i in aArray.indices) {
            a[i] = aArray[i]
        }

        for (i in bArray.indices) {
            b[i] = bArray[i]
        }

        val info = LAPACKE_dgesv(LAPACK_ROW_MAJOR, A.rows, B.cols, a, A.rows, ipiv, b, B.rows)

        if (info < 0) {
            println("LAPACKE_dgesv failed with INFO $info. if INFO = -i, the i-th argument had an illegal value")
        }
        if (info > 0) {
            println("LAPACKE_dgesv failed with INFO $info. if INFO = i, U(i,i) is exactly zero. The factorization " +
                    "has been completed, but the factor U is exactly Nsingular, so the solution could not be computed.")
        }

        val x = b.getPointer(ArenaBase())

        val X = Matrix.fromRowMajor(A.rows, A.cols, DoubleArray(bArray.size) { i -> x[i] })

        return X
    }
}