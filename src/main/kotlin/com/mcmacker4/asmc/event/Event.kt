package com.mcmacker4.asmc.event


abstract class Event {
    
    var consumed: Boolean = false
        private set
    
    fun consume() {
        consumed = true
    }
    
}