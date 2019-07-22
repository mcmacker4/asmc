package com.mcmacker4.asmc.world

import com.mcmacker4.asmc.block.BlockID
import com.mcmacker4.asmc.block.Blocks
import com.mcmacker4.asmc.engine.extensions.*
import com.mcmacker4.asmc.engine.render.Renderer
import com.mcmacker4.asmc.engine.view.Camera
import com.mcmacker4.asmc.input.Input
import com.mcmacker4.asmc.util.mod
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import java.util.*
import kotlin.math.PI
import kotlin.math.sqrt


class World {

    private val camera = Camera().apply { position.set(0f, 150f, 0f); rotation.set(-PI.toFloat(), 0f, 0f) }
    private val chunks = Collections.synchronizedMap(hashMapOf<ChunkPos, Chunk>())
    
    private var pendingChunk: Chunk? = null
    
    init {
        WorldLoader(this).start()
        Input.onKeyDown {
            if (key == GLFW.GLFW_KEY_DELETE) {
                chunks.clear()
                pendingChunk = null
            }
        }
    }

    fun update(delta: Float) {
        
        camera.update(delta)
        
        if (Input.isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_1))
            destructionRay()

        repeat(4) {
            val chunkToUnload = chunks.values.firstOrNull { it.pos.dist(getCurrentChunkPos()) > CHUNK_MAX_DIST }
            if (chunkToUnload != null) {
                chunks.remove(chunkToUnload.pos)
                chunkToUnload.unload()
            }
        }
        
        pendingChunk?.let {
            chunks[it.pos] = it
            updateChunkAndNeighbours(it, arrayListOf(NORTH, SOUTH, EAST, WEST))
            pendingChunk = null
        }
        
    }

    private fun updateChunkAndNeighbours(chunk: Chunk, whichNeighbours: List<Int>? = null) {
        val neighbours = getNeighbouringChunks(chunk.pos)
        chunk.updateVAO(neighbours)
        if (whichNeighbours != null) {
            for (dir in whichNeighbours)
                neighbours[dir]?.updateVAO()
        }
    }

    fun getNeighbouringChunks(pos: ChunkPos): Array<Chunk?> {
        return arrayOf(
            chunks[ChunkPos(pos.xpos, pos.zpos + 1)],
            chunks[ChunkPos(pos.xpos, pos.zpos - 1)],
            chunks[ChunkPos(pos.xpos - 1, pos.zpos)],
            chunks[ChunkPos(pos.xpos + 1, pos.zpos)]
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

    fun getBlockID(x: Int, y: Int, z: Int): BlockID {
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

    @Synchronized fun getCurrentChunkPos(): ChunkPos {
        return ChunkPos(
            camera.position.x.toInt() / Chunk.WIDTH,
            camera.position.z.toInt() / Chunk.DEPTH
        )
    }

    private fun isChunkLoaded(pos: ChunkPos): Boolean {
        return chunks.containsKey(pos)
    }

    @Synchronized fun isChunkLoadedOrQueued(pos: ChunkPos): Boolean {
        return isChunkLoaded(pos) || (pendingChunk?.let { it.pos == pos } ?: false)
    }
    
    @Synchronized fun loadChunk(chunk: Chunk) {
        this.pendingChunk = chunk
    }
    
    @Synchronized fun canLoadChunk() : Boolean {
        return this.pendingChunk == null
    }
    
    companion object {
        const val VIEW_RADIUS = 16
        
        val CHUNK_MAX_DIST = sqrt(VIEW_RADIUS.toFloat() * VIEW_RADIUS + VIEW_RADIUS * VIEW_RADIUS) + 1
        
        const val NORTH = 0
        const val SOUTH = 1
        const val EAST = 2
        const val WEST = 3
    }

}