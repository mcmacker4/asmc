package com.mcmacker4.asmc.engine.gl

import com.mcmacker4.asmc.engine.exceptions.ShaderCompileException
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL20.*


class Shader private constructor(id: Int) : GLObject(id) {

    override fun bind() {}
    override fun unbind() {}

    override fun delete() {
        glDeleteShader(id)
    }
    
    companion object {
        
        fun createVertex(source: String) : Shader {
            val id  = glCreateShader(GL_VERTEX_SHADER)
            glShaderSource(id, source)
            glCompileShader(id)
            if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE)
                throw ShaderCompileException(glGetShaderInfoLog(id))
            return Shader(id)
        }
        
    }

}