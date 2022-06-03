package com.example.mapdungeon.judge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mapdungeon.Hiragana
import com.example.mapdungeon.databinding.ActivityJudgeBinding
import com.example.mapdungeon.locateChar

class JudgeActivity : AppCompatActivity() {
    private lateinit var judgeBinding: ActivityJudgeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        judgeBinding = ActivityJudgeBinding.inflate(layoutInflater)
        val view = judgeBinding.root
        setContentView(view)

        judgeBinding.judgeText.text = "確認中"
        judgeBinding.cityText.text = Hiragana.getCityName()

        val successCity: Boolean = Hiragana.checkLocation()

        if (successCity)
            judgeBinding.judgeText.text = "「${locateChar}」の付く市区町村に\n到着しました！"
        else if(Hiragana.getFirstKana() != null)
            judgeBinding.judgeText.text =
                "「${locateChar}」の付く市区町村に\n到着していません\n現在の頭文字: ${Hiragana.getFirstKana()!!}"
        else
            judgeBinding.judgeText.text =
                "現在位置が取得されていません"

        judgeBinding.returnButton.setOnClickListener {
            finish()
        }
    }
}