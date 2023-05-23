package com.nile.fortu.game.games

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.nile.fortu.game.R
import com.nile.fortu.game.databinding.ActivitySecondGameBinding
import com.nile.fortu.game.databinding.ItemFirstGameSlotBinding
import com.nile.fortu.game.databinding.ItemSecondGameSlotBinding
import kotlin.random.Random

class SecondGameActivity : AppCompatActivity() {

    private var countDown = 0
    private var currentBet = GameViewModel.MIN_BET

    private lateinit var slots: List<SlotItem>

    private val viewModel by lazy {
        ViewModelProvider(this)[GameViewModel::class.java]
    }

    private val binding by lazy {
        ActivitySecondGameBinding.inflate(layoutInflater)
    }

    private val listOfImages = listOf(
        R.drawable.j_image_game2,
        R.drawable.ten_image,
        R.drawable.q_image,
        R.drawable.k_image_game2,
        R.drawable.horse_image,
        R.drawable.dragon_image,
        R.drawable.flower_image,
        R.drawable.logo_image,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val listOfViews = listOf(
            binding.llSlot1,
            binding.llSlot2,
            binding.llSlot3,
            binding.llSlot4,
            binding.llSlot5
        )
        listOfViews.forEachIndexed { index, _ ->
            viewModel.createItemFromView(index)
        }

        observeViewModel()

        binding.tvBalance.text = Utils.balance.toString()

        binding.flSpin.setOnClickListener {
            if (currentBet <= Utils.balance && currentBet >= GameViewModel.MIN_BET) {
                listOfViews.forEachIndexed { index, linearLayout ->
                    setRandomValue(
                        linearLayout,
                        slots[index],
                        Random.nextInt(8),
                        Random.nextInt(15 - 5 + 1) + 5
                    )
                }
            } else if (currentBet > Utils.balance) {
                Toast.makeText(
                    this,
                    getString(R.string.message_not_enough_money),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.minimal_bet_message),
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
                setImagesOnSlots(binding.llSlot1, it[FIRST_SLOT_INDEX])
                setImagesOnSlots(binding.llSlot2, it[SECOND_SLOT_INDEX])
                setImagesOnSlots(binding.llSlot3, it[THIRD_SLOT_INDEX])
                setImagesOnSlots(binding.llSlot4, it[FOURTH_SLOT_INDEX])
                setImagesOnSlots(binding.llSlot5, it[FIFTH_SLOT_INDEX])
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

    private fun setImagesOnSlots (slotView: ItemSecondGameSlotBinding, slotItem: SlotItem) {
        slotView.run {
            prevImage.setImageResource(listOfImages[slotItem.prevImageIndex])
            currentImage.setImageResource(listOfImages[slotItem.currentImageIndex])
            nextImage.setImageResource(listOfImages[slotItem.nextImageIndex])
        }
    }

    private fun setRandomValue(view: ItemSecondGameSlotBinding, slot: SlotItem, image: Int, numRoll: Int) {
        lockOrientationChange()
        view.root.translationY = view.root.height.toFloat() / 2

        view.root.animate()
            .translationY(0f).setDuration(ANIMATION_DURATION)
            .setListener(object : Animator.AnimatorListener {

                override fun onAnimationRepeat(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    setImage(slot.oldValue % 8, slot)
                    if (slot.oldValue != numRoll) {
                        setRandomValue(view, slot, image, numRoll)
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

    private fun changeButtonState(enabled: Boolean) {
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

    private fun setImage(index: Int, slot: SlotItem) {
        viewModel.updateImageIdInItem(slot, index, listOfImages.size)
    }

    private fun eventEnd() {
        if (countDown < 4) {
            countDown++
        } else {
            countDown = 0
            val slotImages = slots.map { it.currentImageIndex }.toSet()
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