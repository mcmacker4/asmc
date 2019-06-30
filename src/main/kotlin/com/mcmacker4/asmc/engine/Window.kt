package com.mcmacker4.asmc.engine

import com.mcmacker4.asmc.event.EventEmitter
import com.mcmacker4.asmc.event.InputEvent
import com.mcmacker4.asmc.event.KeyEvent
import com.mcmacker4.asmc.event.MouseButtonEvent
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryUtil.NULL


object Window : EventEmitter<InputEvent>() {
    
    private val defaultWidth = 1280
    private val defaultHeight = 720
    
    var width: Int = defaultWidth
        private set
    
    var height: Int = defaultHeight
        private set
    
    var title: String = "Another Shitty Minecraft Clone"
        private set
    
    var fullscreen = false
        private set
    
    private var glfwWindow: Long = NULL
    
    fun open(centered: Boolean = true) {
        if (glfwWindow != NULL)
            throw IllegalStateException("A window already exists.")
        
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL)
        
        glfwSetKeyCallback(glfwWindow, ::onKeyboardEvent)
        glfwSetMouseButtonCallback(glfwWindow, ::onMouseButtonEvent)
        glfwSetWindowSizeCallback(glfwWindow, ::onWindowResize)
        
        if (centered) centerWindow()
        
        glfwShowWindow(glfwWindow)
        
        glfwMakeContextCurrent(glfwWindow)
        
        glfwSwapInterval(0)
    }
    
    fun update() {
        glfwSwapBuffers(glfwWindow)
    }

    fun close() {
        glfwDestroyWindow(glfwWindow)
        glfwWindow = NULL
    }

    fun willClose() =
        glfwWindowShouldClose(glfwWindow)
    
    
    private fun onWindowResize(window: Long, width: Int, height: Int) {
        assert(window == glfwWindow)
        this.width = width
        this.height = height
    }
    
    private fun onKeyboardEvent(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        assert(window == glfwWindow)
        emit(KeyEvent(key, scancode, action, mods))
    }
    
    private fun onMouseButtonEvent(window: Long, button: Int, action: Int, mods: Int) {
        assert(window == glfwWindow)
        emit(MouseButtonEvent(button, action, mods))
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
                    (it.width() - defaultWidth) / 2,
                    (it.height() - defaultHeight) / 2,
                    defaultWidth,
                    defaultHeight,
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
    
}