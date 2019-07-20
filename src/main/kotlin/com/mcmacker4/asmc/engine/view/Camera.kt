package com.mcmacker4.asmc.engine.view

import com.mcmacker4.asmc.engine.Window
import com.mcmacker4.asmc.engine.extensions.plusAssign
import com.mcmacker4.asmc.engine.extensions.times
import com.mcmacker4.asmc.engine.extensions.unaryMinus
import com.mcmacker4.asmc.engine.scene.Entity
import com.mcmacker4.asmc.input.Input
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryUtil
import java.lang.Math.toRadians
import java.nio.FloatBuffer


class Camera(
    var fov: Float = 80f,
    var nearPlane: Float = 0.1f,
    var farPlane: Float = 1000f
) : Entity() {
    
    companion object {
        private const val speed = 2f
        private const val sensitivity = 0.003f
    }
    
    private val viewMatrixBuffer = MemoryUtil.memAllocFloat(4*4)
    private val projMatrixBuffer = MemoryUtil.memAllocFloat(4*4)
    
    init {
        Input.onMouseMoved {
            rotation.y -= dx.toFloat() * sensitivity
            rotation.x -= dy.toFloat() * sensitivity
        }
    }
    
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

    override fun update(delta: Float) {
        super.update(delta)
        var direction = Vector3f()
        if (Input.isKeyDown(GLFW_KEY_A) && !Input.isKeyDown(GLFW_KEY_D))
            direction.x = -1f
        else if (Input.isKeyDown(GLFW_KEY_D) && !Input.isKeyDown(GLFW_KEY_A))
            direction.x = 1f
        if (Input.isKeyDown(GLFW_KEY_W) && !Input.isKeyDown(GLFW_KEY_S))
            direction.z = -1f
        else if (Input.isKeyDown(GLFW_KEY_S) && !Input.isKeyDown(GLFW_KEY_W))
            direction.z = 1f
        if (Input.isKeyDown(GLFW_KEY_SPACE) && !Input.isKeyDown(GLFW_KEY_LEFT_CONTROL))
            direction.y = 1f
        else if (Input.isKeyDown(GLFW_KEY_LEFT_CONTROL) && !Input.isKeyDown(GLFW_KEY_SPACE))
            direction.y = -1f
        
        if (Input.isKeyDown(GLFW_KEY_LEFT_SHIFT))
            direction *= 4f
        
        position += direction.rotateY(rotation.y) * speed * delta
    }
    
}