package com.github.mittyrobotics.ui.graph

import com.github.mittyrobotics.core.math.geometry.Vector2D
import java.awt.Color

public data class GraphData(public val data: Array<Vector2D>, public val name: String, public val lines: Boolean = true, public val points: Boolean = true, public val color: Color? = null)