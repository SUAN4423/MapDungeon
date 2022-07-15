package com.example.mapdungeon.cityname

import android.content.res.AssetManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Paths
import kotlin.random.Random
import com.opencsv.CSVReader
import java.io.InputStream

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

        public fun getFirstKana(activity: AppCompatActivity): Char? {
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
            val assetManager : AssetManager = activity.resources.assets
            val file = assetManager.open("townname.csv")
            val fileReader = BufferedReader(InputStreamReader(file, "MS932"))
            var fi: Char? = null
            fileReader.forEachLine {
                var column = it.split(",").toTypedArray()
                val det = (column[3] + column[5]).replace("\"", "")
                if((cityKana + townKana).indexOf(det) >= 0) fi = column[5][1]
//                if(det.equals(addressMap!!.city_kana + addressMap!!.town_kana)) fi = column[5][0]
//                Log.d("debug", "det: ${det}, now: ${cityKana + townKana}")
            }
            firstKana = fi
//            Log.d("debug", "fi : ${fi}")
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
        public fun checkLocation(activity: AppCompatActivity): Boolean {
            if (addressMap != null) {
                var firstKana: Char? = null
                firstKana = getFirstKana(activity)
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