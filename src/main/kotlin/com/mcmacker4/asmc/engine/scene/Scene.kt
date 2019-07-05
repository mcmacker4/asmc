package com.mcmacker4.asmc.engine.scene

import com.mcmacker4.asmc.engine.view.Camera


class Scene(
    val camera: Camera
) {
    
    val modelEntities = arrayListOf<ModelEntity>()
    
    fun update(delta:  Float) {
        camera.update(delta)
        modelEntities.forEach {
            it.update(delta)
        }
    }
    
}