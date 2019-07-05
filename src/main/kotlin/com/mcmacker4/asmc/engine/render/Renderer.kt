package com.mcmacker4.asmc.engine.render

import com.mcmacker4.asmc.engine.gl.Program
import com.mcmacker4.asmc.engine.scene.ModelEntity
import com.mcmacker4.asmc.engine.scene.Scene


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
        program.uniformMatrix("modelMatrix", entity.getModelMatrix())
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