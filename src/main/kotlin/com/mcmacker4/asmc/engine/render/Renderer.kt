package com.mcmacker4.asmc.engine.render

import com.mcmacker4.asmc.engine.gl.GLProgram
import com.mcmacker4.asmc.engine.gl.GLTexture
import com.mcmacker4.asmc.engine.view.Camera
import com.mcmacker4.asmc.world.Chunk
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11.glBindTexture
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture
import org.lwjgl.system.MemoryUtil


object Renderer {
    
    private var program = GLProgram.load("default")
    
//    private fun prepare(scene: Scene) {
//        program.bind()
//        scene.camera.let {
//            program.uniformMatrix("viewMatrix", it.getViewMatrix())
//            program.uniformMatrix("projectionMatrix", it.getProjectionMatrix())
//        }
//    }
//    
//    private fun render(entity: ModelEntity) {
//        program.setTextureIndex("tex", 0)
//        program.uniformMatrix("modelMatrix", entity.getModelMatrix())
//        glActiveTexture(GL_TEXTURE0)
//        glBindTexture(entity.model.texture.target, entity.model.texture.id)
//        entity.model.render()
//    }
    
//    fun render(scene: Scene) {
//        prepare(scene)
//        scene.modelEntities.forEach {
//            render(it)
//        }
//        finalize()
//    }
    
    private val mmBuffer = MemoryUtil.memAllocFloat(4*4)
    private val terrainTexture = GLTexture.load("terrain.png")
    
    init {
        Matrix4f().identity().get(mmBuffer)
    }
    
    private fun renderChunk(chunk: Chunk) {
        program.setTextureIndex("tex", 0)
        program.uniformMatrix("modelMatrix", mmBuffer)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(terrainTexture.target, terrainTexture.id)
        chunk.render()
    }
    
    fun render(camera: Camera, chunk: Chunk) {
        program.bind()
        program.uniformMatrix("viewMatrix", camera.getViewMatrix())
        program.uniformMatrix("projectionMatrix", camera.getProjectionMatrix())
        renderChunk(chunk)
        finalize()
    }
    
    private fun finalize() {
        program.unbind()
    }
    
}