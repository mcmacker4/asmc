package com.mcmacker4.asmc.engine.gl

import com.mcmacker4.asmc.engine.exceptions.STBLoadException
import org.lwjgl.opengl.GL11.*
import org.lwjgl.stb.STBImage.*
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer


class Texture constructor(id: Int, val target: Int) : GLObject(id) {

    override fun bind() {
        glBindTexture(target, id)
    }

    override fun unbind() {
        glBindTexture(target, 0)
    }

    override fun delete() {
        glDeleteTextures(id)
    }

    companion object {
        
        fun load(path: String) : Texture {
            
            val id = glGenTextures()
            glBindTexture(GL_TEXTURE_2D, id)
            
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            
            Texture::class.java.getResourceAsStream("/textures/$path").use { file ->
                val fileArray = file.readBytes()
                val fileBuffer = ByteBuffer.wrap(fileArray).flip()
                
                if (!stbi_info_from_memory(fileBuffer, IntArray(1), IntArray(1), IntArray(1))) {
                    throw Exception(stbi_failure_reason())
                } else {
                    MemoryStack.stackPush().use { stack ->
                        val xbuff = stack.mallocInt(1)
                        val ybuff = stack.mallocInt(1)
                        val cbuff = stack.mallocInt(1)

                        val imageData = stbi_load_from_memory(fileBuffer, xbuff, ybuff, cbuff, 4)
                            ?: throw STBLoadException("STB could not load image from memory.")
                        glTexImage2D(
                            GL_TEXTURE_2D,
                            0,
                            GL_RGBA,
                            xbuff.get(),
                            ybuff.get(),
                            0,
                            GL_RGBA,
                            GL_UNSIGNED_BYTE,
                            imageData
                        )
                        stbi_image_free(imageData)
                    }
                }
            }
            
            return Texture(id, GL_TEXTURE_2D)
            
        }
        
    }

}