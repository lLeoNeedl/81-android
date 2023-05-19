package com.nile.fortu.game

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.nile.fortu.game.databinding.ActivityFirstGameBinding
import com.nile.fortu.game.slotImagesScroll.SlotItem
import com.nile.fortu.game.slotImagesScroll.Utils
import kotlin.random.Random

class FirstGameActivity : AppCompatActivity() {

    private var countDown = 0
    private var currentBet = 0
    private var score = 0

    val slot1= SlotItem()
    val slot2 = SlotItem()
    val slot3 = SlotItem()

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
            sl1NextImage.translationY = binding.flSlot1.height.toFloat()
            sl1CurrentImage.setImageResource(R.drawable.j_image)
            sl2NextImage.translationY = binding.flSlot2.height.toFloat()
            sl2CurrentImage.setImageResource(R.drawable.j_image)
            sl3NextImage.translationY = binding.flSlot3.height.toFloat()
            sl3CurrentImage.setImageResource(R.drawable.j_image)
        }

        binding.flSpin.setOnClickListener {
            if (currentBet <= Utils.balance) {
                setRandomValue(
                    binding.sl1CurrentImage,
                    binding.sl1NextImage,
                    binding.flSlot1.height,
                    Random.nextInt(6),
                    Random.nextInt(15 - 5 + 1) + 5,
                    slot1
                )
                setRandomValue(
                    binding.sl2CurrentImage,
                    binding.sl2NextImage,
                    binding.flSlot2.height,
                    Random.nextInt(6),
                    Random.nextInt(15 - 5 + 1) + 5,
                    slot2
                )
                setRandomValue(
                    binding.sl3CurrentImage,
                    binding.sl3NextImage,
                    binding.flSlot3.height,
                    Random.nextInt(6),
                    Random.nextInt(15 - 5 + 1) + 5,
                    slot3
                )
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

    fun eventEnd() {
        if (countDown < 2) {
            countDown++
        } else {
            countDown = 0

            if ((binding.sl1NextImage.tag as Int) == (binding.sl2NextImage.tag as Int) && (binding.sl2NextImage.tag as Int) == (binding.sl3NextImage.tag as Int)) {
                Toast.makeText(this, "YOU WON!!!", Toast.LENGTH_SHORT).show()
                score = currentBet * 2
                Utils.balance += score
                binding.tvBalance.text = Utils.balance.toString()
            } else if ((binding.sl1NextImage.tag as Int) == (binding.sl2NextImage.tag as Int) || (binding.sl2NextImage.tag as Int) == (binding.sl3NextImage.tag as Int) || (binding.sl1NextImage.tag as Int) == (binding.sl3NextImage.tag as Int)) {
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

    fun changeButtonState(enabled: Boolean) {
        binding.flSpin.isClickable = enabled
        binding.flSpin.isFocusable = enabled
    }

    fun lockOrientationChange() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
    }

    fun unlockOrientationChange() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }

    fun setRandomValue(currentImage: ImageView, nextImage: ImageView, height: Int, image: Int, numRoll: Int, slot: SlotItem) {
        currentImage.visibility = View.VISIBLE
        currentImage.animate()
            .translationY(-height.toFloat())
            .setDuration(ANIMATION_DURATION).start()

        nextImage.translationY = nextImage.height.toFloat()
        nextImage.animate()
            .translationY(0f).setDuration(ANIMATION_DURATION)
            .setListener(object : Animator.AnimatorListener {

                override fun onAnimationRepeat(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    currentImage.visibility = View.GONE
                    setImage(currentImage, slot.oldValue%6, slot)
                    currentImage.translationY = 0f
                    if(slot.oldValue != numRoll) {
                        setRandomValue(currentImage, nextImage, height, image, numRoll, slot)
                        slot.oldValue++
                    }
                    else {
                        slot.lastResult = 0
                        slot.oldValue = 0
                        setImage(nextImage, image, slot)
                        eventEnd()
                        changeButtonState(true)
                        unlockOrientationChange()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationStart(animation: Animator) {
                    changeButtonState(false)
                    lockOrientationChange()
                }

            }).start()
    }

    private fun setImage(currentImage: ImageView, value: Int, slot: SlotItem) {
        when (value) {
            Utils.nineImage -> currentImage.setImageResource(R.drawable.nine_image)
            Utils.jImage -> currentImage.setImageResource(R.drawable.j_image)
            Utils.kImage -> currentImage.setImageResource(R.drawable.k_image)
            Utils.aImage -> currentImage.setImageResource(R.drawable.a_image)
            Utils.runeImage -> currentImage.setImageResource(R.drawable.rune_image)
            Utils.wildImage -> currentImage.setImageResource(R.drawable.wild_image)
        }

        currentImage.tag = value
        slot.lastResult = value
    }

    companion object {

        private const val ANIMATION_DURATION = 250L
        fun newIntent(context: Context) = Intent(context, FirstGameActivity::class.java)
    }
}