package com.mcmacker4.asmc

import com.mcmacker4.asmc.engine.Application
import com.mcmacker4.asmc.engine.Window
import com.mcmacker4.asmc.engine.render.Renderer
import com.mcmacker4.asmc.input.Input
import com.mcmacker4.asmc.world.World
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*


class ASMC : Application() {
    
    private lateinit var world: World
    
    override fun onInit() {
        
        Input.grabCursor()

        var isWireframe = false
        var isCulling = true
        
        world = World()

        Input.onKeyDown {
            when (key) {
                GLFW_KEY_F11 -> {
                    Window.toggleFullscreen()
                    consume()
                }
                GLFW_KEY_ESCAPE -> {
                    stop()
                    consume()
                }
                GLFW_KEY_L -> {
                    isWireframe = !isWireframe
                    if (isWireframe)
                        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
                    else
                        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
                }
                GLFW_KEY_C -> {
                    isCulling = !isCulling
                    if (isCulling) glEnable(GL_CULL_FACE)
                    else glDisable(GL_CULL_FACE)
                }
            }
        }
        
    }

    override fun onUpdate(delta: Float) {
        world.update(delta)
        Renderer.render(world)
    }

    override fun onEnd() {}

}


fun main() {
    ASMC().start()
}