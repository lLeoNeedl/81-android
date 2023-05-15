package com.nile.fortu.game

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.nile.fortu.game.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity(), MenuFragment.OnReturnButtonPressed {

    private val binding by lazy {
        ActivityMenuBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onReturnButtonPressed() {
        finish()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MenuActivity::class.java)
    }
}