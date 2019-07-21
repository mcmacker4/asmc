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
    
    fun addNorth(p: ArrayList<Float>, n: ArrayList<Float>, uv: ArrayList<Float>,  pos: Vector3f, texture: BlockTexture) {
        p.apply {
            add(0 + pos.x); add(1 + pos.y); add(1 + pos.z)
            add(0 + pos.x); add(0 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(1 + pos.z)
            add(0 + pos.x); add(1 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(1 + pos.y); add(1 + pos.z)
        }
        addUVs(uv, texture.north)
        repeat(6) { n.apply {
            add(0f); add(0f); add(1f)
        }}
    }
    
    fun addSouth(p: ArrayList<Float>, n: ArrayList<Float>, uv: ArrayList<Float>, pos: Vector3f, texture: BlockTexture) {
        p.apply {
            add(1 + pos.x); add(1 + pos.y); add(0 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(0 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(1 + pos.x); add(1 + pos.y); add(0 + pos.z)
            add(0 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(0 + pos.x); add(1 + pos.y); add(0 + pos.z)
        }
        addUVs(uv, texture.south)
        repeat(6) { n.apply {
            add(0f); add(0f); add(-1f)
        }}
    }

    fun addEast(p: ArrayList<Float>, n: ArrayList<Float>, uv: ArrayList<Float>, pos: Vector3f, texture: BlockTexture) {
        p.apply {
             add(0 + pos.x); add(1 + pos.y); add(0 + pos.z)
             add(0 + pos.x); add(0 + pos.y); add(0 + pos.z)
             add(0 + pos.x); add(0 + pos.y); add(1 + pos.z)
             add(0 + pos.x); add(1 + pos.y); add(0 + pos.z)
             add(0 + pos.x); add(0 + pos.y); add(1 + pos.z)
             add(0 + pos.x); add(1 + pos.y); add(1 + pos.z)
        }
        addUVs(uv, texture.east)
        repeat(6) { n.apply {
            add(-1f); add(0f); add(0f)
        }}
    }

    fun addWest(p: ArrayList<Float>, n: ArrayList<Float>, uv: ArrayList<Float>, pos: Vector3f, texture: BlockTexture) {
        p.apply {
            add(1 + pos.x); add(1 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(1 + pos.x); add(1 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(1 + pos.x); add(1 + pos.y); add(0 + pos.z)
        }
        addUVs(uv, texture.west)
        repeat(6) { n.apply {
            add(1f); add(0f); add(0f)
        }}
    }

    fun addUp(p: ArrayList<Float>, n: ArrayList<Float>, uv: ArrayList<Float>, pos: Vector3f, texture: BlockTexture) {
        p.apply {
            add(0 + pos.x); add(1 + pos.y); add(0 + pos.z)
            add(0 + pos.x); add(1 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(1 + pos.y); add(1 + pos.z)
            add(0 + pos.x); add(1 + pos.y); add(0 + pos.z)
            add(1 + pos.x); add(1 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(1 + pos.y); add(0 + pos.z)
        }
        addUVs(uv, texture.up)
        repeat(6) { n.apply {
            add(0f); add(1f); add(0f)
        }}
    }

    fun addDown(p: ArrayList<Float>, n: ArrayList<Float>, uv: ArrayList<Float>, pos: Vector3f, texture: BlockTexture) {
        p.apply {
            add(0 + pos.x); add(0 + pos.y); add(1 + pos.z)
            add(0 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(0 + pos.x); add(0 + pos.y); add(1 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(0 + pos.z)
            add(1 + pos.x); add(0 + pos.y); add(1 + pos.z)
        }
        addUVs(uv, texture.down)
        repeat(6) { n.apply {
            add(0f); add(-1f); add(0f)
        }}
    }
    
}