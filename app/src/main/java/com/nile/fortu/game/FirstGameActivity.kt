package com.nile.fortu.game

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nile.fortu.game.databinding.ActivityFirstGameBinding
import com.nile.fortu.game.slotImagesScroll.SlotItem
import com.nile.fortu.game.slotImagesScroll.Utils
import kotlin.random.Random

class FirstGameActivity : AppCompatActivity() {

    private var countDown = 0
    private var currentBet = 0
    private var score = 0

    private lateinit var slots: List<SlotItem>

    private val viewModel by lazy {
        ViewModelProvider(this)[FirstGameViewModel::class.java]
    }

    private val binding by lazy {
        ActivityFirstGameBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val listOfViews = listOf(binding.flSlot1, binding.flSlot2, binding.flSlot3)
        listOfViews.forEachIndexed { index, frameLayout ->
            viewModel.createItemFromView(index, frameLayout)
        }

        binding.tvBet.text = currentBet.toString()
        binding.tvBalance.text = Utils.balance.toString()
        binding.tvScore.text = score.toString()

        viewModel.slotList.observe(this) {
            slots = it
            binding.run {
                sl1CurrentImage.setImageResource(it[0].currentImageId ?: R.drawable.j_image)
                sl1NextImage.setImageResource(it[0].currentImageId ?: R.drawable.j_image)
                sl2CurrentImage.setImageResource(it[1].currentImageId ?: R.drawable.j_image)
                sl2NextImage.setImageResource(it[1].currentImageId ?: R.drawable.j_image)
                sl3CurrentImage.setImageResource(it[2].currentImageId ?: R.drawable.j_image)
                sl3NextImage.setImageResource(it[2].currentImageId ?: R.drawable.j_image)
            }
        }

        binding.flSpin.setOnClickListener {
            if (currentBet <= Utils.balance) {
                slots.forEach {
                    setRandomValue(
                        it,
                        Random.nextInt(6),
                        Random.nextInt(15 - 5 + 1) + 5
                    )
                }
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
            if (slots[0]!!.currentImageId == slots[1]!!.currentImageId && slots[1]!!.currentImageId == slots[2]!!.currentImageId) {
                Toast.makeText(this, "YOU WON!!!", Toast.LENGTH_SHORT).show()
                score = currentBet * 2
                Utils.balance += score
                binding.tvBalance.text = Utils.balance.toString()
            } else if (slots[0]!!.currentImageId == slots[1]!!.currentImageId || slots[1]!!.currentImageId == slots[2]!!.currentImageId || slots[0]!!.currentImageId == slots[2]!!.currentImageId) {
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

    fun setRandomValue(slot: SlotItem, image: Int, numRoll: Int) {
        slot.currentImage.visibility = View.VISIBLE
        slot.currentImage.animate()
            .translationY(-(binding.flSlot1.height.toFloat()))
            .setDuration(ANIMATION_DURATION).start()

        slot.nextImage.translationY = slot.nextImage.height.toFloat()

        slot.nextImage.animate()
            .translationY(0f).setDuration(ANIMATION_DURATION)
            .setListener(object : Animator.AnimatorListener {

                override fun onAnimationRepeat(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    slot.currentImage.visibility = View.GONE
                    setImage(slot.oldValue % 6, slot)
                    slot.currentImage.translationY = 0f
                    if (slot.oldValue != numRoll) {
                        setRandomValue(slot, image, numRoll)
                        slot.oldValue++
                    } else {
                        slot.oldValue = 0
                        setImage(image, slot)
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

    private fun setImage(value: Int, slot: SlotItem) {
        when (value) {
            Utils.nineImage -> viewModel.updateImageIdInItem(slot, R.drawable.nine_image)
            Utils.jImage -> viewModel.updateImageIdInItem(slot, R.drawable.j_image)
            Utils.kImage -> viewModel.updateImageIdInItem(slot,R.drawable.k_image)
            Utils.aImage -> viewModel.updateImageIdInItem(slot, R.drawable.a_image)
            Utils.runeImage -> viewModel.updateImageIdInItem(slot, R.drawable.rune_image)
            Utils.wildImage -> viewModel.updateImageIdInItem(slot, R.drawable.wild_image)
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 250L
        fun newIntent(context: Context) = Intent(context, FirstGameActivity::class.java)
    }
}