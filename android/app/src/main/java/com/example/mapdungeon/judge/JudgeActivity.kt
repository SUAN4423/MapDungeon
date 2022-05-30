package com.example.mapdungeon.judge

import android.app.Activity
import android.os.Bundle
import com.example.mapdungeon.databinding.ActivityJudgeBinding

class JudgeActivity: Activity() {
    private lateinit var judgeBinding: ActivityJudgeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        judgeBinding = ActivityJudgeBinding.inflate(layoutInflater)
        val view = judgeBinding.root
        setContentView(view)
    }
}