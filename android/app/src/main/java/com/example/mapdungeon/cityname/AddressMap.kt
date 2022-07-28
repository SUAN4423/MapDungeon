package com.example.mapdungeon.cityname

import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader

data class AddressMap(
    val prefecture: String,
    val city: String,
    val city_kana: String,
    val town: String,
    val town_kana: String,
    val postal: String
) {

    val str get() = this.prefecture + this.city + this.town

    fun getFirstKana(assetManager: AssetManager): Char? {

        var firstKana: Char? = null

        val file = assetManager.open("townname.csv")
        val fileReader = BufferedReader(InputStreamReader(file, "MS932"))
        var longest = Pair(0, 0)
        val placeString = this.city + this.town
        var debugString = ""
        fileReader.forEachLine {
            var column = it.split(",").toTypedArray()
            var tableString = ""
            //「支庁」、「振興局」があると厄介なので該当箇所を消す
            if (column[2].indexOf("支庁") == -1 && column[2].indexOf("振興局") == -1) {
                tableString = (column[2] + column[4]).replace("\"", "")
            } else {
                tableString = column[4].replace("\"", "")
            }
            val res =
                longestMatch(tableString, placeString) //CSV上のprefix、APIで取ったものの部分文字列
            if (res.first > longest.first || res.first == longest.first && res.second > longest.second) {
                firstKana = column[5][1]
                longest = res
                debugString = tableString
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

    private fun longestMatch(s: String, t: String): Pair<Int, Int> {
        //sのprefixとtの部分文字列との最長マッチ
        //2つ目の情報はマッチ数が一致したときになるべく短い文字を採用するためのもの
        val sLen = s.length
        val tLen = t.length
        var longest = 0
        for (i in 0 until tLen) {
            for (j in 0 until sLen) {
                if (i + j >= tLen) break
                if (s[j] == t[i + j]) longest = Integer.max(longest, j + 1)
                else break
            }
        }
        return Pair(longest, -sLen - tLen)
    }
}
