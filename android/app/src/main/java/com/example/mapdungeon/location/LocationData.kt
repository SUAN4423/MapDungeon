package com.example.mapdungeon.location

var x: Double = 0.0
var y: Double = 0.0
var locationName: String? = null
var isUsed: Boolean = true; // NOTE: 位置判定が行われたらtrueに変更。標準では判定が行われないよう最初からtrueに設定
var addressMap: HashMap<String, String>? = null // NOTE: <String, String>は<種類, その内容>です
var locateChar: Char = 'あ'