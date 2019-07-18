package com.mcmacker4.asmc

import com.mcmacker4.asmc.engine.Application
import com.mcmacker4.asmc.engine.Window
import com.mcmacker4.asmc.engine.gl.Texture
import com.mcmacker4.asmc.engine.scene.ModelEntity
import com.mcmacker4.asmc.engine.scene.RawModel
import com.mcmacker4.asmc.engine.render.Renderer
import com.mcmacker4.asmc.engine.scene.Scene
import com.mcmacker4.asmc.engine.view.Camera
import com.mcmacker4.asmc.input.Input
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*


class ASMC : Application() {
    
    lateinit var scene: Scene

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

        val model = RawModel.create(
            floatArrayOf(
                //North (z-1)
                1f, 1f, 0f,
                1f, 0f, 0f,
                0f, 0f, 0f,
                1f, 1f, 0f,
                0f, 0f, 0f,
                0f, 1f, 0f,
                //South (z+1)
                0f, 1f, 1f,
                0f, 0f, 1f,
                1f, 0f, 1f,
                0f, 1f, 1f,
                1f, 0f, 1f,
                1f, 1f, 1f,
                //East (x+1)
                1f, 1f, 1f,
                1f, 0f, 1f,
                1f, 0f, 0f,
                1f, 1f, 1f,
                1f, 0f, 0f,
                1f, 1f, 0f,
                //West (x-1)
                0f, 1f, 0f,
                0f, 0f, 0f,
                0f, 0f, 1f,
                0f, 1f, 0f,
                0f, 0f, 1f,
                0f, 1f, 1f,
                //Up (y+1)
                0f, 1f, 0f,
                0f, 1f, 1f,
                1f, 1f, 1f,
                0f, 1f, 0f,
                1f, 1f, 1f,
                1f, 1f, 0f,
                //Down (y-1)
                0f, 0f, 1f,
                0f, 0f, 0f,
                1f, 0f, 0f,
                0f, 0f, 1f,
                1f, 0f, 0f,
                1f, 0f, 1f
            ).map { it - 0.5f }.toFloatArray(),
            floatArrayOf(
                //North (z-1)
                0f, 0f,
                0f, 1f,
                1f, 1f,
                0f, 0f,
                1f, 1f,
                1f, 0f,
                //South (z+1)
                0f, 0f,
                0f, 1f,
                1f, 1f,
                0f, 0f,
                1f, 1f,
                1f, 0f,
                //East (x+1)
                0f, 0f,
                0f, 1f,
                1f, 1f,
                0f, 0f,
                1f, 1f,
                1f, 0f,
                //West (x-1)
                0f, 0f,
                0f, 1f,
                1f, 1f,
                0f, 0f,
                1f, 1f,
                1f, 0f,
                //Up (y+1)
                0f, 0f,
                0f, 1f,
                1f, 1f,
                0f, 0f,
                1f, 1f,
                1f, 0f,
                //Down (y-1)
                0f, 0f,
                0f, 1f,
                1f, 1f,
                0f, 0f,
                1f, 1f,
                1f, 0f
            ).map { it / 16 }.toFloatArray(),
            Texture.load("terrain.png")
        )

        val entity = ModelEntity(model).apply {
            onUpdate = { delta ->
                rotation.y += Math.PI.toFloat() * delta
                //rotation.z += Math.PI.toFloat() * delta
            }
        }
        
        val camera = Camera().apply {
            position.set(0f, 0f, -1.5f)
            rotation.y = Math.PI.toFloat()
        }
        
        scene = Scene(camera).apply {
            modelEntities.add(entity)
        }

    }

    override fun onUpdate(delta: Float) {
        scene.update(delta)
        Renderer.render(scene)
    }

    override fun onEnd() {}

}


fun main() {
    ASMC().start()
}