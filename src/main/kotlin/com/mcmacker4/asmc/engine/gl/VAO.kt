package com.mcmacker4.asmc.engine.gl

import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*


class VAO private constructor(id: Int) : GLObject(id) {
    
    override fun bind() {
        glBindVertexArray(id)
    }

    override fun unbind() {
        glBindVertexArray(0)
    }
    
    fun bindAttribute(index: Int, size: Int, type: Int, buffer: VBO) {
        buffer.bind()
        glVertexAttribPointer(index, size, type, false, 0, 0)
        glEnableVertexAttribArray(index)
    }
    
    override fun delete() {
        glDeleteVertexArrays(id)
    }

    companion object {
        
        fun create() =
            VAO(glGenVertexArrays())
        
    }
    
}