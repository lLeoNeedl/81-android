package com.nile.fortu.game

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import com.nile.fortu.game.databinding.ActivityBonusGameBinding
import com.nile.fortu.game.databinding.ActivityMenuBinding
import java.util.*
import kotlin.random.Random

class BonusGameActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityBonusGameBinding.inflate(layoutInflater)
    }

    private val sector = arrayOf(
        "1000", "500", "200", "1000", "50", "100", "500", "200", "10", "200", "Jackpot", "100"
    )
    private val sectorDegrees = arrayOfNulls<Int>(sector.size)
    private var degree = 0
    private var score = 0

    private var isSpinning = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getDegreeFromSectors()

        binding.tvBalance.text = Util.balance.toString()
        binding.tvScore.text = score.toString()

        binding.flSpin.setOnClickListener {
            if (!isSpinning) {
                spin()
                isSpinning = true
            }
        }

        binding.btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun spin() {
        degree = Random.nextInt(sector.size - 1)
        val sectorDegree = sectorDegrees[degree]?.toFloat() ?: return

        val rotateAnimation = RotateAnimation(
            0F, ((360 * sector.size) + sectorDegree),
            RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )

        rotateAnimation.duration = 3600
        rotateAnimation.fillAfter = true
        rotateAnimation.interpolator = DecelerateInterpolator()
        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation?) {
                changeButtonState(false)
            }

            override fun onAnimationEnd(animation: Animation?) {
                Toast.makeText(
                    this@BonusGameActivity, "You have got " +
                            sector[sector.size - (degree + 1)] + " point", Toast.LENGTH_SHORT
                ).show()
                isSpinning = false

                if (sector[sector.size - (degree + 1)] == "Jackpot") {
                    saveEarnings(2000)
                } else {
                    saveEarnings(sector[sector.size - (degree + 1)].toInt())
                }
                changeButtonState(true)
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })

        binding.ivCircle.startAnimation(rotateAnimation)
    }

    fun changeButtonState(enabled: Boolean) {
        binding.flSpin.isClickable = enabled
        binding.flSpin.isFocusable = enabled
    }

    private fun saveEarnings(earned: Int) {
        Util.balance = Util.balance + earned
        binding.tvBalance.text = Util.balance.toString()
        score = earned
        binding.tvScore.text = score.toString()
    }

    private fun getDegreeFromSectors() {
        val sectorDegree = 360 / sector.size
        for (i in sector.indices) {
            sectorDegrees[i] = (i + 1) * sectorDegree
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, BonusGameActivity::class.java)
    }
}