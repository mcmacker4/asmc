package com.mcmacker4.asmc.engine.scene

import com.mcmacker4.asmc.engine.gl.VAO
import com.mcmacker4.asmc.engine.gl.VBO
import org.lwjgl.opengl.GL11.*


class RawModel private constructor(private val vao: VAO, private val vertexCount: Int) {
    
    fun render() {
        vao.bind()
        glDrawArrays(GL_TRIANGLES, 0, vertexCount)
        vao.unbind()
    }
    
    companion object {
        
        fun create(positions: FloatArray) : RawModel {
            
            val posBuffer = VBO.array(positions)
            
            val vao = VAO.create().apply {
                bind()
                bindAttribute(0, 3, GL_FLOAT, posBuffer)
            }

            return RawModel(vao, positions.size / 3)
            
        } 
        
    }
    
}