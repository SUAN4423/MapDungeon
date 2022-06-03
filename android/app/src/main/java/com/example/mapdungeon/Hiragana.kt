package com.example.mapdungeon

import com.example.mapdungeon.location.addressMap
import com.example.mapdungeon.location.locateChar
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

        public fun getCityName(): String {
            if (addressMap != null) {
                return addressMap!!["prefecture"] + addressMap!!["city"] + addressMap!!["town"]
            } else {
                return "null"
            }
        }

        public fun getFirstKana(): Char? {
            val city: String? = addressMap!!["city"]
            val cityKana: String? = addressMap!!["city-kana"]
            val town: String? = addressMap!!["town"]
            val townKana: String? = addressMap!!["town-kana"]
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
                if (firstKana!! == locateChar) {
//                    Toast.makeText(
//                        activity,
//                        "\"${locateChar}\"から始まる市区町村にたどり着きました！！！",
//                        Toast.LENGTH_LONG
//                    ).show()
//                    Hiragana.setRandomHiragana();
//                    mapsBinding.themeText.text = "今のお題は「" + locateChar + "」です"
                    return true
                } else {
//                    Toast.makeText(
//                        activity,
//                        "\"${locateChar}\"から始まる市区町村へ行ってください、頑張って！ (現在の頭文字: \"${firstKana!!}\")",
//                        Toast.LENGTH_LONG
//                    ).show()
                    return false
                }
            }
            return false
        }
    }
}