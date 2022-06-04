package com.example.mapdungeon.cityname

import kotlin.random.Random

class Hiragana {
    companion object {
        private var locateChar: Char = 'あ'
        public var addressMap: AddressMap? = null

        public fun setRandomHiragana() {
            locateChar = getRandomHiragana()
        }

        private fun getRandomHiragana(): Char { // TODO: ひらがなにはあるが、それから始まる日本の都市がない場合その都市の建設(例: "ん"から始まる都市)
//            val randomInt: Int = Random.nextInt(85)
//            var retChar: Char = 'ぁ' + randomInt
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

        public fun getNowMission(): Char {
            return locateChar
        }

        public fun getCityName(): String {
            if (addressMap != null) {
                return addressMap!!.prefecture + addressMap!!.city + addressMap!!.town
            } else {
                return "null"
            }
        }

        public fun getFirstKana(): Char? {
            val city: String? = addressMap!!.city
            val cityKana: String? = addressMap!!.city_kana
            val town: String? = addressMap!!.town
            val townKana: String? = addressMap!!.town_kana
            var firstKana: Char? = null
            if (city != null && cityKana != null && town != null && townKana != null) {
                if (city == "郡上市" || city == "蒲郡市") { //○○郡××としたいときの例外処理
                    firstKana = cityKana[0]
                } else if (city.indexOf("郡") >= 0) { //○○郡××
                    val indexOfGUN = cityKana.indexOf("ぐん") + 2
                    firstKana = cityKana[indexOfGUN]
                } else { //○○市
                    firstKana = cityKana[0]
                }
            }
            when (firstKana) {
                'が' -> firstKana = 'か'
                'ぎ' -> firstKana = 'き'
                'ぐ' -> firstKana = 'く'
                'げ' -> firstKana = 'け'
                'ご' -> firstKana = 'こ'
                'ざ' -> firstKana = 'さ'
                'じ' -> firstKana = 'し'
                'ず' -> firstKana = 'す'
                'ぜ' -> firstKana = 'せ'
                'ぞ' -> firstKana = 'そ'
                'だ' -> firstKana = 'た'
                'ぢ' -> firstKana = 'ち'
                'づ' -> firstKana = 'つ'
                'で' -> firstKana = 'て'
                'ど' -> firstKana = 'と'
                'ば', 'ぱ' -> firstKana = 'は'
                'び', 'ぴ' -> firstKana = 'ひ'
                'ぶ', 'ぷ' -> firstKana = 'ふ'
                'べ', 'ぺ' -> firstKana = 'へ'
                'ぼ', 'ぽ' -> firstKana = 'ほ'
            }
            return firstKana
        }

        //        public fun checkLocation(activity: Activity, mapsBinding: ActivityMapsBinding):Boolean {
        public fun checkLocation(): Boolean {
            if (addressMap != null) {
                var firstKana: Char? = null
                firstKana = getFirstKana()
//                Log.d("debug", firstKana!! + " " + cityKana)
//                Toast.makeText(
//                    activity,
//                    "${firstKana!!} ${cityKana}", Toast.LENGTH_LONG
//                ).show()
                return firstKana!! == locateChar
            }
            return false
        }
    }
}