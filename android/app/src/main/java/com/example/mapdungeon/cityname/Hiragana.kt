package com.example.mapdungeon.cityname


import kotlin.random.Random


class Hiragana {
    companion object {


        fun setRandomHiragana(): MutableList<Char> {
            val chars = mutableListOf<Char>()
            while (chars.size < 8) {
                val selectedChar = getRandomHiragana()
                var isDistinct = true
                chars.forEach({
                    if (it == selectedChar) isDistinct = false
                })
                if (isDistinct) chars.add(selectedChar)
            }
            val missionChars = chars.shuffled().toMutableList()
//            isClearList.replaceAll { false }
            return missionChars
        }

        private fun getRandomHiragana(): Char { // TODO: ひらがなにはあるが、それから始まる日本の都市がない場合その都市の建設(例: "ん"から始まる都市)

            var retChar: Char = '*'
            var randomSum: Int = 0
            HiraganaData.hiraganaPercent.forEach {
                randomSum += it.second
            }
            val randomInt: Int = Random.nextInt(randomSum)
            var sum: Int = 0
            HiraganaData.hiraganaPercent.forEach {
                if (retChar == '*') {
                    sum += it.second
                    if (randomInt < sum) {
                        retChar = it.first
                    }
                }
            }
            return retChar
        }
    }
}