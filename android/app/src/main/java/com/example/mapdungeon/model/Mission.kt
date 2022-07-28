package com.example.mapdungeon.model


import com.example.mapdungeon.cityname.AddressMap
import java.util.*

data class Mission(
    val missionChar: Char,
    val id: String = UUID.randomUUID().toString(),
    var isClear: Boolean = false,
    val createdAt: Date = Date(),
    var clearedAt: Date? = null,
    var address: AddressMap? = null
) {
}