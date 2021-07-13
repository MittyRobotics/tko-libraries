package com.github.mittyrobotics.motion.models

import com.github.mittyrobotics.core.math.linalg.Matrix

public class SystemModel(
    public val A_: (Map<String, Matrix>) -> Matrix,
    public val B_: (Map<String, Matrix>) -> Matrix,
    public val C_: (Map<String, Matrix>) -> Matrix,
    public val D_: (Map<String, Matrix>) -> Matrix
) {
    public val A: Matrix
        get() = A_(emptyMap())
    public val B: Matrix
        get() = B_(emptyMap())
    public val C: Matrix
        get() = C_(emptyMap())
    public val D: Matrix
        get() = D_(emptyMap())

    public constructor(A: Matrix, B: Matrix, C: Matrix, D: Matrix) : this({ A }, { B }, { C }, { D })
}