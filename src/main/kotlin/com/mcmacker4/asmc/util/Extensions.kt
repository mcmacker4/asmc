package com.mcmacker4.asmc.util


infix fun Int.mod(o: Int) : Int {
    val r = this.rem(o)
    return if (r < 0) r + o else r
}