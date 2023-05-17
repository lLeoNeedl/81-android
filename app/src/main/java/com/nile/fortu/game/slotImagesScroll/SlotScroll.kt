package com.nile.fortu.game.slotImagesScroll

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.nile.fortu.game.R


class SlotScroll(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    lateinit var eventEnd: EventEnd
    var lastResult = 0
    var oldValue = 0
    private val view = LayoutInflater.from(context).inflate(R.layout.slot_image_scrol, this)
    private val nextImage = view.findViewById<ImageView>(R.id.nextImage)
    private val currentImage = view.findViewById<ImageView>(R.id.currentImage)

    val value: Int
        get() = nextImage.tag as Int

    init {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.slot_image_scrol, this)
        nextImage.translationY = height.toFloat()
        currentImage.setImageResource(R.drawable.j_image)
    }

    fun setRandomValue(image: Int, numRoll: Int) {
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
    }
}