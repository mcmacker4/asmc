package com.mcmacker4.asmc.engine.render

import com.mcmacker4.asmc.engine.gl.Program
import com.mcmacker4.asmc.engine.model.ModelEntity
import com.mcmacker4.asmc.engine.view.Camera


object Renderer {
    
    private var program = Program.load("default")
    
    val camera = Camera()
    
    fun prepare() {
        program.bind()
        program.uniformMatrix("viewMatrix", camera.getViewMatrix())
        program.uniformMatrix("projectionMatrix", camera.getProjectionMatrix())
    }
    
    fun render(entity: ModelEntity) {
        program.bind()
        program.uniformMatrix("modelMatrix", entity.getModelMatrix())
        entity.model.render()
        program.unbind()
    }
    
    fun finalize() {
        program.unbind()
    }
    
}