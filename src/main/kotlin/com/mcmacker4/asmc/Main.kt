package com.mcmacker4.asmc

import com.mcmacker4.asmc.block.Block
import com.mcmacker4.asmc.block.BlockTexture
import com.mcmacker4.asmc.block.BlockVertices
import com.mcmacker4.asmc.engine.Application
import com.mcmacker4.asmc.engine.Window
import com.mcmacker4.asmc.engine.gl.GLTexture
import com.mcmacker4.asmc.engine.scene.ModelEntity
import com.mcmacker4.asmc.engine.scene.RawModel
import com.mcmacker4.asmc.engine.render.Renderer
import com.mcmacker4.asmc.engine.scene.Scene
import com.mcmacker4.asmc.engine.view.Camera
import com.mcmacker4.asmc.input.Input
import com.mcmacker4.asmc.world.Chunk
import com.mcmacker4.asmc.world.World
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*


class ASMC : Application() {
    
    lateinit var camera: Camera
    lateinit var world: World
    override fun onInit() {
        
        Input.grabCursor()

        var isWireframe = false
        var isCulling = true
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
        
        world = World()
        
        camera = Camera().apply {
            position.set(0f, 0f, -1.5f)
            rotation.y = Math.PI.toFloat()
        }
        
    }

    override fun onUpdate(delta: Float) {
        camera.update(delta)
        world.update(delta)
        world.render()
    }

    override fun onEnd() {}

}


fun main() {
    ASMC().start()
}