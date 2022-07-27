package com.example.mapdungeon.model

import java.util.*

data class Bingo (
    val mutableList: Array<Mission>,
    val id: String = UUID.randomUUID().toString(),
    val createdAt: Date = Date(),
    var isClear: Boolean = false,
    var clearedAt: Boolean = false
) {
}