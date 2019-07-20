package com.mcmacker4.asmc.block


typealias BlockID = Short

object Blocks {

    const val AIR: BlockID = 0
    const val STONE: BlockID = 1
    const val GRASS: BlockID = 2

    private val BLOCKS = hashMapOf(
        Pair(AIR, Block(null, true)),
        Pair(STONE, Block(BlockTexture(1, 1, 1, 1, 1, 1), false)),
        Pair(GRASS, Block(BlockTexture(3, 3, 3, 3, 0, 2), false))
    )
    
    private val UNKNOWN = Block(BlockTexture(31, 31, 31, 31, 31, 31), false)
    
    operator fun get(id: BlockID) : Block {
        return BLOCKS[id] ?: UNKNOWN
    }

}