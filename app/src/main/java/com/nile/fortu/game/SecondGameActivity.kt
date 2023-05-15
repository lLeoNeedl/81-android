package com.nile.fortu.game

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nile.fortu.game.databinding.ActivityFirstGameBinding
import com.nile.fortu.game.databinding.ActivitySecondGameBinding

class SecondGameActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySecondGameBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnReturn.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SecondGameActivity::class.java)
    }
}