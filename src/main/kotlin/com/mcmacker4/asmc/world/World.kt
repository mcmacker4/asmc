package com.mcmacker4.asmc.world

import com.mcmacker4.asmc.engine.render.Renderer
import com.mcmacker4.asmc.engine.view.Camera
import com.mcmacker4.asmc.util.Log


class World {
    
    private val camera = Camera()
    private val chunks = arrayListOf<Chunk>()
    
    init {
        repeat(CHUNKS_Z) { k ->
            repeat(CHUNKS_X) { i ->
                chunks.add(Chunk.create(i, k))
            }
        }
        
        var vcount = 0
        chunks.forEach { vcount += it.vertexCount }
        Log.log("World has ${vcount / 3} triangles.")
        
    }
    
    fun update(delta: Float) {
        camera.update(delta)
    }
    
    fun render() {
        Renderer.init()
        Renderer.setCamera(camera)
        chunks.forEach {
            Renderer.render(it)
        }
        Renderer.finalize()
    }
    
    companion object {
        const val CHUNKS_X = 40
        const val CHUNKS_Z = 40
    }
    
}