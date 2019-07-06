package com.mcmacker4.asmc.engine.scene

import com.mcmacker4.asmc.engine.gl.VAO
import com.mcmacker4.asmc.engine.gl.VBO
import org.lwjgl.opengl.GL11.*


class RawModel private constructor(private val vao: VAO, private val vertexCount: Int) {
    
    fun render() {
        vao.bind()
        //glDrawArrays(GL_TRIANGLES, 0, vertexCount)
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)
        vao.unbind()
    }
    
    companion object {
        
        fun create(indices: IntArray, positions: FloatArray) : RawModel {
            
            val indicesBuff = VBO.indices(indices)
            val posBuffer = VBO.array(positions)
            
            val vao = VAO.create().apply {
                bind()
                indicesBuff.bind()
                bindAttribute(0, 3, GL_FLOAT, posBuffer)
            }

            return RawModel(vao, indices.size)
            
        } 
        
    }
    
}