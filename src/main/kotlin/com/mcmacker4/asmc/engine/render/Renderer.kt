package com.mcmacker4.asmc.engine.render

import com.mcmacker4.asmc.engine.gl.GLProgram
import com.mcmacker4.asmc.engine.gl.GLTexture
import com.mcmacker4.asmc.engine.view.Camera
import com.mcmacker4.asmc.world.Chunk
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture
import org.lwjgl.system.MemoryUtil


object Renderer {
    
    private var program = GLProgram.load("default")
    
    private val terrainTexture = GLTexture.load2D("terrain.png")
    
    private fun renderChunk(chunk: Chunk) {
        program.setTextureIndex("tex", 0)
        program.uniformMatrix("modelMatrix", chunk.getModelMatrix())
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(terrainTexture.target, terrainTexture.id)
        chunk.vao.bind()
        glDrawArrays(GL_TRIANGLES, 0, chunk.vertexCount)
        chunk.vao.unbind()
    }
    
    fun init() {
        program.bind()
    }
    
    fun setCamera(camera: Camera) {
        program.uniformMatrix("viewMatrix", camera.getViewMatrix())
        program.uniformMatrix("projectionMatrix", camera.getProjectionMatrix())
    }
    
    fun render(chunk: Chunk) {
        renderChunk(chunk)
    }
    
    fun finalize() {
        program.unbind()
    }
    
}