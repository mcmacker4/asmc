package com.mcmacker4.asmc.world

import com.mcmacker4.asmc.block.Block
import com.mcmacker4.asmc.block.BlockID
import com.mcmacker4.asmc.block.Blocks
import com.mcmacker4.asmc.engine.extensions.*
import com.mcmacker4.asmc.engine.render.Renderer
import com.mcmacker4.asmc.engine.view.Camera
import com.mcmacker4.asmc.input.Input
import com.mcmacker4.asmc.util.mod
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW


class World {
    
    data class ChunkPos(val xpos: Int, val zpos: Int)
    
    private val camera = Camera().apply { position.set(0f, 150f, 0f) }
    private val chunks = hashMapOf<ChunkPos, Chunk>()
    
    init {
        for (k in CHUNKS_Z) {
            for (i in CHUNKS_X) {
                chunks[ChunkPos(i, k)] = Chunk.create(this, i, k)
            }
        }
        chunks.forEach { (_, chunk) -> 
            updateChunkAndNeighbours(chunk)
        }
        Input.onMouseDown {
            when (button) {
                GLFW.GLFW_MOUSE_BUTTON_1 -> destructionRay()
            }
        }
    }
    
    fun update(delta: Float) {
        camera.update(delta)
    }
    
    private fun updateChunkAndNeighbours(chunk: Chunk, whichNeighbours: List<Int>? = null) {
        val neighbours = getNeighbouringChunks(chunk.xpos, chunk.zpos)
        chunk.updateVAO(neighbours)
        if (whichNeighbours != null) {
            for (dir in whichNeighbours)
                neighbours[dir]?.updateVAO()
        }
    }
    
    fun getNeighbouringChunks(xpos: Int, zpos: Int): Array<Chunk?> {
        return arrayOf(
            chunks[ChunkPos(xpos, zpos + 1)],
            chunks[ChunkPos(xpos, zpos - 1)],
            chunks[ChunkPos(xpos - 1, zpos)],
            chunks[ChunkPos(xpos + 1, zpos)]
        )
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
        return Blocks[getBlockID(x, y, z)]
    }
    
    fun getBlockID(x: Int, y: Int, z: Int) : BlockID {
        if (y < 0 || y >= Chunk.HEIGHT) return Blocks.AIR
        val bx = x mod Chunk.WIDTH
        val bz = z mod Chunk.DEPTH
        val cx = x / Chunk.WIDTH
        val cz = z / Chunk.DEPTH
        val chunk = chunks[ChunkPos(cx, cz)] ?: return Blocks.AIR
        return chunk.getBlockID(bx, y, bz)
    }
    
    fun setBlock(x: Int, y: Int, z: Int, type: BlockID) {
        val bx = x mod Chunk.WIDTH
        val bz = z mod Chunk.DEPTH
        val cx = x / Chunk.WIDTH
        val cz = z / Chunk.DEPTH
        val chunk = chunks[ChunkPos(cx, cz)]
        if (chunk != null) {
            chunk.blocks[bx + y * Chunk.WIDTH + bz * Chunk.WIDTH * Chunk.HEIGHT] = type
            val which = arrayListOf<Int>()
            if (bx == 0) which.add(EAST)
            if (bx == Chunk.WIDTH - 1) which.add(WEST)
            if (bz == 0) which.add(SOUTH)
            if (bz == Chunk.WIDTH - 1) which.add(NORTH)
            updateChunkAndNeighbours(chunk, which)
        }
    }
    
    private fun destructionRay() {
        val look = camera.getLookVector()
        val pos = Vector3f(camera.position)
        val step = 0.01f
        var times = 0
        while (times * step < 1000f) {
            if (getBlockID(pos.x.toInt(), pos.y.toInt(), pos.z.toInt()) != Blocks.AIR) {
                setBlock(pos.x.toInt(), pos.y.toInt(), pos.z.toInt(), Blocks.AIR)
                break
            }
            pos += look * step
            times++
        }
    }

    companion object {
        val CHUNKS_X = -30..30
        val CHUNKS_Z = -30..30

        const val NORTH = 0
        const val SOUTH = 1
        const val EAST = 2
        const val WEST = 3
    }
    
}