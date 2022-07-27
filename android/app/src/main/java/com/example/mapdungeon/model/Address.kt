package com.example.mapdungeon.model

data class Address(
    val prefecture: String,
    val city: String,
    val cityKana: String,
    val town: String,
    val town_kana: String,
    val postal: String
) {
    val str get() = this.prefecture + this.city + this.town

    val firstKana: Char
        get() {
            val first = if (city == "郡上市" || city == "蒲郡市") { //○○郡××としたいときの例外処理
                cityKana[0]
            } else if (city.indexOf("郡") >= 0) { //○○郡××
                val indexOfGUN = cityKana.indexOf("ぐん") + 2
                cityKana[indexOfGUN]
            } else { //○○市
                cityKana[0]
            }

            return when (first) {
                'が' -> 'か'
                'ぎ' -> 'き'
                'ぐ' -> 'く'
                'げ' -> 'け'
                'ご' -> 'こ'
                'ざ' -> 'さ'
                'じ' -> 'し'
                'ず' -> 'す'
                'ぜ' -> 'せ'
                'ぞ' -> 'そ'
                'だ' -> 'た'
                'ぢ' -> 'ち'
                'づ' -> 'つ'
                'で' -> 'て'
                'ど' -> 'と'
                'ば', 'ぱ' -> 'は'
                'び', 'ぴ' -> 'ひ'
                'ぶ', 'ぷ' -> 'ふ'
                'べ', 'ぺ' -> 'へ'
                'ぼ', 'ぽ' -> 'ほ'
                else -> first
            }
        }
}
