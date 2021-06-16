package com.github.mittyrobotics.core.math.linalg

import org.ejml.simple.SimpleMatrix

public actual fun solve(A: Matrix, B: Matrix): Matrix {
    val ejmlA = SimpleMatrix(A.data)
    val ejmlB = SimpleMatrix(B.data)
    val x = ejmlA.solve(ejmlB)
    return Matrix.fromRowMajor(x.numRows(), x.numCols(), x.ddrm.data)
}