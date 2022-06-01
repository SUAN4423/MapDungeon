package com.example.mapdungeon

import kotlin.random.Random

class Hiragana {
    companion object {
        public fun setRandomHiragana() {
            locateChar = getRandomHiragana()
        }

        private fun getRandomHiragana(): Char { // TODO: ひらがなにはあるが、それから始まる日本の都市がない場合その都市の建設(例: "ん"から始まる都市)
            val randomInt: Int = Random.nextInt(85)
            var retChar: Char = 'ぁ' + randomInt
            return retChar
        }
    }
}