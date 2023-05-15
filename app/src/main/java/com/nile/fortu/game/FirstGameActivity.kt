package com.nile.fortu.game

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FirstGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_game)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, FirstGameActivity::class.java)
    }
}