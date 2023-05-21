package com.nile.fortu.game

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.nile.fortu.game.databinding.ActivityFirstGameBinding
import com.nile.fortu.game.slotImagesScroll.SlotItem
import com.nile.fortu.game.slotImagesScroll.Utils
import kotlin.random.Random

class FirstGameActivity : AppCompatActivity() {

    private var countDown = 0
    private var currentBet = 0

    private lateinit var slots: List<SlotItem>

    private val viewModel by lazy {
        ViewModelProvider(this)[GameViewModel::class.java]
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

        observeViewModel()

        binding.tvBalance.text = Utils.balance.toString()

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
                Toast.makeText(
                    this,
                    getString(R.string.message_not_enough_money),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnIncrease.setOnClickListener {
            viewModel.increaseBet()
        }

        binding.btnDecrease.setOnClickListener {
            if (currentBet != 0) {
                viewModel.decreaseBet()
            }
        }

        binding.btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.slotList.observe(this) {
            slots = it
            binding.run {
                sl1CurrentImage.setImageResource(it[FIRST_SLOT_INDEX].currentImageId ?: R.drawable.j_image)
                sl1NextImage.setImageResource(it[FIRST_SLOT_INDEX].currentImageId ?: R.drawable.j_image)
                sl2CurrentImage.setImageResource(it[SECOND_SLOT_INDEX].currentImageId ?: R.drawable.j_image)
                sl2NextImage.setImageResource(it[SECOND_SLOT_INDEX].currentImageId ?: R.drawable.j_image)
                sl3CurrentImage.setImageResource(it[THIRD_SLOT_INDEX].currentImageId ?: R.drawable.j_image)
                sl3NextImage.setImageResource(it[THIRD_SLOT_INDEX].currentImageId ?: R.drawable.j_image)
            }
        }

        viewModel.currentBet.observe(this) {
            currentBet = it
            binding.tvBet.text = it.toString()
        }

        viewModel.score.observe(this) {
            binding.tvScore.text = it.toString()
        }
    }

    fun setRandomValue(slot: SlotItem, image: Int, numRoll: Int) {
        lockOrientationChange()
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
                    }
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationStart(animation: Animator) {
                    changeButtonState(false)
                }

            }).start()
    }

    fun changeButtonState(enabled: Boolean) {
        binding.flSpin.isClickable = enabled
        binding.flSpin.isFocusable = enabled
        binding.btnIncrease.isClickable = enabled
        binding.btnIncrease.isFocusable = enabled
        binding.btnDecrease.isClickable = enabled
        binding.btnDecrease.isFocusable = enabled
    }

    private fun lockOrientationChange() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
    }

    private fun unlockOrientationChange() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }

    private fun setImage(value: Int, slot: SlotItem) {
        when (value) {
            Utils.FirstGameSlots.nineImage -> viewModel.updateImageIdInItem(slot, R.drawable.nine_image)
            Utils.FirstGameSlots.jImage -> viewModel.updateImageIdInItem(slot, R.drawable.j_image)
            Utils.FirstGameSlots.kImage -> viewModel.updateImageIdInItem(slot, R.drawable.k_image)
            Utils.FirstGameSlots.aImage -> viewModel.updateImageIdInItem(slot, R.drawable.a_image)
            Utils.FirstGameSlots.runeImage -> viewModel.updateImageIdInItem(slot, R.drawable.rune_image)
            Utils.FirstGameSlots.wildImage -> viewModel.updateImageIdInItem(slot, R.drawable.wild_image)
        }
    }

    fun eventEnd() {
        if (countDown < 2) {
            countDown++
        } else {
            countDown = 0
            val slotImages = slots.map { it.currentImageId }.toSet()
            when (slotImages.size) {
                1 -> {
                    Toast.makeText(this, getString(R.string.winner_message), Toast.LENGTH_SHORT).show()
                    viewModel.updateScore(currentBet * 2)
                }
                2 -> {
                    Toast.makeText(this, getString(R.string.message_did_good), Toast.LENGTH_SHORT).show()
                    viewModel.updateScore(currentBet)
                }
                else -> {
                    Toast.makeText(this, getString(R.string.message_lost), Toast.LENGTH_SHORT).show()
                    Utils.balance -= currentBet
                    viewModel.updateScore(0)
                }
            }
            Utils.resetBalance()
            binding.tvBalance.text = Utils.balance.toString()
            unlockOrientationChange()
        }
    }

    companion object {

        private const val FIRST_SLOT_INDEX = 0
        private const val SECOND_SLOT_INDEX = 1
        private const val THIRD_SLOT_INDEX = 2

        private const val ANIMATION_DURATION = 250L
        fun newIntent(context: Context) = Intent(context, FirstGameActivity::class.java)
    }
}