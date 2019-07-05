package com.mcmacker4.asmc.engine.view

import com.mcmacker4.asmc.engine.Window
import com.mcmacker4.asmc.engine.extensions.unaryMinus
import com.mcmacker4.asmc.engine.model.Entity
import org.joml.Matrix4f
import org.lwjgl.system.MemoryUtil
import java.lang.Math.toRadians
import java.nio.FloatBuffer


class Camera(
    var fov: Float = 80f,
    var nearPlane: Float = 0.1f,
    var farPlane: Float = 1000f
) : Entity() {
    
    private val viewMatrixBuffer = MemoryUtil.memAllocFloat(4*4)
    private val projMatrixBuffer = MemoryUtil.memAllocFloat(4*4)
    
    fun getViewMatrix() : FloatBuffer {
        return Matrix4f().apply {
            identity()
            rotateXYZ(-rotation)
            translate(-position)
        }.get(viewMatrixBuffer)
    }
    
    fun getProjectionMatrix() : FloatBuffer {
        return Matrix4f()
            .perspective(toRadians(fov.toDouble()).toFloat(), Window.aspect(), nearPlane, farPlane)
            .get(projMatrixBuffer)
    }
    
}