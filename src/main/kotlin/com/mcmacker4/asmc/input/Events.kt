package com.mcmacker4.asmc.input


typealias EventListener<T> = T.() -> Unit

abstract class Event {
    
    var consumed: Boolean = false
        private set
    
    fun consume() {
        consumed = true
    }
    
}

enum class EventType {
    KEYBOARD,
    MOUSE_BUTTON,
    MOUSE_CURSOR
}


data class KeyboardEvent(
    val key: Int,
    val scancode: Int,
    val action: Int,
    val mods: Int
) : Event()

data class MouseButtonEvent(
    val button: Int,
    val action: Int,
    val mods: Int
) : Event()

data class MouseCursorEvent(
    val xpos: Double,
    val ypos: Double
) : Event()
