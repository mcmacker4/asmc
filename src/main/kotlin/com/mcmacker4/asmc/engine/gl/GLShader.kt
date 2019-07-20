package com.mcmacker4.asmc.engine.gl

import com.mcmacker4.asmc.engine.exceptions.ShaderCompileException
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL20.*
import java.io.FileNotFoundException


class GLShader private constructor(id: Int) : GLObject(id) {

    override fun bind() {}
    override fun unbind() {}

    override fun delete() {
        glDeleteShader(id)
    }
    
    companion object {
        
        private fun create(source: String, type: Int) : GLShader {
            val id  = glCreateShader(type)
            glShaderSource(id, source)
            glCompileShader(id)
            if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE)
                throw ShaderCompileException(glGetShaderInfoLog(id))
            return GLShader(id)
        }
        
        fun createVertex(source: String) = create(source, GL_VERTEX_SHADER)
        fun createFragment(source: String) = create(source, GL_FRAGMENT_SHADER)

        fun loadVertex(name: String) : GLShader {
            val file = "/shaders/$name.v.glsl"
            val stream = GLShader::class.java.getResourceAsStream(file) ?: throw FileNotFoundException(file)
            return createVertex(stream.bufferedReader().readText())
        }

        fun loadFragment(name: String) : GLShader {
            val file = "/shaders/$name.f.glsl"
            val stream = GLShader::class.java.getResourceAsStream(file) ?: throw FileNotFoundException(file)
            return createFragment(stream.bufferedReader().readText())
        }

    }

}