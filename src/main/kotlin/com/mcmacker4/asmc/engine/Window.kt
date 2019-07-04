package com.mcmacker4.asmc.engine

import com.mcmacker4.asmc.input.Input
import com.mcmacker4.asmc.input.KeyboardEvent
import com.mcmacker4.asmc.input.MouseButtonEvent
import com.mcmacker4.asmc.input.MouseCursorEvent
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.glViewport
import org.lwjgl.opengl.GLUtil
import org.lwjgl.system.Callback
import org.lwjgl.system.MemoryUtil.NULL


object Window {
    
    var width = 0
        private set
    
    var height = 0
        private set
    
    lateinit var title: String
        private set
    
    var fullscreen = false
        private set
    
    private var glfwWindow: Long = NULL
    
    private var lastW = 0
    private var lastH = 0
    
    private var debugProc: Callback? = null
    
    fun open(width: Int, height: Int, title: String, centered: Boolean = true) {
        if (glfwWindow != NULL)
            throw IllegalStateException("A window already exists.")
        
        this.width = width
        this.height = height
        this.title = title
        
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE)
        
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL)

        if (centered) centerWindow()
        
        glfwShowWindow(glfwWindow)
        
        glfwMakeContextCurrent(glfwWindow)
        GL.createCapabilities()

        glfwSetWindowSizeCallback(glfwWindow, ::onWindowResize)

        glfwSetKeyCallback(glfwWindow, ::onKeyboardEvent)
        glfwSetMouseButtonCallback(glfwWindow, ::onMouseButtonEvent)
        glfwSetCursorPosCallback(glfwWindow, ::onMouseCursorEvent)
        
        debugProc = GLUtil.setupDebugMessageCallback()
        
        glfwSwapInterval(0)
    }
    
    fun update() {
        glfwSwapBuffers(glfwWindow)
    }

    fun close() {
        glfwDestroyWindow(glfwWindow)
        debugProc?.free()
        glfwWindow = NULL
    }

    fun willClose() =
        glfwWindowShouldClose(glfwWindow)
    
    
    private fun onWindowResize(window: Long, width: Int, height: Int) {
        assert(window == glfwWindow)
        this.width = width
        this.height = height
        glViewport(0, 0, width, height)
    }
    
    private fun onKeyboardEvent(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        assert(window == glfwWindow)
        Input.emit(KeyboardEvent(key, scancode, action, mods))
    }
    
    private fun onMouseButtonEvent(window: Long, button: Int, action: Int, mods: Int) {
        assert(window == glfwWindow)
        Input.emit(MouseButtonEvent(button, action, mods))
    }
    
    private fun onMouseCursorEvent(window: Long, xpos: Double, ypos: Double) {
        assert(window == glfwWindow)
        Input.emit(MouseCursorEvent(xpos, ypos))
    }
    
    private fun centerWindow() {
        glfwGetVideoMode(glfwGetPrimaryMonitor())?.let {
            glfwSetWindowPos(
                glfwWindow,
                (it.width() - width) / 2,
                (it.height() - height) / 2
            )
        }
    }
    
    private fun updateResolution() {
        if (glfwWindow != NULL)
            glfwSetWindowSize(glfwWindow, width, height)
    }
    
    private fun updateTitle() {
        if (glfwWindow != NULL)
            glfwSetWindowTitle(glfwWindow, title)
    }
    
    private fun updateFullscreen() {
        if (fullscreen) {
            val monitor = glfwGetPrimaryMonitor()
            lastW = width
            lastH = height
            glfwGetVideoMode(monitor)?.let {
                glfwSetWindowMonitor(
                    glfwWindow,
                    if (fullscreen) glfwGetPrimaryMonitor() else NULL,
                    0, 0,
                    it.width(), it.height(),
                    GLFW_DONT_CARE)
            }
        } else {
            val monitor = glfwGetPrimaryMonitor()
            glfwGetVideoMode(monitor)?.let {
                glfwSetWindowMonitor(
                    glfwWindow,
                    NULL,
                    (it.width() - lastW) / 2,
                    (it.height() - lastH) / 2,
                    lastW,
                    lastH,
                    GLFW_DONT_CARE
                )
            }
        }
    }

    fun setWidth(value: Int) {
        width = value
        updateResolution()
    }

    fun setHeight(value: Int) {
        height = value
        updateResolution()
    }

    fun setTitle(value: String) {
        title = value
        updateTitle()
    }

    fun setFullscreen(value: Boolean) {
        fullscreen = value
        updateFullscreen()
    }
    
    fun toggleFullscreen() =
        setFullscreen(!fullscreen)
    
}