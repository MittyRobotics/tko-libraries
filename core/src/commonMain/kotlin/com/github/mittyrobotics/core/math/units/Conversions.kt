package com.github.mittyrobotics.core.math.units

import com.github.mittyrobotics.core.math.geometry.Rotation
import com.github.mittyrobotics.core.math.geometry.Vector2D
import kotlin.math.PI

/**
 * Unit Conversions
 *
 * Throughout this library, units are standardized to SI units. In order to input a different unit of measurement, use
 * the functions to easily convert that unit into the SI unit. Type in a number, then use the extension function for
 * whatever unit that number is in to convert it to SI units.
 *
 * For example:
 * `5.0.inches()` will convert 5.0 inches into meters. `45.0.degrees` will convert 45.0 degrees to radians.
 */

public fun Double.inches():Double = this * 254e-4
public fun Double.pounds():Double = this / 2.205
public fun Double.degrees():Double = this*PI/180.0
public fun Vector2D.inches():Vector2D = Vector2D(this.x.inches(), this.y.inches())
public fun Rotation.degrees(): Rotation = Rotation(this.radians.degrees())