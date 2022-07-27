package com.example.mapdungeon.model


import java.time.ZonedDateTime
import java.util.*

data class Mission(
    val id: String = UUID.randomUUID().toString(),
    var isClear: Boolean = false,
    val missionChar: Char,
    val createdAt: Date = Date(),
    var clearedAt: ZonedDateTime? = null
) {

}