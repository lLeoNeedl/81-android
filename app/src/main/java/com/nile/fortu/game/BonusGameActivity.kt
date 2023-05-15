package com.nile.fortu.game

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nile.fortu.game.databinding.ActivityBonusGameBinding
import com.nile.fortu.game.databinding.ActivityMenuBinding

class BonusGameActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityBonusGameBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnReturn.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, BonusGameActivity::class.java)
    }
}