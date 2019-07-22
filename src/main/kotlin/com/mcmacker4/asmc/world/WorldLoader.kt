package com.mcmacker4.asmc.world

class WorldLoader(private val world: World) : Thread() {
    
    init {
        isDaemon = true
    }
            
    override fun run() {
        
        while (true) {
            
            if (world.canAddChunkToLoadQueue()) {
                val chunkPos = findClosestUnloadedChunk()
                if (chunkPos != null) {
                    val chunk = Chunk.create(world, chunkPos)
                    world.addChunkToLoadQueue(chunk)
                }
            }
            
            yield()
            
        }
        
    }
    
    private fun findClosestUnloadedChunk() : ChunkPos? {
        val origin = world.getCurrentChunkPos()
        var n = 0; var d = 1
        if (!world.isChunkLoadedOrQueued(origin))
            return origin
        repeat(World.VIEW_RADIUS) {
            // Vertical
            for (i in -n..n) {
                if (!world.isChunkLoadedOrQueued(origin.xpos + d, origin.zpos + i))
                    return ChunkPos(origin.xpos + d, origin.zpos + i)
                else if (!world.isChunkLoadedOrQueued(origin.xpos - d, origin.zpos + i))
                    return ChunkPos(origin.xpos - d, origin.zpos + i)
            }
            n++
            // Horizontal
            for (i in -n..n) {
                if (!world.isChunkLoadedOrQueued(origin.xpos + i, origin.zpos + d))
                    return ChunkPos(origin.xpos + i, origin.zpos + d)
                else if (!world.isChunkLoadedOrQueued(origin.xpos + i, origin.zpos - d))
                    return ChunkPos(origin.xpos + i, origin.zpos - d)
            }
            d++
        }
        return null
    }
    
}