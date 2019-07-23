package com.mcmacker4.asmc.engine.extensions

import org.joml.Matrix4f
import org.joml.Vector3f


operator fun Vector3f.unaryMinus(): Vector3f = mul(-1f, Vector3f())

operator fun Vector3f.plus(other: Vector3f): Vector3f = add(other, Vector3f())

operator fun Vector3f.plusAssign(other: Vector3f) { add(other) }

operator fun Vector3f.minus(other: Vector3f): Vector3f = sub(other, Vector3f())

operator fun Vector3f.minusAssign(other: Vector3f) { sub(other) }

operator fun Vector3f.times(other: Float): Vector3f = mul(other, Vector3f())

operator fun Vector3f.timesAssign(other: Float) { mul(other) }

operator fun Matrix4f.times(other: Matrix4f): Matrix4f = mul(other, Matrix4f())


fun Float.clamp(min: Float, max: Float) = when {
    this > max -> max
    this < min -> min
    else -> this
}