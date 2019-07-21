package com.mcmacker4.asmc.util

import java.time.LocalTime

object Log {

    fun log(msg: String?) {
        val t = LocalTime.now()
        val h = t.hour.toString().padStart(2, '0')
        val m = t.minute.toString().padStart(2, '0')
        val s = t.second.toString().padStart(2, '0')
        println("[$h:$m:$s] $msg")
    }

}