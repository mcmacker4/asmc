package com.mcmacker4.asmc.event

typealias Listener<T> = (T) -> Unit

abstract class EventEmitter<T : Event> {
    
    private val listeners = arrayListOf<Listener<T>>()
    
    protected fun emit(event: T) {
        for (listener in listeners) {
            listener.invoke(event)
            if (event.consumed)
                break
        }
    }
    
    fun addListener(listener: Listener<T>) {
        listeners.add(listener)
    }
    
    fun removeListener(listener: Listener<T>) {
        listeners.remove(listener)
    }
    
}