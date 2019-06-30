package com.mcmacker4.asmc.event


abstract class InputEvent : Event()


data class KeyEvent(
    val key: Int,
    val scancode: Int,
    val action: Int,
    val mods: Int
) : InputEvent()

data class MouseButtonEvent(
    val button: Int,
    val action: Int,
    val mods: Int
) : InputEvent()