package com.mcmacker4.asmc.engine

import com.mcmacker4.asmc.engine.model.RawModel
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*


object Engine {
    
    fun start(callback: Engine.() -> Unit) {
        
        GLFWErrorCallback.createPrint(System.err).set()
        
        if (!glfwInit())
            throw RuntimeException("Could not initialize GLFW")
        
        Window.open(1280, 720, "ASMC")
        
        glClearColor(0.3f, 0.6f, 0.9f, 1.0f)
        
        callback.invoke(this)
        
        val model = RawModel.create(
            floatArrayOf(
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0f, 0.5f, 0f
            )
        )
        
        while (!Window.willClose()) {
            glfwPollEvents()
            
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            model.render()
            
            Window.update()
        }
        
        Window.close()
        
    }
    
}