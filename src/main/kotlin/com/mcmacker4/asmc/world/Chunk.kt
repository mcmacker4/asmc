package com.mcmacker4.asmc.world

import com.mcmacker4.asmc.block.Block
import com.mcmacker4.asmc.block.BlockID
import com.mcmacker4.asmc.block.BlockVertices
import com.mcmacker4.asmc.block.Blocks
import com.mcmacker4.asmc.engine.gl.VAO
import com.mcmacker4.asmc.engine.gl.VBO
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer


class Chunk private constructor(val world: World, val blocks: Array<BlockID>, val pos: ChunkPos) {
    
    var vao: VAO = VAO.EMPTY
    var vertexCount: Int = 0
    
    private val mmBuffer: FloatBuffer = MemoryUtil.memAllocFloat(4*4)
    
    fun updateVAO() = updateVAO(world.getNeighbouringChunks(pos))
    
    fun updateVAO(neighbours: Array<Chunk?>? = null) {
        val positions = arrayListOf<Float>()
        val normals = arrayListOf<Float>()
        val uvs = arrayListOf<Float>()
        repeat(DEPTH) { k ->
            repeat(HEIGHT) { j ->
                repeat(WIDTH) { i ->
                    val pos = Vector3f(i.toFloat(), j.toFloat(), k.toFloat())
                    getBlock(i, j, k, neighbours).texture?.let { texture ->
                        if (getBlock(i, j + 1, k, neighbours).translucent)
                            BlockVertices.addUp(positions, normals, uvs, pos, texture)
                        if (getBlock(i, j - 1, k, neighbours).translucent)
                            BlockVertices.addDown(positions, normals, uvs, pos, texture)
                        if (getBlock(i, j, k + 1, neighbours).translucent)
                            BlockVertices.addNorth(positions, normals, uvs, pos, texture)
                        if (getBlock(i, j, k - 1, neighbours).translucent)
                            BlockVertices.addSouth(positions, normals, uvs, pos, texture)
                        if (getBlock(i - 1, j, k, neighbours).translucent)
                            BlockVertices.addEast(positions, normals, uvs, pos, texture)
                        if (getBlock(i + 1, j, k, neighbours).translucent)
                            BlockVertices.addWest(positions, normals, uvs, pos, texture)
                    }
                }
            }
        }
        vertexCount = positions.size / 3
        if (vao == VAO.EMPTY) {
            vao = VAO.create().apply {
                bind()
                bindAttribute(VAO.POSITIONS, 3, GL_FLOAT, VBO.array(positions.toFloatArray()))
                bindAttribute(VAO.NORMALS, 3, GL_FLOAT, VBO.array(normals.toFloatArray()))
                bindAttribute(VAO.TEXTURE_UVS, 2, GL_FLOAT, VBO.array(uvs.toFloatArray()))
            }
        } else {
            vao.attributes[VAO.POSITIONS]?.write(positions.toFloatArray())
            vao.attributes[VAO.NORMALS]?.write(normals.toFloatArray())
            vao.attributes[VAO.TEXTURE_UVS]?.write(uvs.toFloatArray())
        }
    }
    
    fun getBlock(x: Int, y: Int, z: Int, neighbours: Array<Chunk?>?) : Block {
        val north = neighbours?.get(World.NORTH)
        val south = neighbours?.get(World.SOUTH)
        val east = neighbours?.get(World.EAST)
        val west = neighbours?.get(World.WEST)
        when {
            y < 0 -> return Blocks[Blocks.AIR]
            y >= HEIGHT -> return Blocks[Blocks.AIR]
            x < 0 -> return east?.getBlock(x + WIDTH, y, z) ?: Blocks[Blocks.AIR]
            x >= WIDTH -> return west?.getBlock(x - WIDTH, y, z) ?: Blocks[Blocks.AIR]
            z < 0 -> return south?.getBlock(x, y, z + DEPTH) ?: Blocks[Blocks.AIR]
            z >= WIDTH -> return north?.getBlock(x, y, z - DEPTH) ?: Blocks[Blocks.AIR]
        }
        return Blocks[blocks[x + y * WIDTH + z * WIDTH * HEIGHT]]
    }
    
    fun getBlock(x: Int, y: Int, z: Int) : Block {
        return Blocks[getBlockID(x, y, z)]
    }
    
    fun getBlockID(x: Int, y: Int, z: Int) : BlockID {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT || z < 0 || z >= DEPTH)
            return world.getBlockID(x + pos.xpos * WIDTH, y, z * pos.zpos * DEPTH)
        return blocks[x + y * WIDTH + z * WIDTH * HEIGHT]
    }
    
    fun getModelMatrix(): FloatBuffer {
        Matrix4f().identity()
            .translate(pos.xpos * WIDTH.toFloat(), 0f, pos.zpos * DEPTH.toFloat())
            .get(mmBuffer)
        return mmBuffer
    }

    fun unload() {
        vao.attributes.forEach { (_, vbo) -> vbo.delete() }
        vao.delete()
    }

    companion object {
        
        const val WIDTH = 8
        const val HEIGHT = 128
        const val DEPTH = 8
        
        private const val FEATURE_SIZE = 24
        
        private val noise = OpenSimplexNoise(1)
        
        fun create(world: World, pos: ChunkPos) : Chunk {
            val blocks = Array(WIDTH * HEIGHT * DEPTH) { Blocks.AIR }
            repeat(WIDTH) { i ->
                repeat(DEPTH) { k ->
                    val height = (noise.eval(
                        (i + pos.xpos * WIDTH).toDouble() / FEATURE_SIZE,
                        (k + pos.zpos * DEPTH).toDouble() / FEATURE_SIZE
                    ) * 12 + 100).toInt()
                    repeat(HEIGHT) { j ->
                        val cave = noise.eval(
                            (i + pos.xpos * WIDTH).toDouble() / FEATURE_SIZE,
                            j.toDouble() / FEATURE_SIZE,
                            (k + pos.zpos * DEPTH).toDouble() / FEATURE_SIZE
                        )
                        blocks[i + j * WIDTH + k * WIDTH * HEIGHT] = when {
                            cave > 0.5 -> Blocks.AIR
                            else -> when {
                                j > height -> Blocks.AIR
                                j == height -> Blocks.GRASS
                                else -> Blocks.STONE
                            }
                        }
                    }
                }
            }
            return Chunk(world, blocks, pos)
        }
        
    }
    
}