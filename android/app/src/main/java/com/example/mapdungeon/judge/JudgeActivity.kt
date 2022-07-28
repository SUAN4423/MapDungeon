package com.example.mapdungeon.judge

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mapdungeon.cityname.Hiragana
import com.example.mapdungeon.databinding.ActivityJudgeBinding
import com.example.mapdungeon.global.GlobalData
import com.example.mapdungeon.location.*
import com.example.mapdungeon.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class JudgeActivity : AppCompatActivity() {
    private lateinit var judgeBinding: ActivityJudgeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        judgeBinding = ActivityJudgeBinding.inflate(layoutInflater)
        val view = judgeBinding.root
        setContentView(view)

        val latitude = intent.getDoubleExtra(EXTRA_LATITUDE, 0.0)
        val longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0)

        judgeBinding.judgeText.text = "確認中"
        judgeBinding.cityText.text = ""


        lifecycleScope.launch {
            when (val res = AddressAPIRepository().getAddress(latitude,longitude)) {
                is Result.Success -> {
                    val address = res.data
                    Log.d("debug", address.str)

                    val firstKana: Char? = res.data.getFirstKana(this@JudgeActivity.resources.assets)
                    val missionChars = GlobalData.bingo.getMissionCharsList()
                    val clearIndex = missionChars.indexOf(firstKana)
                    if (clearIndex >= 0) {
                        GlobalData.bingo.missions[clearIndex].isClear = true
                        GlobalData.bingo.missions[clearIndex].clearedAt = Date()
                        GlobalData.bingo.missions[clearIndex].address = address.copy()
                    }

                    judgeBinding.judgeText.text =
                        if (firstKana in missionChars) {
                            "「${firstKana}」から始まる\n市区町村に\n到着しました！"
                        } else {
                            "お題に沿った市区町村に\n" +
                                    "到着していません\n" +
                                    "現在の頭文字: $firstKana"
                        }

                    judgeBinding.cityText.text = address.str ?: "住所不明"
                }
                else -> {
                    judgeBinding.judgeText.text = "住所取得失敗"
                }
            }
        }

        judgeBinding.returnButton.setOnClickListener {
            finish()
        }
    }
}