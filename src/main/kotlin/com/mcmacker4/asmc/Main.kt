package com.mcmacker4.asmc

import com.mcmacker4.asmc.engine.Application
import com.mcmacker4.asmc.engine.Window
import com.mcmacker4.asmc.engine.model.ModelEntity
import com.mcmacker4.asmc.engine.model.RawModel
import com.mcmacker4.asmc.engine.render.Renderer
import com.mcmacker4.asmc.input.Input
import org.lwjgl.glfw.GLFW.*


class ASMC : Application() {

    private lateinit var entity: ModelEntity

    override fun onInit() {
        
        Input.grabCursor()
        
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

        entity = ModelEntity(model).apply {
            position.z = -1f
            scale.set(0.5f)
        }

    }

    override fun onUpdate(delta: Float) {
        entity.rotation.y += Math.PI.toFloat() * delta
        
        Renderer.prepare()
        Renderer.render(entity)
        Renderer.finalize()
    }

    override fun onEnd() {}

}


fun main() {
    ASMC().start()
}