package com.mcmacker4.asmc.engine.model

import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer


abstract class Entity(
    val position: Vector3f = Vector3f(),
    val rotation: Vector3f = Vector3f(),
    val scale: Vector3f = Vector3f(1f)
) {
    
    private val modelMatrixBuffer = MemoryUtil.memAllocFloat(4*4)
    
    fun getModelMatrix() : FloatBuffer {
        return Matrix4f().apply {
            identity()
            translate(position)
            rotateXYZ(rotation)
            scale(scale)
        }.get(modelMatrixBuffer)
    }
    
}