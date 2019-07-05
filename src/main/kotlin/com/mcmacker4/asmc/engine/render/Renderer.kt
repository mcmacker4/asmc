package com.mcmacker4.asmc.engine.render

import com.mcmacker4.asmc.engine.gl.Program
import com.mcmacker4.asmc.engine.model.Entity


object Renderer {
    
    private var program = Program.load("default")
    
    fun render(entity: Entity) {
        program.bind()
        program.uniformMatrix("modelMatrix", entity.getModelMatrix())
        entity.model.render()
        program.unbind()
    }
    
}