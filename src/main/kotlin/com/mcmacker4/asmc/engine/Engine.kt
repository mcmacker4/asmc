package com.mcmacker4.asmc.engine

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*


object Engine {
    
    fun start(callback: () -> Unit) {
        
        GLFWErrorCallback.createPrint(System.err).set()
        
        if (!glfwInit())
            throw RuntimeException("Could not initialize GLFW")
        
        Window.open()
        
        GL.createCapabilities()
        
        callback.invoke()
        
        glClearColor(0.3f, 0.6f, 0.9f, 1.0f)
        
        while (!Window.willClose()) {
            glfwPollEvents()
            
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            
            Window.update()
        }
        
        Window.close()
        
    }
    
}