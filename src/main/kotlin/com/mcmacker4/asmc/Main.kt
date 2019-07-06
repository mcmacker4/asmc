package com.mcmacker4.asmc

import com.mcmacker4.asmc.engine.Application
import com.mcmacker4.asmc.engine.Window
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
            intArrayOf(
                6, 4, 0, 6, 0, 2, //North
                3, 1, 5, 3, 5, 7, //South
                7, 5, 4, 7, 4, 6, //East
                2, 0, 1, 2, 1, 3, //West
                2, 3, 7, 2, 7, 6, //Up
                1, 0, 4, 1, 4, 5  //Down
            ),
            floatArrayOf(
                0f, 0f, 0f,
                0f, 0f, 1f,
                0f, 1f, 0f,
                0f, 1f, 1f,
                1f, 0f, 0f,
                1f, 0f, 1f,
                1f, 1f, 0f,
                1f, 1f, 1f
            )
        )

        val entity = ModelEntity(model).apply {
            position.set(-0.5f, -0.5f, -0.5f)
        }
        
        val camera = Camera().apply {
            position.set(0f, 0f, -3f)
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