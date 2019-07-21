package com.mcmacker4.asmc.engine

import com.mcmacker4.asmc.input.Input
import com.mcmacker4.asmc.input.KeyboardEvent
import com.mcmacker4.asmc.input.MouseButtonEvent
import com.mcmacker4.asmc.input.MouseCursorEvent
import com.mcmacker4.asmc.util.Log
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
    
    var glfwWindow: Long = NULL
        private set
    
    private var lastW = 0
    private var lastH = 0
    
    private var debugProc: Callback? = null
    
    fun open(width: Int, height: Int, title: String, centered: Boolean = true, fullscreen: Boolean = false) {
        if (glfwWindow != NULL)
            throw IllegalStateException("A window already exists.")
        
        Log.log("GLFW Initialized")
        
        this.width = width
        this.height = height
        
        this.lastW = width
        this.lastH = height
        
        this.title = title
        
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE)
        
        if (fullscreen) {
            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor()) ?: throw Exception("VideoMode is null!")
            glfwWindow = glfwCreateWindow(vidmode.width(), vidmode.height(), title, glfwGetPrimaryMonitor(), NULL)
        } else {
            glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL)
            if (centered) centerWindow()
        }
        
        Log.log("Window created.")
        
        glfwMakeContextCurrent(glfwWindow)
        GL.createCapabilities()

        glfwSetWindowSizeCallback(glfwWindow, ::onWindowResize)

        glfwSetKeyCallback(glfwWindow, ::onKeyboardEvent)
        glfwSetMouseButtonCallback(glfwWindow, ::onMouseButtonEvent)
        glfwSetCursorPosCallback(glfwWindow, ::onMouseCursorEvent)

        //debugProc = GLUtil.setupDebugMessageCallback()
        
        glfwSwapInterval(0)
        
        glfwShowWindow(glfwWindow)
    }
    
    fun update() {
        glfwSwapBuffers(glfwWindow)
    }
    
    fun close() {
        glfwSetWindowShouldClose(glfwWindow, true)
    }

    fun destroy() {
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
    
    private fun updateResolution(width: Int, height: Int) {
        this.width = width
        this.height = height
        if (glfwWindow != NULL)
            glfwSetWindowSize(glfwWindow, width, height)
    }
    
    private fun updateTitle(title: String) {
        this.title = title
        if (glfwWindow != NULL)
            glfwSetWindowTitle(glfwWindow, title)
    }
    
    fun aspect() = width.toFloat() / height
    
    fun isFullscreen() = glfwGetWindowMonitor(glfwWindow) != NULL

    fun setFullscreen(value: Boolean) {
        if (value) {
            val monitor = glfwGetPrimaryMonitor()
            val vidmode = glfwGetVideoMode(monitor) ?: throw Exception("Video Mode for Primary Monitor not found.")
            lastW = width
            lastH = height
            glfwSetWindowMonitor(
                glfwWindow,
                monitor,
                0, 0,
                vidmode.width(), vidmode.height(),
                GLFW_DONT_CARE)
        } else {
            val monitor = glfwGetPrimaryMonitor()
            val vidmode = glfwGetVideoMode(monitor) ?: throw Exception("Video Mode for Primary Monitor not found.")
            glfwSetWindowMonitor(
                glfwWindow,
                NULL,
                (vidmode.width() - lastW) / 2,
                (vidmode.height() - lastH) / 2,
                lastW,
                lastH,
                GLFW_DONT_CARE
            )
        }
    }
    
    fun toggleFullscreen() = setFullscreen(!isFullscreen())
    
}