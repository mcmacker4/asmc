package com.mcmacker4.asmc.world

import com.mcmacker4.asmc.block.BlockID
import com.mcmacker4.asmc.block.Blocks
import com.mcmacker4.asmc.engine.extensions.*
import com.mcmacker4.asmc.engine.view.Camera
import com.mcmacker4.asmc.input.Input
import com.mcmacker4.asmc.util.mod
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import java.util.*
import kotlin.math.sqrt


class World {

    val camera = Camera().apply { position.set(0f, 100f, 0f) }
    val chunks: MutableMap<ChunkPos, Chunk> = Collections.synchronizedMap(hashMapOf<ChunkPos, Chunk>())
    
    private var pendingChunks = ArrayDeque<Chunk>(2)
    
    init {
        WorldLoader(this).start()
        Input.onKeyDown {
            if (key == GLFW.GLFW_KEY_DELETE) {
                chunks.clear()
                synchronized(pendingChunks) {
                    pendingChunks.clear()
                }
            }
        }
    }

    fun update(delta: Float) {
        
        camera.update(delta)
        
        if (Input.isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_1))
            destructionRay()

        val current = getCurrentChunkPos()
        
        val chunksWithDistToRemove = chunks.values.map {
            Pair(it.pos.dist(current), it)
        }.filter { it.first > CHUNK_MAX_DIST }
        
        repeat(4) {
            chunksWithDistToRemove.maxBy { it.first }?.let { (_, chunk) ->
                chunk.unload()
                chunks.remove(chunk.pos)
            }
        }
        
        synchronized(pendingChunks) {
            if (pendingChunks.isNotEmpty()) {
                val chunk = pendingChunks.remove()
                chunks[chunk.pos] = chunk
                updateChunkAndNeighbours(chunk, arrayListOf(NORTH, SOUTH, EAST, WEST))
            }
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
            chunk.setBlock(bx, y, bz, type)
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

    fun isChunkLoadedOrQueued(pos: ChunkPos): Boolean {
        return isChunkLoaded(pos) || synchronized(pendingChunks) { pendingChunks.any { it.pos == pos } }
    }
    
    fun loadChunk(chunk: Chunk) {
        synchronized(pendingChunks) {
            pendingChunks.add(chunk)
        }
    }
    
    fun pendingChunkSlotsAvailable() : Int {
        return synchronized(pendingChunks) { 4 - pendingChunks.size }
    }
    
    fun isChunkVisible(pos: ChunkPos): Boolean {
        val chunk = chunks[pos] ?: return false
        val p0 = Vector3f(pos.xpos.toFloat() * Chunk.WIDTH, 0f, pos.zpos.toFloat() * Chunk.DEPTH)
        val p1 = p0 + Vector3f(Chunk.WIDTH.toFloat(), chunk.maxHeight.toFloat(), Chunk.DEPTH.toFloat())
        return camera.checkFrustumAab(p0, p1)
    }
    
    companion object {
        const val VIEW_RADIUS = 24
        
        val CHUNK_MAX_DIST = sqrt(VIEW_RADIUS.toFloat() * VIEW_RADIUS + VIEW_RADIUS * VIEW_RADIUS) + 1
        
        const val NORTH = 0
        const val SOUTH = 1
        const val EAST = 2
        const val WEST = 3
    }

}