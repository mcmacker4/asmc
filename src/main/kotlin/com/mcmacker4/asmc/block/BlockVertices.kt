package com.mcmacker4.asmc.block

import org.joml.Vector3f


object BlockVertices {

    private fun addUVs(uv: ArrayList<Float>, id: Int) {
        val x = id % 16
        val y = id / 16
        uv.apply {
            add((0 + x) / 16f); add((0 + y) / 16f)
            add((0 + x) / 16f); add((1 + y) / 16f)
            add((1 + x) / 16f); add((1 + y) / 16f)
            add((0 + x) / 16f); add((0 + y) / 16f)
            add((1 + x) / 16f); add((1 + y) / 16f)
            add((1 + x) / 16f); add((0 + y) / 16f)
        }
    }
    
    fun addNorth(p: ArrayList<Float>, uv: ArrayList<Float>,  pos: Vector3f, texture: BlockTexture) {
        p.apply {
            add(0 + pos.x); add(1 + pos.y); add(1 + pos.z)
            add(0 + pos.x); add(0 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(1 + pos.z)
            add(0 + pos.x); add(1 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(1 + pos.y); add(1 + pos.z)
        }
        addUVs(uv, texture.north)
    }
    
    fun addSouth(p: ArrayList<Float>, uv: ArrayList<Float>, pos: Vector3f, texture: BlockTexture) {
        p.apply {
            add(1 + pos.x); add(1 + pos.y); add(0 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(0 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(1 + pos.x); add(1 + pos.y); add(0 + pos.z)
            add(0 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(0 + pos.x); add(1 + pos.y); add(0 + pos.z)
        }
        addUVs(uv, texture.south)
    }

    fun addEast(p: ArrayList<Float>, uv: ArrayList<Float>, pos: Vector3f, texture: BlockTexture) {
        p.apply {
             add(0 + pos.x); add(1 + pos.y); add(0 + pos.z)
             add(0 + pos.x); add(0 + pos.y); add(0 + pos.z)
             add(0 + pos.x); add(0 + pos.y); add(1 + pos.z)
             add(0 + pos.x); add(1 + pos.y); add(0 + pos.z)
             add(0 + pos.x); add(0 + pos.y); add(1 + pos.z)
             add(0 + pos.x); add(1 + pos.y); add(1 + pos.z)
        }
        addUVs(uv, texture.east)
    }

    fun addWest(p: ArrayList<Float>, uv: ArrayList<Float>, pos: Vector3f, texture: BlockTexture) {
        p.apply {
            add(1 + pos.x); add(1 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(1 + pos.x); add(1 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(1 + pos.x); add(1 + pos.y); add(0 + pos.z)
        }
        addUVs(uv, texture.west)
    }

    fun addUp(p: ArrayList<Float>, uv: ArrayList<Float>, pos: Vector3f, texture: BlockTexture) {
        p.apply {
            add(0 + pos.x); add(1 + pos.y); add(0 + pos.z)
            add(0 + pos.x); add(1 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(1 + pos.y); add(1 + pos.z)
            add(0 + pos.x); add(1 + pos.y); add(0 + pos.z)
            add(1 + pos.x); add(1 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(1 + pos.y); add(0 + pos.z)
        }
        addUVs(uv, texture.up)
    }

    fun addDown(p: ArrayList<Float>, uv: ArrayList<Float>, pos: Vector3f, texture: BlockTexture) {
        p.apply {
            add(0 + pos.x); add(0 + pos.y); add(1 + pos.z)
            add(0 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(0 + pos.x); add(0 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(1 + pos.z)
        }
        addUVs(uv, texture.down)
    }
    
}