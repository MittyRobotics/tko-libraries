package com.github.mittyrobotics.ui

import com.github.mittyrobotics.core.math.linalg.Matrix
import com.github.mittyrobotics.core.math.linalg.solve

public fun main(){
    val A = Matrix(arrayOf(doubleArrayOf(1.0, 1.0, 0.0), doubleArrayOf(0.0, 0.0, 2.0), doubleArrayOf(0.0, 0.0, -1.0)))
    println(A.expm())
}