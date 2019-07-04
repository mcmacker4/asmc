package com.mcmacker4.asmc.engine.gl


abstract class GLObject(val id: Int) {
    
    abstract fun bind()
    abstract fun unbind()
    
    abstract fun delete()
    
}