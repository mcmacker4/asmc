package com.mcmacker4.asmc.world

import java.util.*

class WorldLoader(private val world: World) : Thread() {
    
    companion object {
        const val MAX_QUEUE_SIZE = 30
    }

    private val loadQueue = ArrayList<ChunkPos>()
    
    init {
        isDaemon = true
    }
    
    
    override fun run() {
        
        while (true) {
            
            yield()

            discardQueueItems()
            fillLoadQueue()
            
            if (world.canLoadChunk()) {
                val chunkPos = loadQueue.minBy { it.dist(world.getCurrentChunkPos()) } ?: continue
                val chunk = Chunk.create(world, chunkPos)
                world.loadChunk(chunk)
                loadQueue.remove(chunkPos)
            }
            
        }
        
    }
    
    private fun fillLoadQueue() {
        while (loadQueue.size < MAX_QUEUE_SIZE) {
            val chunkPos = findClosestUnloadedChunk()
            if (chunkPos != null)
                loadQueue.add(chunkPos)
        }
    }
    
    private fun discardQueueItems() {
        val current = world.getCurrentChunkPos()
        loadQueue.removeIf { it.dist(current) > World.CHUNK_MAX_DIST }
    }
    
    private fun isChunkLoadedOrQueued(pos: ChunkPos) : Boolean {
        return loadQueue.contains(pos) || world.isChunkLoadedOrQueued(pos)
    }
    
    private fun isChunkLoadedOrQueued(xpos: Int, ypos: Int) : Boolean {
        return isChunkLoadedOrQueued(ChunkPos(xpos, ypos))
    }
    
    private fun findClosestUnloadedChunk() : ChunkPos? {
        val origin = world.getCurrentChunkPos()
        var n = 0; var d = 1
        if (!isChunkLoadedOrQueued(origin))
            return origin
        repeat(World.VIEW_RADIUS) {
            // Vertical
            for (i in -n..n) {
                if (!isChunkLoadedOrQueued(origin.xpos + d, origin.zpos + i))
                    return ChunkPos(origin.xpos + d, origin.zpos + i)
                else if (!isChunkLoadedOrQueued(origin.xpos - d, origin.zpos + i))
                    return ChunkPos(origin.xpos - d, origin.zpos + i)
            }
            n++
            // Horizontal
            for (i in -n..n) {
                if (!isChunkLoadedOrQueued(origin.xpos + i, origin.zpos + d))
                    return ChunkPos(origin.xpos + i, origin.zpos + d)
                else if (!isChunkLoadedOrQueued(origin.xpos + i, origin.zpos - d))
                    return ChunkPos(origin.xpos + i, origin.zpos - d)
            }
            d++
        }
        return null
    }
    
}