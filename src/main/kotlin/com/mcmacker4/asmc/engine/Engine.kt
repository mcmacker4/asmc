package com.mcmacker4.asmc.engine

import com.mcmacker4.asmc.event.KeyEvent
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback


object Engine {
    
    fun start() {
        
        GLFWErrorCallback.createPrint(System.err).set()
        
        if (!glfwInit())
            throw RuntimeException("Could not initialize GLFW")
        
        Window.open()
        
        var times = 0

        Window.addListener {
            if (it is KeyEvent && it.key == GLFW_KEY_F && it.action == GLFW_PRESS) {
                Window.setFullscreen(!Window.fullscreen)
            }
        }
        
        while (!Window.willClose()) {
            glfwPollEvents()
            
            Window.update()
        }
        
        Window.close()
        
    }
    
}