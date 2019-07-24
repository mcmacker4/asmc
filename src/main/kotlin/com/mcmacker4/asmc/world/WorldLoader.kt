package com.mcmacker4.asmc.world

import java.util.*

class WorldLoader(private val world: World) : Thread() {
    
    companion object {
        const val MAX_QUEUE_SIZE = World.VIEW_RADIUS * World.VIEW_RADIUS
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

            val current = world.getCurrentChunkPos()
            for (i in 1..world.pendingChunkSlotsAvailable()) {
                val chunkPos = loadQueue.minBy {
                    // Prioritize visible chunks
                    if (world.isChunkVisible(it))
                        it.dist(current)
                    else it.dist(current) * 1.5f
                } ?: break
                loadQueue.remove(chunkPos)
                world.loadChunk(Chunk.create(world, chunkPos))
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
    
    private fun findClosestUnloadedChunk() : ChunkPos? {
        val origin = world.getCurrentChunkPos()
        var n = 0; var d = 1
        if (!isChunkLoadedOrQueued(origin))
            return origin
        repeat(World.VIEW_RADIUS) {
            // Vertical
            for (i in -n..n) {
                var chunkPos = ChunkPos(origin.xpos + d, origin.zpos + i)
                if (chunkPos.dist(origin) < World.CHUNK_MAX_DIST && !isChunkLoadedOrQueued(chunkPos))
                    return chunkPos
                chunkPos = ChunkPos(origin.xpos - d, origin.zpos + i)
                if (chunkPos.dist(origin) < World.CHUNK_MAX_DIST && !isChunkLoadedOrQueued(chunkPos))
                    return chunkPos
            }
            n++
            // Horizontal
            for (i in -n..n) {
                var chunkPos = ChunkPos(origin.xpos + i, origin.zpos + d)
                if (chunkPos.dist(origin) < World.CHUNK_MAX_DIST && !isChunkLoadedOrQueued(chunkPos))
                    return chunkPos
                chunkPos = ChunkPos(origin.xpos + i, origin.zpos - d)
                if (chunkPos.dist(origin) < World.CHUNK_MAX_DIST && !isChunkLoadedOrQueued(chunkPos))
                    return chunkPos
            }
            d++
        }
        return null
    }
    
}