package com.mcmacker4.asmc.engine.gl

import org.lwjgl.opengl.GL30.*


class VAO private constructor(id: Int) : GLObject(id) {
    
    val attributes = hashMapOf<Int, VBO>()
    
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
        attributes[index] = buffer
    }
    
    override fun delete() {
        glDeleteVertexArrays(id)
        vaos.remove(id)
    }

    companion object {
        
        const val POSITIONS = 0
        const val NORMALS = 1
        const val TEXTURE_UVS = 2
        
        private val vaos = arrayListOf<Int>()
        
        fun create() : VAO = glGenVertexArrays().let {
            vaos.add(it)
            return VAO(it)
        }
        
        fun cleanup() {
            vaos.forEach { glDeleteVertexArrays(it) }
        }
        
    }
    
}