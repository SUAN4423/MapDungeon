package com.example.mapdungeon.location

var isUsed: Boolean = true; // NOTE: 位置判定が行われたらtrueに変更。標準では判定が行われないよう最初からtrueに設定
var addressMap: HashMap<String, String>? = null // NOTE: <String, String>は<種類, その内容>です

const val EXTRA_LATITUDE = "com.example.location.EXTRA_LATITUDE"
const val EXTRA_LONGITUDE = "com.example.location.EXTRA_LONGITUDE"
