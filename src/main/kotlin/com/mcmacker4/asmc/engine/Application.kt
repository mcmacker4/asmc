package com.mcmacker4.asmc.engine

import com.mcmacker4.asmc.engine.gl.VAO
import com.mcmacker4.asmc.engine.gl.VBO
import com.mcmacker4.asmc.util.Log
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL11.*


abstract class Application {
    
    fun start() {
        
        GLFWErrorCallback.createPrint(System.err).set()
        
        if (!glfwInit())
            throw RuntimeException("Could not initialize GLFW")

        val scale = 70
        Window.open(16 * scale, 9 * scale, "ASMC")
        
        glClearColor(0.3f, 0.6f, 0.9f, 1.0f)

        Log.log(glGetString(GL_VENDOR))
        Log.log(glGetString(GL_RENDERER))
        
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_CULL_FACE)

        onInit()

        var current = System.currentTimeMillis()
        var last = current
        
        while (!Window.willClose()) {
            current = System.currentTimeMillis()
            val delta = (current - last) / 1000f
            last = current
            
            glfwPollEvents()
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            onUpdate(delta)

            Window.update()
        }

        onEnd()
        
        VAO.cleanup()
        VBO.cleanup()
        
        Window.destroy()
        
    }

    abstract fun onInit()
    abstract fun onUpdate(delta: Float)
    abstract fun onEnd()

    fun stop() {
        Window.close()
    }
    
}