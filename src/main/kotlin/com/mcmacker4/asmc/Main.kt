package com.mcmacker4.asmc

import com.mcmacker4.asmc.engine.Engine
import com.mcmacker4.asmc.engine.Window
import com.mcmacker4.asmc.input.Input
import org.lwjgl.glfw.GLFW.*


fun main() {
    
    Engine.start {
        
        Input.addKeyboardListener {
            if (action == GLFW_RELEASE) {
                when (key) {
                    GLFW_KEY_F11 -> {
                        Window.toggleFullscreen()
                        consume()
                    }
                    GLFW_KEY_ESCAPE -> {
                        stop()
                        consume()
                    }
                }
            }
        }
        
    }
    
}