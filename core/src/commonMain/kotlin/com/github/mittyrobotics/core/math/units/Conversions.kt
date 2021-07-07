package com.github.mittyrobotics.core.math.units

import kotlin.math.PI

public fun Double.inches():Double = this * 254e-4
public fun Double.pounds():Double = this / 2.205
public fun Double.degrees():Double = this*PI/180.0