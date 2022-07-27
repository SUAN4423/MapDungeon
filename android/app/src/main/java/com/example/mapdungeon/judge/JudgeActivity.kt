package com.example.mapdungeon.judge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mapdungeon.MissionViewModel
import com.example.mapdungeon.cityname.missionFirstKana
import com.example.mapdungeon.databinding.ActivityJudgeBinding
import com.example.mapdungeon.location.*

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
//        judgeBinding.cityText.text = Hiragana.getCityName()
//
//        val successCity: Boolean = Hiragana.checkLocation()
//
//        if (successCity)
//            judgeBinding.judgeText.text = "「${locateChar}」の付く市区町村に\n到着しました！"
//        else if(Hiragana.getFirstKana() != null)
//            judgeBinding.judgeText.text =
//                "「${locateChar}」の付く市区町村に\n到着していません\n現在の頭文字: ${Hiragana.getFirstKana()!!}"
//        else
//            judgeBinding.judgeText.text =
//                "現在位置が取得されていません"

        val model = ViewModelProvider(this)[MissionViewModel::class.java]
        val ok = model.judge(longitude, latitude)

            if (result!!.getBinding() != null) { // NOTE: 画面更新処理
                if (result.getBinding() is ActivityJudgeBinding) { // NOTE: JudgeActivityの画面更新処理
                    judgeBinding.judgeText.text =
                        if (address?.firstKana == missionFirstKana) {
                            // TODO: use ViewModel
                            "「${missionFirstKana}」から始まる\n市区町村に\n到着しました！"
                        } else {
                            // TODO: use ViewModel
                            "「${missionFirstKana}」から始まる\n市区町村に\n到着していません\n現在の頭文字: ${address?.firstKana ?: '?'}"
                        }

                    judgeBinding.cityText.text = address?.str ?: "住所不明"
                }
            }

        judgeBinding.returnButton.setOnClickListener {
            finish()
        }
    }
}