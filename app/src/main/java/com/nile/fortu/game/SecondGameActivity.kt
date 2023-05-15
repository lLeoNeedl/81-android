package com.nile.fortu.game

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SecondGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_game)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SecondGameActivity::class.java)
    }
}