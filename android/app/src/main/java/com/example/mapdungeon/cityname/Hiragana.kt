package com.example.mapdungeon.cityname

import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.random.Random
import java.lang.Integer.max

class Hiragana {
    companion object {
        private var missionChars: MutableList<Char> = mutableListOf('あ','あ','あ','あ','あ','あ','あ','あ')
        public var addressMap: AddressMap? = null

        public fun setRandomHiragana() {
            missionChars.replaceAll { getRandomHiragana() }
        }

        private fun getRandomHiragana(): Char { // TODO: ひらがなにはあるが、それから始まる日本の都市がない場合その都市の建設(例: "ん"から始まる都市)
//            val randomInt: Int = Random.nextInt(85)
//            var retChar: Char = 'ぁ' + randomInt
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

        public fun getCurrentMission(): MutableList<Char> {
            return missionChars
        }

        public fun getCityName(): String {
            if (addressMap != null) {
                return addressMap!!.prefecture + addressMap!!.city + addressMap!!.town
            } else {
                return "null"
            }
        }

        private fun longestMatch(s: String, t: String): Pair<Int,Int> {
            //sのprefixとtの部分文字列との最長マッチ
            //2つ目の情報はマッチ数が一致したときになるべく短い文字を採用するためのもの
            val sLen = s.length
            val tLen = t.length
            var longest = 0
            for(i in 0 until tLen){
                for(j in 0 until sLen){
                    if(i+j >= tLen) break
                    if(s[j] == t[i+j]) longest = max(longest, j+1)
                    else break
                }
            }
            return Pair<Int,Int>(longest, -sLen-tLen)
//            編集距離はうまくいかなさそう
//            var dp = Array(sLen+1) { IntArray(tLen+1) }
//            for(i in 0 until sLen+1){
//                for(j in 0 until tLen+1){
//                    dp[i][j] = 998244353
//                }
//            }
//            for(i in 0 until sLen+1) dp[i][0] = i
//            for(j in 0 until tLen+1) dp[0][j] = j
//            for(i in 0 until sLen+1){
//                for(j in 0 until tLen+1){
//                    if(i > 0) dp[i][j] = min(dp[i][j], dp[i-1][j] + 1)
//                    if(j > 0) dp[i][j] = min(dp[i][j], dp[i][j-1] + 1)
//                    if(i > 0 && j > 0){
//                        var isNotSame : Int = 0
//                        if(s[i-1] != t[j-1]) isNotSame = 1
//                        dp[i][j] = min(dp[i][j], dp[i-1][j-1] + isNotSame)
//                    }
//                }
//            }
//            return dp[sLen][tLen]
        }

        public fun getFirstKana(activity: AppCompatActivity): Char? {
            var firstKana: Char? = null
            val assetManager : AssetManager = activity.resources.assets
            val file = assetManager.open("townname.csv")
            val fileReader = BufferedReader(InputStreamReader(file, "MS932"))
            var longest = Pair<Int,Int>(0,0)
            val placeString = addressMap!!.city + addressMap!!.town
            var debugString = ""
            fileReader.forEachLine {
                var column = it.split(",").toTypedArray()
                var tableString = ""
                //「支庁」、「振興局」があると厄介なので該当箇所を消す
                if(column[2].indexOf("支庁") == -1 && column[2].indexOf("振興局") == -1){
                    tableString = (column[2] + column[4]).replace("\"","")
                }else{
                    tableString = column[4].replace("\"","")
                }
                val res = longestMatch(tableString, placeString) //CSV上のprefix、APIで取ったものの部分文字列
                if(res.first > longest.first || res.first == longest.first && res.second > longest.second){
                    firstKana = column[5][1]
                    longest = res
                    debugString = tableString
                }
            }
//            Log.d("debug", "max : ${longest}")
//            Log.d("debug", "match : ${longestMatch(debugString,placeString).first}")
//            Log.d("debug", "dS : ${debugString}")
//            Log.d("debug", "pS : ${placeString}")
//            Log.d("debug", "fi : ${firstKana}")
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
        public fun checkLocation(activity: AppCompatActivity): Pair<Boolean, Char?> {
            if (addressMap != null) {
                var firstKana: Char? = null
                firstKana = getFirstKana(activity)
//                Log.d("debug", firstKana!! + " " + cityKana)
//                Toast.makeText(
//                    activity,
//                    "${firstKana!!} ${cityKana}", Toast.LENGTH_LONG
//                ).show()
                return (firstKana in missionChars) to firstKana
            }
            return false to null
        }
    }
}