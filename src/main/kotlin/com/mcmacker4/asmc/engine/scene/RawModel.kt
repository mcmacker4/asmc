package com.mcmacker4.asmc.engine.scene

import com.mcmacker4.asmc.engine.gl.GLTexture
import com.mcmacker4.asmc.engine.gl.VAO
import com.mcmacker4.asmc.engine.gl.VBO
import org.lwjgl.opengl.GL11.*


class RawModel private constructor(
    private val vao: VAO,
    private val vertexCount: Int,
    val texture: GLTexture
) {
    
    fun render() {
        vao.bind()
        glDrawArrays(GL_TRIANGLES, 0, vertexCount)
        vao.unbind()
    }
    
    companion object {
        
        fun create(positions: FloatArray, uvcoords: FloatArray, texture: GLTexture) : RawModel {
            
            val posBuffer = VBO.array(positions)
            val uvBuffer = VBO.array(uvcoords)
            
            val vao = VAO.create().apply {
                bind()
                bindAttribute(VAO.POSITIONS, 3, GL_FLOAT, posBuffer)
                bindAttribute(VAO.TEXTURE_UVS, 2, GL_FLOAT, uvBuffer)
            }

            return RawModel(vao, positions.size / 3, texture)
            
        } 
        
    }
    
}