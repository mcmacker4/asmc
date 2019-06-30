package com.mcmacker4.asmc.input


object Input {
    
    private val keyboardListeners = arrayListOf<EventListener<KeyboardEvent>>()
    private val mouseCursorListeners = arrayListOf<EventListener<MouseCursorEvent>>()
    private val mouseButtonListeners = arrayListOf<EventListener<MouseButtonEvent>>()
    
    fun emit(event: Event) {
        when (event) {
            is KeyboardEvent -> keyboardListeners.invokeAll(event)
            is MouseCursorEvent -> mouseCursorListeners.invokeAll(event)
            is MouseButtonEvent -> mouseButtonListeners.invokeAll(event)
        }
    }
    
    fun addKeyboardListener(listener: EventListener<KeyboardEvent>) =
        keyboardListeners.add(listener)

    fun addMouseCursorListener(listener: EventListener<MouseCursorEvent>) =
        mouseCursorListeners.add(listener)

    fun addMouseButtonListener(listener: EventListener<MouseButtonEvent>) =
        mouseButtonListeners.add(listener)

    fun removeKeyboardListener(listener: EventListener<KeyboardEvent>) =
        keyboardListeners.remove(listener)

    fun removeMouseCursorListener(listener: EventListener<MouseCursorEvent>) =
        mouseCursorListeners.remove(listener)

    fun removeMouseButtonListener(listener: EventListener<MouseButtonEvent>) =
        mouseButtonListeners.remove(listener)
    
}

fun<T : Event> List<EventListener<T>>.invokeAll(event: T) {
    forEach {
        it.invoke(event)
        if (event.consumed) return
    }
}