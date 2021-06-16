package com.github.mittyrobotics.ui

import com.github.mittyrobotics.core.math.linalg.Matrix
import com.github.mittyrobotics.core.math.linalg.solve

public fun main(){
    val A = Matrix(arrayOf(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0)))
    val B = Matrix(arrayOf(doubleArrayOf(5.0, 6.0), doubleArrayOf(7.0, 8.0)))
    val x = solve(A, B)
    println(x)
}