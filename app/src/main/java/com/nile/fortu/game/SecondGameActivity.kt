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
import com.nile.fortu.game.databinding.ActivitySecondGameBinding
import com.nile.fortu.game.slotImagesScroll.SlotItem
import com.nile.fortu.game.slotImagesScroll.Utils
import kotlin.random.Random

class SecondGameActivity : AppCompatActivity() {

    private var countDown = 0
    private var currentBet = 0

    private lateinit var slots: List<SlotItem>

    private val viewModel by lazy {
        ViewModelProvider(this)[GameViewModel::class.java]
    }

    private val binding by lazy {
        ActivitySecondGameBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val listOfViews = listOf(
            binding.flSlot1,
            binding.flSlot2,
            binding.flSlot3,
            binding.flSlot4,
            binding.flSlot5
        )
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
                        Random.nextInt(8),
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
                sl1CurrentImage.setImageResource(it[FIRST_SLOT_INDEX].currentImageId ?: R.drawable.j_image_game2)
                sl1NextImage.setImageResource(it[FIRST_SLOT_INDEX].currentImageId ?: R.drawable.j_image_game2)
                sl2CurrentImage.setImageResource(it[SECOND_SLOT_INDEX].currentImageId ?: R.drawable.j_image_game2)
                sl2NextImage.setImageResource(it[SECOND_SLOT_INDEX].currentImageId ?: R.drawable.j_image_game2)
                sl3CurrentImage.setImageResource(it[THIRD_SLOT_INDEX].currentImageId ?: R.drawable.j_image_game2)
                sl3NextImage.setImageResource(it[THIRD_SLOT_INDEX].currentImageId ?: R.drawable.j_image_game2)
                sl4CurrentImage.setImageResource(it[FOURTH_SLOT_INDEX].currentImageId ?: R.drawable.j_image_game2)
                sl4NextImage.setImageResource(it[FOURTH_SLOT_INDEX].currentImageId ?: R.drawable.j_image_game2)
                sl5CurrentImage.setImageResource(it[FIFTH_SLOT_INDEX].currentImageId ?: R.drawable.j_image_game2)
                sl5NextImage.setImageResource(it[FIFTH_SLOT_INDEX].currentImageId ?: R.drawable.j_image_game2)
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
                    setImage(slot.oldValue % 8, slot)
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
            Utils.SecondGameUtils.jImage -> viewModel.updateImageIdInItem(slot, R.drawable.j_image_game2)
            Utils.SecondGameUtils.tenImage -> viewModel.updateImageIdInItem(slot, R.drawable.ten_image)
            Utils.SecondGameUtils.qImage -> viewModel.updateImageIdInItem(slot, R.drawable.q_image)
            Utils.SecondGameUtils.kImage -> viewModel.updateImageIdInItem(slot, R.drawable.k_image_game2)
            Utils.SecondGameUtils.horseImage -> viewModel.updateImageIdInItem(slot, R.drawable.horse_image)
            Utils.SecondGameUtils.dragonImage -> viewModel.updateImageIdInItem(slot, R.drawable.dragon_image)
            Utils.SecondGameUtils.flowerImage -> viewModel.updateImageIdInItem(slot, R.drawable.flower_image)
            Utils.SecondGameUtils.logoImage -> viewModel.updateImageIdInItem(slot, R.drawable.logo_image)
        }
    }

    fun eventEnd() {
        if (countDown < 4) {
            countDown++
        } else {
            countDown = 0
            val slotImages = slots.map { it.currentImageId }.toSet()
            when (slotImages.size) {
                1 -> {
                    Toast.makeText(this, getString(R.string.winner_message), Toast.LENGTH_SHORT).show()
                    viewModel.updateScore(currentBet * 3)
                }
                2 -> {
                    Toast.makeText(this, getString(R.string.message_excellent), Toast.LENGTH_SHORT).show()
                    viewModel.updateScore(currentBet * 2)
                }
                3 -> {
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
        private const val FOURTH_SLOT_INDEX = 3
        private const val FIFTH_SLOT_INDEX = 4

        private const val ANIMATION_DURATION = 250L

        fun newIntent(context: Context) = Intent(context, SecondGameActivity::class.java)
    }
}