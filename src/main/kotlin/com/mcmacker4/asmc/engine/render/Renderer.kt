package com.mcmacker4.asmc.engine.render

import com.mcmacker4.asmc.engine.gl.GLProgram
import com.mcmacker4.asmc.engine.gl.GLTexture
import com.mcmacker4.asmc.engine.view.Camera
import com.mcmacker4.asmc.world.Chunk
import com.mcmacker4.asmc.world.World
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture


object Renderer {
    
    private var program = GLProgram.load("default")
    
    private val terrainTexture = GLTexture.load2D("terrain.png")
    
    private fun renderChunk(chunk: Chunk) {
        program.uniformMatrix("modelMatrix", chunk.getModelMatrix())
        chunk.vao.bind()
        glDrawArrays(GL_TRIANGLES, 0, chunk.vertexCount)
        chunk.vao.unbind()
    }
    
    private fun init() {
        program.bind()
    }
    
    private fun setCamera(camera: Camera) {
        program.uniformMatrix("viewMatrix", camera.getViewMatrixBuffer())
        program.uniformMatrix("projectionMatrix", camera.getProjectionMatrixBuffer())
        program.uniformVec3("cameraPos", camera.position)
    }
    
    private fun bindTerrainTexture() {
        program.setTextureIndex("tex", 0)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(terrainTexture.target, terrainTexture.id)
    }
    
    fun render(world: World) {
        init()
        setCamera(world.camera)
        bindTerrainTexture()
        world.chunks.forEach { (pos, chunk) ->
            if (world.isChunkVisible(pos))
                renderChunk(chunk)
        }
        finalize()
    }
    
    private fun finalize() {
        program.unbind()
    }
    
}