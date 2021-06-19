package com.github.mittyrobotics.motion.models

import com.github.mittyrobotics.core.math.linalg.Matrix

public data class LinearSystem(public val A: Matrix, public val B: Matrix, public val C: Matrix, public val D: Matrix)