package com.example.mapdungeon.cityname

import com.example.mapdungeon.model.Topofhiragana
import kotlin.random.Random

fun getRandomHiragana(): Char { // TODO: ひらがなにはあるが、それから始まる日本の都市がない場合その都市の建設(例: "ん"から始まる都市)
    var retChar: Char = '*'
    var randomSum: Int = 0
    Topofhiragana.hiraganaPercent.forEach {
        randomSum += it.second
    }
    val randomInt: Int = Random.nextInt(randomSum)
    var sum: Int = 0
    Topofhiragana.hiraganaPercent.forEach {
        if (retChar == '*') {
            sum += it.second
            if (randomInt < sum) {
                retChar = it.first
            }
        }
    }
    return retChar
}

// TODO: ViewModelなどに状態をもちたい.
var missionFirstKana = getRandomHiragana()