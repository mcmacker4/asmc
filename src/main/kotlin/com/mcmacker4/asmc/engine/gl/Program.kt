package com.mcmacker4.asmc.engine.gl

import com.mcmacker4.asmc.engine.exceptions.ShaderLinkException
import com.mcmacker4.asmc.engine.exceptions.ShaderValidateException
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL20.*
import java.nio.FloatBuffer


class Program private constructor(id: Int) : GLObject(id) {
    
    fun uniformMatrix(name: String, matrix: FloatBuffer) {
        val loc = glGetUniformLocation(id, name)
        glUniformMatrix4fv(loc, false, matrix)
    }
    
    fun setTextureIndex(name: String, index: Int) {
        val loc = glGetUniformLocation(id, name)
        glUniform1i(loc, index)
    }
    
    override fun bind() {
        glUseProgram(id)
    }

    override fun unbind() {
        glUseProgram(0)
    }

    override fun delete() {
        glDeleteProgram(id)
    }
    
    companion object {
        
        fun create(vShader: Shader, fShader: Shader) : Program {
            val id = glCreateProgram()
            glAttachShader(id, vShader.id)
            glAttachShader(id, fShader.id)
            
            glLinkProgram(id)
            if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE)
                throw ShaderLinkException(glGetProgramInfoLog(id))
            
            glValidateProgram(id)
            if (glGetProgrami(id, GL_VALIDATE_STATUS) == GL_FALSE)
                throw ShaderValidateException(glGetProgramInfoLog(id))
            
            return Program(id)
        }

        fun load(name: String) : Program {
            return create(
                Shader.loadVertex(name),
                Shader.loadFragment(name)
            )
        }
        
    }
    
}