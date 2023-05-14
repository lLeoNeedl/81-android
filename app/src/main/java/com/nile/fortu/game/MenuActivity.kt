package com.nile.fortu.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.nile.fortu.game.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity(), OnFragmentAttached {

    private val binding by lazy {
        ActivityMenuBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    override fun updateScreen(fragmentTag: String) {
        when (fragmentTag) {
            CHOOSE_GAME_FRAGMENT_TAG -> {
                binding.tvReturnToMenu.visibility = View.VISIBLE
                binding.btnReturn.visibility = View.INVISIBLE
                binding.btnGift.visibility = View.VISIBLE
            }
            SETTINGS_FRAGMENT_TAG -> {
                binding.tvSettingsHeader.visibility = View.VISIBLE
                binding.btnLogout.visibility = View.INVISIBLE
            }
        }
    }

    override fun restoreViews() {
        binding.tvSettingsHeader.visibility = View.INVISIBLE
        binding.tvReturnToMenu.visibility = View.INVISIBLE
        binding.btnGift.visibility = View.INVISIBLE
        binding.btnReturn.visibility = View.VISIBLE
        binding.btnLogout.visibility = View.VISIBLE
    }

    companion object {
        const val CHOOSE_GAME_FRAGMENT_TAG = "choose_game"
        const val SETTINGS_FRAGMENT_TAG = "settings"
    }
}