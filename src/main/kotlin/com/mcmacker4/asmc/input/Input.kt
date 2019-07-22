package com.mcmacker4.asmc.input

import com.mcmacker4.asmc.engine.Window
import org.lwjgl.glfw.GLFW.*


object Input {
    
    private val keyDownListeners = arrayListOf<EventListener<KeyboardEvent>>()
    private val keyUpListeners = arrayListOf<EventListener<KeyboardEvent>>()
    
    private val mouseDownListeners = arrayListOf<EventListener<MouseButtonEvent>>()
    private val mouseUpListeners = arrayListOf<EventListener<MouseButtonEvent>>()
    
    private val mouseCursorListeners = arrayListOf<EventListener<MouseCursorEvent>>()
    
    var mouseX: Double
        private set
    var mouseY: Double
        private set
    
    init {
        val xarr = DoubleArray(1)
        val yarr = DoubleArray(1)
        glfwGetCursorPos(Window.glfwWindow, xarr, yarr)
        mouseX = xarr[0]
        mouseY = yarr[0]
    }

    fun emit(event: Event) {
        when (event) {
            is KeyboardEvent -> {
                when (event.action) {
                    GLFW_PRESS -> keyDownListeners.invokeAll(event)
                    GLFW_RELEASE -> keyUpListeners.invokeAll(event)
                }
            }
            is MouseButtonEvent -> {
                when (event.action) {
                    GLFW_PRESS -> mouseDownListeners.invokeAll(event)
                    GLFW_RELEASE -> mouseUpListeners.invokeAll(event)
                }
            }
            is MouseCursorEvent -> {
                val deltaEvent = MouseCursorEvent(event.dx - mouseX, event.dy - mouseY)
                mouseX = event.dx
                mouseY = event.dy
                mouseCursorListeners.invokeAll(deltaEvent)
            }
        }
    }
    
    fun isKeyDown(key: Int) = glfwGetKey(Window.glfwWindow, key) == GLFW_PRESS
    
    fun isMouseButtonDown(button: Int) = glfwGetMouseButton(Window.glfwWindow, button) == GLFW_PRESS
    
    fun grabCursor() = glfwSetInputMode(
        Window.glfwWindow,
        GLFW_CURSOR,
        GLFW_CURSOR_DISABLED
    )
    
    fun releaseCursor() = glfwSetInputMode(
        Window.glfwWindow,
        GLFW_CURSOR,
        GLFW_CURSOR_NORMAL
    )
    
    fun onKeyDown(listener: EventListener<KeyboardEvent>) =
        keyDownListeners.add(listener)
    
    fun onKeyUp(listener: EventListener<KeyboardEvent>) =
        keyUpListeners.add(listener)
    
    fun onMouseDown(listener: EventListener<MouseButtonEvent>) =
        mouseDownListeners.add(listener)
    
    fun onMouseUp(listener: EventListener<MouseButtonEvent>) =
        mouseUpListeners.add(listener)
    
    fun onMouseMoved(listener: EventListener<MouseCursorEvent>) =
        mouseCursorListeners.add(listener)
    
}

fun<T : Event> List<EventListener<T>>.invokeAll(event: T) {
    forEach {
        it.invoke(event)
        if (event.consumed) return
    }
}