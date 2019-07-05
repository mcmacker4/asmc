package com.mcmacker4.asmc

import com.mcmacker4.asmc.engine.Application
import com.mcmacker4.asmc.engine.Window
import com.mcmacker4.asmc.engine.model.Entity
import com.mcmacker4.asmc.engine.model.RawModel
import com.mcmacker4.asmc.engine.render.Renderer
import com.mcmacker4.asmc.input.Input
import org.lwjgl.glfw.GLFW.*


class ASMC : Application() {

    private lateinit var entity: Entity

    override fun onInit() {

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

        val model = RawModel.create(
            floatArrayOf(
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0f, 0.5f, 0f
            )
        )

        entity = Entity(model)

    }

    override fun onUpdate(delta: Float) {
        entity.rotation.y += Math.PI.toFloat() * delta
        Renderer.render(entity)
    }

    override fun onEnd() {}

}


fun main() {
    ASMC().start()
}