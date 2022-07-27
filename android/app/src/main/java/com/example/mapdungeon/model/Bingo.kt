package com.example.mapdungeon.model

import com.example.mapdungeon.cityname.Hiragana
import java.util.*

data class Bingo(
    var missions: Array<Mission>,
    val id: String = UUID.randomUUID().toString(),
    val createdAt: Date = Date(),
    var isClear: Boolean = false,
    var clearedAt: Date? = null
) {
    fun getClearList(): List<Boolean> {
        return missions.map { it.isClear }
//        return List(8){missions[it].isClear}
    }
}

fun genBingo(): Bingo {
    Hiragana.setRandomHiragana()
    val missionChars = Hiragana.getCurrentMission()
    return Bingo(Array(8) { Mission(missionChars[it]) })
}