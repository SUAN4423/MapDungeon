package com.example.mapdungeon

import android.app.Activity
import android.os.Bundle
import com.example.mapdungeon.databinding.ActivityMenuBinding

class MenuActivity : Activity() {
    private lateinit var menuBinding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuBinding = ActivityMenuBinding.inflate(layoutInflater)
        val view = menuBinding.root
        setContentView(view)
    }
}