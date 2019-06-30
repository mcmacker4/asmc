package com.mcmacker4.asmc

import com.mcmacker4.asmc.engine.Engine
import com.mcmacker4.asmc.engine.Window
import com.mcmacker4.asmc.event.KeyEvent
import org.lwjgl.glfw.GLFW


fun main() {
    
    Engine.start {
        
        Window.addListener {
            if (it is KeyEvent && it.key == GLFW.GLFW_KEY_F && it.action == GLFW.GLFW_PRESS) {
                Window.setFullscreen(!Window.fullscreen)
            }
        }

    }
    
}