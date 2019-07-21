package com.mcmacker4.asmc.world

import com.mcmacker4.asmc.block.Block
import com.mcmacker4.asmc.block.BlockID
import com.mcmacker4.asmc.block.Blocks
import com.mcmacker4.asmc.engine.render.Renderer
import com.mcmacker4.asmc.engine.view.Camera
import com.mcmacker4.asmc.util.Log
import com.mcmacker4.asmc.util.mod


class World {
    
    data class ChunkPos(val xpos: Int, val zpos: Int)
    
    private val camera = Camera()
    private val chunks = hashMapOf<ChunkPos, Chunk>()
    
    init {
        repeat(CHUNKS_Z) { k ->
            repeat(CHUNKS_X) { i ->
                chunks[ChunkPos(i, k)] = Chunk.create(this, i, k)
            }
        }
        chunks.forEach { (_, chunk) -> 
            updateChunkAndNeighbours(chunk)
        }
    }
    
    fun update(delta: Float) {
        camera.update(delta)
    }
    
    private fun updateChunkAndNeighbours(chunk: Chunk) {
        val neighbours = arrayOf(
            chunks[ChunkPos(chunk.xpos, chunk.zpos + 1)],
            chunks[ChunkPos(chunk.xpos, chunk.zpos - 1)],
            chunks[ChunkPos(chunk.xpos - 1, chunk.zpos)],
            chunks[ChunkPos(chunk.xpos + 1, chunk.zpos)]
        )
        chunk.updateVAO(neighbours)
    }
    
    fun render() {
        Renderer.init()
        Renderer.setCamera(camera)
        chunks.forEach { (_, chunk) ->
            Renderer.render(chunk)
        }
        Renderer.finalize()
    }

    fun getBlock(x: Int, y: Int, z: Int): Block {
        if (y < 0 || y >= Chunk.HEIGHT) return Blocks[Blocks.AIR]
        val bx = x mod Chunk.WIDTH
        val bz = z mod Chunk.DEPTH
        val cx = x / Chunk.WIDTH
        val cz = z / Chunk.DEPTH
        val chunk = chunks[ChunkPos(cx, cz)] ?: return Blocks[Blocks.AIR]
        return chunk.getBlock(bx, y, bz)
    }
    
    fun setBlock(x: Int, y: Int, z: Int, type: BlockID) {
        val bx = x mod Chunk.WIDTH
        val bz = z mod Chunk.DEPTH
        val cx = x / Chunk.WIDTH
        val cz = z / Chunk.DEPTH
        val chunk = chunks[ChunkPos(cx, cz)]
        if (chunk != null) {
            chunk.blocks[bx + y * Chunk.WIDTH + bz * Chunk.WIDTH * Chunk.HEIGHT] = type
            updateChunkAndNeighbours(chunk)
        }
    }

    companion object {
        const val CHUNKS_X = 30
        const val CHUNKS_Z = 30

        const val NORTH = 0
        const val SOUTH = 1
        const val EAST = 2
        const val WEST = 3
    }
    
}