package com.nile.fortu.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.nile.fortu.game.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMenuBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    companion object {
        const val CHOOSE_GAME_FRAGMENT_TAG = "choose_game"
        const val SETTINGS_FRAGMENT_TAG = "settings"
    }
}