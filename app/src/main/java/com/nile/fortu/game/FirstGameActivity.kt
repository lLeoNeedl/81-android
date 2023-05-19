package com.nile.fortu.game

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.nile.fortu.game.databinding.ActivityFirstGameBinding
import com.nile.fortu.game.slotImagesScroll.EventEnd
import com.nile.fortu.game.slotImagesScroll.Utils
import kotlin.random.Random

class FirstGameActivity : AppCompatActivity() {

    private var countDown = 0
    private var currentBet = 0
    private var score = 0

    lateinit var eventEnd: EventEnd
    var lastResult = 0
    var oldValue = 0


    val value: Int
        get() = nextImage.tag as Int

    private val binding by lazy {
        ActivityFirstGameBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.tvBet.text = currentBet.toString()
        binding.tvBalance.text = Utils.balance.toString()
        binding.tvScore.text = score.toString()

        binding.run {
            sl1NextImage.translationY = binding.flSlot1.height.toFloat() ?: 0F
            sl1CurrentImage.setImageResource(R.drawable.j_image)
            sl2NextImage.translationY = binding.flSlot2.height.toFloat() ?: 0F
            sl2CurrentImage.setImageResource(R.drawable.j_image)
            sl3NextImage.translationY = binding.flSlot3.height.toFloat() ?: 0F
            sl3CurrentImage.setImageResource(R.drawable.j_image)
        }

        binding.flSpin.setOnClickListener {
            if (currentBet <= Utils.balance) {
                setRandomValue(binding.flSlot1, Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5)
                setRandomValue(binding.flSlot2, Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5)
                setRandomValue(binding.flSlot3, Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5)
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

    fun eventEnd(result: Int, bet: Int) {
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

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.slot_image_scrol, this)
        nextImage.translationY = height.toFloat()
        currentImage.setImageResource(R.drawable.j_image)
    }

    fun setRandomValue(layout: FrameLayout, image: Int, numRoll: Int) {
        currentImage.visibility = View.VISIBLE
        currentImage.animate()
            .translationY(-height.toFloat())
            .setDuration(SlotScroll.ANIMATION_DURATION).start()

        nextImage.translationY = nextImage.height.toFloat()
        nextImage.animate()
            .translationY(0f).setDuration(SlotScroll.ANIMATION_DURATION)
            .setListener(object : Animator.AnimatorListener {

                override fun onAnimationRepeat(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    currentImage.visibility = View.GONE
                    setImage(currentImage, oldValue%6)
                    currentImage.translationY = 0f
                    if(oldValue != numRoll) {
                        setRandomValue(image,numRoll)
                        oldValue++
                    }
                    else {
                        lastResult = 0
                        oldValue = 0
                        setImage(nextImage, image)
                        eventEnd.eventEnd(image%6, numRoll)
                        eventEnd.changeButtonState(true)
                        eventEnd.unlockOrientationChange()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationStart(animation: Animator) {
                    eventEnd.changeButtonState(false)
                    eventEnd.lockOrientationChange()
                }

            }).start()
    }

    private fun setImage(currentImage: ImageView, value: Int) {
        when (value) {
            Utils.nineImage -> currentImage.setImageResource(R.drawable.nine_image)
            Utils.jImage -> currentImage.setImageResource(R.drawable.j_image)
            Utils.kImage -> currentImage.setImageResource(R.drawable.k_image)
            Utils.aImage -> currentImage.setImageResource(R.drawable.a_image)
            Utils.runeImage -> currentImage.setImageResource(R.drawable.rune_image)
            Utils.wildImage -> currentImage.setImageResource(R.drawable.wild_image)
        }

        currentImage.tag = value
        lastResult = value
    }

    companion object {

        private const val ANIMATION_DURATION = 250L

        fun newIntent(context: Context) = Intent(context, FirstGameActivity::class.java)
    }
}