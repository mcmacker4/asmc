package com.mcmacker4.asmc.engine.render

import com.mcmacker4.asmc.engine.gl.Program
import com.mcmacker4.asmc.engine.scene.ModelEntity
import com.mcmacker4.asmc.engine.scene.Scene
import org.lwjgl.opengl.GL11.glBindTexture
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture


object Renderer {
    
    private var program = Program.load("default")
    
    private fun prepare(scene: Scene) {
        program.bind()
        scene.camera.let {
            program.uniformMatrix("viewMatrix", it.getViewMatrix())
            program.uniformMatrix("projectionMatrix", it.getProjectionMatrix())
        }
    }
    
    private fun render(entity: ModelEntity) {
        program.setTextureIndex("tex", 0)
        program.uniformMatrix("modelMatrix", entity.getModelMatrix())
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(entity.model.texture.target, entity.model.texture.id)
        entity.model.render()
    }
    
    fun render(scene: Scene) {
        prepare(scene)
        scene.modelEntities.forEach {
            render(it)
        }
        finalize()
    }
    
    private fun finalize() {
        program.unbind()
    }
    
}