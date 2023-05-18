package com.nile.fortu.game

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import com.nile.fortu.game.databinding.ActivityFirstGameBinding
import com.nile.fortu.game.slotImagesScroll.EventEnd
import com.nile.fortu.game.slotImagesScroll.Utils
import kotlin.random.Random

class FirstGameActivity : AppCompatActivity(), EventEnd {

    private var countDown = 0
    private var currentBet = 0
    private var score = 0

    private val binding by lazy {
        ActivityFirstGameBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        score = savedInstanceState?.getInt("score") ?: 25
        Log.d("TAG", score.toString())
        currentBet = savedInstanceState?.getInt("bet") ?: 25
        Log.d("TAG", currentBet.toString())

        binding.tvBet.text = currentBet.toString()
        binding.tvBalance.text = Utils.balance.toString()
        binding.tvScore.text = score.toString()

        binding.slot1.eventEnd = this
        binding.slot2.eventEnd = this
        binding.slot3.eventEnd = this


        binding.flSpin.setOnClickListener {
            if (currentBet <= Utils.balance) {
                binding.slot1.setRandomValue(Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5)
                binding.slot2.setRandomValue(Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5)
                binding.slot3.setRandomValue(Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5)
            } else {
                Toast.makeText(this, "You don't have enough money", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnIncrease.setOnClickListener {
            increaseBet()
        }

        binding.btnDecrease.setOnClickListener {
            if (currentBet != 0) {
                decreaseBet()
            }
        }

        binding.btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun increaseBet() {
        currentBet += 100
        binding.tvBet.text = currentBet.toString()
    }

    private fun decreaseBet() {
        currentBet -= 100
        binding.tvBet.text = currentBet.toString()
    }

    override fun eventEnd(result: Int, bet: Int) {
        if (countDown < 2) {
            countDown++
        } else {
            countDown = 0

            if (binding.slot1.value == binding.slot2.value && binding.slot2.value == binding.slot3.value) {
                Toast.makeText(this, "YOU WON!!!", Toast.LENGTH_SHORT).show()
                score = currentBet * 2
                Utils.balance += score
                binding.tvBalance.text = Utils.balance.toString()
            } else if (binding.slot1.value == binding.slot2.value || binding.slot2.value == binding.slot3.value || binding.slot1.value == binding.slot3.value) {
                Toast.makeText(this, "You did good.", Toast.LENGTH_SHORT).show()
                score = currentBet
                Utils.balance += score
                binding.tvBalance.text = Utils.balance.toString()
            } else {
                Toast.makeText(this, "You lost. Better luck next time.", Toast.LENGTH_SHORT).show()
                Utils.balance -= currentBet
                binding.tvBalance.text = Utils.balance.toString()
            }
        }
        binding.tvScore.text = score.toString()
    }

    override fun changeButtonState(enabled: Boolean) {
        binding.flSpin.isClickable = enabled
        binding.flSpin.isFocusable = enabled
    }

    override fun lockOrientationChange() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
    }

    override fun unlockOrientationChange() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt("score", score)
        outState.putInt("bet", currentBet)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, FirstGameActivity::class.java)
    }
}