package com.github.mittyrobotics.core.math.units

import kotlin.jvm.JvmInline
import kotlin.math.PI


public sealed interface MeasurementUnit {

    public val _value: Double

}

@Suppress("MemberVisibilityCanBePrivate")
@JvmInline
public value class Distance private constructor(public override val _value: Double):MeasurementUnit{
    public val meters: Double
        get() = _value
    public val inches: Double
        get() = _value / 254e-4

    public companion object {
        public fun fromMeters(meters: Double): Distance = Distance(meters)
        public fun fromInches(inches: Double): Distance = Distance(inches * 254e-4)
    }
}

@Suppress("MemberVisibilityCanBePrivate")
@JvmInline
public value class Velocity private constructor(public override val _value: Double):MeasurementUnit {
    public val meters: Double
        get() = _value
    public val inches: Double
        get() = _value / 254e-4

    public companion object {
        public fun fromMeters(meters: Double): Velocity = Velocity(meters)
        public fun fromInches(inches: Double): Velocity = Velocity(inches * 254e-4)
    }
}

@Suppress("MemberVisibilityCanBePrivate")
@JvmInline
public value class Acceleration private constructor(public override val _value: Double):MeasurementUnit {
    public val meters: Double
        get() = _value
    public val inches: Double
        get() = _value / 254e-4

    public companion object {
        public fun fromMeters(meters: Double): Acceleration = Acceleration(meters)
        public fun fromInches(inches: Double): Acceleration = Acceleration(inches * 254e-4)
    }
}

@Suppress("MemberVisibilityCanBePrivate")
@JvmInline
public value class Angle private constructor(public override val _value: Double):MeasurementUnit{
    public val degrees: Double
        get() = _value * (180 / PI)

    public companion object {
        public fun fromRadians(radians: Double): Angle = Angle(radians)
        public fun fromDegrees(degrees: Double): Angle = Angle(degrees * (PI / 180))
    }
}

@Suppress("MemberVisibilityCanBePrivate")
@JvmInline
public value class AngularVelocity private constructor(public override val _value: Double):MeasurementUnit{
    public val degrees: Double
        get() = _value * (180 / PI)

    public companion object {
        public fun fromRadians(radians: Double): AngularVelocity = AngularVelocity(radians)
        public fun fromDegrees(degrees: Double): AngularVelocity = AngularVelocity(degrees * (PI / 180))
    }
}

@Suppress("MemberVisibilityCanBePrivate")
@JvmInline
public value class AngularAcceleration private constructor(public override val _value: Double):MeasurementUnit{
    public val degrees: Double
        get() = _value * (180 / PI)

    public companion object {
        public fun fromRadians(radians: Double): AngularAcceleration = AngularAcceleration(radians)
        public fun fromDegrees(degrees: Double): AngularAcceleration = AngularAcceleration(degrees * (PI / 180))
    }
}

@Suppress("MemberVisibilityCanBePrivate")
@JvmInline
public value class Mass private constructor(public override val _value: Double):MeasurementUnit{
    public val pounds: Double
        get() = _value * 2.205

    public companion object {
        public fun fromKilograms(kilograms: Double): Mass = Mass(kilograms)
        public fun fromPounds(pounds: Double): Mass = Mass(pounds / 2.205)
    }
}
