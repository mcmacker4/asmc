package com.mcmacker4.asmc.world

import com.mcmacker4.asmc.block.Block
import com.mcmacker4.asmc.block.BlockID
import com.mcmacker4.asmc.block.BlockVertices
import com.mcmacker4.asmc.block.Blocks
import com.mcmacker4.asmc.engine.gl.VAO
import com.mcmacker4.asmc.engine.gl.VBO
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*


class Chunk private constructor(val blocks: Array<BlockID>) {
    
    private val vao: VAO
    
    init {
        val positions = arrayListOf<Float>()
        val uvs = arrayListOf<Float>()
        repeat(DEPTH) { k ->
            repeat(HEIGHT) { j ->
                repeat(WIDTH) { i ->
                    val pos = Vector3f(i.toFloat(), j.toFloat(), k.toFloat())
                    val texture = getBlock(i, j, k).texture
                    if (texture != null) {
                        if (getBlock(i, j + 1, k).translucent)
                            BlockVertices.addUp(positions, uvs, pos, texture)
                        if (getBlock(i, j - 1, k).translucent)
                            BlockVertices.addDown(positions, uvs, pos, texture)
                        if (getBlock(i, j, k + 1).translucent)
                            BlockVertices.addNorth(positions, uvs, pos, texture)
                        if (getBlock(i, j, k - 1).translucent)
                            BlockVertices.addSouth(positions, uvs, pos, texture)
                        if (getBlock(i - 1, j, k).translucent)
                            BlockVertices.addEast(positions, uvs, pos, texture)
                        if (getBlock(i + 1, j, k).translucent)
                            BlockVertices.addWest(positions, uvs, pos, texture)
                    }
                }
            }
        }
        vao = VAO.create().apply {
            bind()
            bindAttribute(VAO.POSITIONS, 3, GL_FLOAT, VBO.array(positions.toFloatArray()))
            bindAttribute(VAO.TEXTURE_UVS, 2, GL_FLOAT, VBO.array(uvs.toFloatArray()))
        }
    }
    
    fun getBlock(x: Int, y: Int, z: Int) : Block {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT || z < 0 || z >= DEPTH)
            return Blocks[Blocks.AIR]
        return Blocks[blocks[x + y * WIDTH + z * WIDTH * HEIGHT]]
    }
    
    fun render() {
        vao.bind()
        glDrawArrays(GL_TRIANGLES, 0, blocks.size * 6 * 6)
        vao.unbind()
    }
    
    companion object {
        
        const val WIDTH = 128
        const val HEIGHT = 4
        const val DEPTH = 128
        
        fun create() : Chunk {
            return Chunk(Array(WIDTH * HEIGHT * DEPTH) {
                (Math.random() * 3).toShort()
            })
        }
        
    }
    
}