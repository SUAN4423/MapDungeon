package com.example.mapdungeon.model

import com.example.mapdungeon.cityname.getRandomHiragana

class Mission{
    var isCompleted = false
        private set

    val firstKana = getRandomHiragana()
    fun updateIsCompleted(address: Address): Boolean {
        if (address.firstKana != firstKana) {
            return false
        }
        isCompleted = true
        return true
    }

}