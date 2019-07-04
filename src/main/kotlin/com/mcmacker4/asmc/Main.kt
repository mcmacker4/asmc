package com.mcmacker4.asmc

import com.mcmacker4.asmc.engine.Engine
import com.mcmacker4.asmc.engine.Window
import com.mcmacker4.asmc.input.Input
import org.lwjgl.glfw.GLFW.*


fun main() {
    
    Engine.start {
        
        Input.addKeyboardListener {
            if (key == GLFW_KEY_F && action == GLFW_PRESS) {
                Window.toggleFullscreen()
                consume()
            }
        }
        
    }
    
}