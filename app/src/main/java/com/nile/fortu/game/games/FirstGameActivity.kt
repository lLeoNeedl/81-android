package com.nile.fortu.game.games

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.nile.fortu.game.R
import com.nile.fortu.game.databinding.ActivityFirstGameBinding
import com.nile.fortu.game.databinding.ItemFirstGameSlotBinding
import kotlin.random.Random

class FirstGameActivity : AppCompatActivity() {

    private var countDown = 0
    private var currentBet = GameViewModel.MIN_BET

    private lateinit var slots: List<SlotItem>

    private val viewModel by lazy {
        ViewModelProvider(this)[GameViewModel::class.java]
    }

    private val binding by lazy {
        ActivityFirstGameBinding.inflate(layoutInflater)
    }

    private val listOfImages = listOf(
        R.drawable.nine_image,
        R.drawable.j_image,
        R.drawable.k_image,
        R.drawable.a_image,
        R.drawable.rune_image,
        R.drawable.wild_image
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val listOfViews = listOf(binding.llSlot1, binding.llSlot2, binding.llSlot3)
        listOfViews.forEachIndexed { index, linearLayout ->
            viewModel.createItemFromView(index, linearLayout)
        }

        observeViewModel()

        binding.tvBalance.text = Utils.balance.toString()

        binding.flSpin.setOnClickListener {
            if (currentBet <= Utils.balance && currentBet >= GameViewModel.MIN_BET) {
                listOfViews.forEachIndexed { index, linearLayout ->
                    setRandomValue(
                        linearLayout,
                        slots[index],
                        Random.nextInt(6),
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
                setImagesOnSlots(binding.llSlot1, it[0])
                setImagesOnSlots(binding.llSlot2, it[1])
                setImagesOnSlots(binding.llSlot3, it[2])
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

    private fun setImagesOnSlots (slotView: ItemFirstGameSlotBinding, slotItem: SlotItem) {
        slotView.run {
            prevImage.setImageResource(listOfImages[slotItem.prevImageIndex])
            currentImage.setImageResource(listOfImages[slotItem.currentImageIndex])
            nextImage.setImageResource(listOfImages[slotItem.nextImageIndex])
        }
    }

    fun setRandomValue(view: ItemFirstGameSlotBinding, slot: SlotItem, image: Int, numRoll: Int) {
        lockOrientationChange()
        view.root.translationY = view.root.height.toFloat()

        view.root.animate()
            .translationY(0f).setDuration(ANIMATION_DURATION)
            .setListener(object : Animator.AnimatorListener {

                override fun onAnimationRepeat(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    setImage(slot.oldValue % 6, slot)
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

    private fun setImage(index: Int, slot: SlotItem) {
        viewModel.updateImageIdInItem(slot, index, listOfImages.size)
    }

    fun eventEnd() {
        if (countDown < 2) {
            countDown++
        } else {
            countDown = 0
            val slotImages = slots.map { it.currentImageIndex }.toSet()
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

        private const val ANIMATION_DURATION = 250L
        fun newIntent(context: Context) = Intent(context, FirstGameActivity::class.java)
    }
}