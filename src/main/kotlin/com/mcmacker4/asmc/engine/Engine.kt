package com.mcmacker4.asmc.engine

import com.mcmacker4.asmc.engine.gl.Program
import com.mcmacker4.asmc.engine.gl.Shader
import com.mcmacker4.asmc.engine.gl.VAO
import com.mcmacker4.asmc.engine.gl.VBO
import com.mcmacker4.asmc.engine.model.RawModel
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL11.*


object Engine {
    
    fun stop() {
        Window.close()
    }
    
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
        
        val vShader = Shader.createVertex("""
            #version 330 core
            
            layout (location = 0) in vec3 position;
            
            out vec4 color;
            
            void main() {
                gl_Position = vec4(position, 1.0);
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
        
        val program = Program.create(vShader, fShader)
        
        println(glGetString(GL_VENDOR))
        
        while (!Window.willClose()) {
            glfwPollEvents()
            
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            
            program.bind()
            model.render()
            
            Window.update()
        }
        
        VAO.cleanup()
        VBO.cleanup()
        
        Window.destroy()
        
    }
    
}