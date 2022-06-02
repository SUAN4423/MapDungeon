package com.example.mapdungeon.judge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mapdungeon.databinding.ActivityJudgeBinding

class JudgeActivity: AppCompatActivity() {
    private lateinit var judgeBinding: ActivityJudgeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        judgeBinding = ActivityJudgeBinding.inflate(layoutInflater)
        val view = judgeBinding.root
        setContentView(view)

        judgeBinding.returnButton.setOnClickListener {
            finish()
        }
    }
}