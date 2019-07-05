package com.mcmacker4.asmc.engine.extensions

import org.joml.Vector3f


operator fun Vector3f.unaryMinus(): Vector3f? = mul(-1f, Vector3f())