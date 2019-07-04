package com.mcmacker4.asmc.engine

import com.mcmacker4.asmc.engine.gl.Program
import com.mcmacker4.asmc.engine.gl.Shader
import com.mcmacker4.asmc.engine.gl.VAO
import com.mcmacker4.asmc.engine.gl.VBO
import com.mcmacker4.asmc.engine.model.Entity
import com.mcmacker4.asmc.engine.model.RawModel
import com.mcmacker4.asmc.engine.render.Renderer
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL11.*


object Engine {
    
    fun start(callback: Engine.() -> Unit) {
        
        GLFWErrorCallback.createPrint(System.err).set()
        
        if (!glfwInit())
            throw RuntimeException("Could not initialize GLFW")
        
        Window.open(1280, 720, "ASMC")
        
        glClearColor(0.3f, 0.6f, 0.9f, 1.0f)
        
        callback.invoke(this)
        
        val model = RawModel.create(
            floatArrayOf(
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0f, 0.5f, 0f
            )
        )
        
        val entity = Entity(model)
        
        val vShader = Shader.createVertex("""
            #version 330 core
            
            layout (location = 0) in vec3 position;
            
            out vec4 color;
            
            uniform mat4 modelMatrix;
            
            void main() {
                gl_Position = modelMatrix * vec4(position, 1.0);
                color = vec4(position + 0.5, 1.0);
            }
        """.trimIndent())

        val fShader = Shader.createFragment("""
            #version 330 core

            in vec4 color;

            out vec4 FragColor;

            void main() {
                FragColor = color;
            }
        """.trimIndent())
        
        Renderer.program = Program.create(vShader, fShader)
        
        println(glGetString(GL_VENDOR))
        
        var current = System.currentTimeMillis()
        var last = current
        
        while (!Window.willClose()) {
            current = System.currentTimeMillis()
            val delta = (current - last) / 1000f
            last = current
            
            glfwPollEvents()
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            
            entity.rotation.y += Math.PI.toFloat() * delta
            Renderer.render(entity)
            
            Window.update()
        }
        
        VAO.cleanup()
        VBO.cleanup()
        
        Window.destroy()
        
    }

    fun stop() {
        Window.close()
    }
    
}