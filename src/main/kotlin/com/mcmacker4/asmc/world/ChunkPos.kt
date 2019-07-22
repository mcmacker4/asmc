package com.mcmacker4.asmc.world

import kotlin.math.sqrt


data class ChunkPos(val xpos: Int, val zpos: Int) {
    
    fun delta(other: ChunkPos) : ChunkDeltaPos {
        return ChunkDeltaPos(xpos.toFloat() - other.xpos, zpos.toFloat() - other.zpos)
    }
    
    fun dist(other: ChunkPos) : Float {
        val dx = other.xpos.toFloat() - xpos
        val dz = other.zpos.toFloat() - zpos
        return sqrt( dx * dx + dz * dz )
    }
    
}

data class ChunkDeltaPos(val xpos: Float, val zpos: Float) : Comparable<ChunkDeltaPos> {
    
    fun length() : Float {
        return sqrt( xpos * xpos + zpos * zpos )
    }
    
    override fun compareTo(other: ChunkDeltaPos): Int {
        val len1 = this.length()
        val len2 = other.length()
        return when {
            len1 < len2 -> -1
            len1 > len2 -> 1
            else -> 0
        }
    }
    
}