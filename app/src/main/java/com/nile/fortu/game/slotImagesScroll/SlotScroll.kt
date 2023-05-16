package com.nile.fortu.game.slotImagesScroll

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.nile.fortu.game.R


class SlotScroll : FrameLayout {

    internal lateinit var eventEnd: EventEnd
    internal var lastResult = 0
    internal var oldValue = 0
    val view = LayoutInflater.from(context).inflate(R.layout.slot_image_scrol, this)
    val nextImage = view.findViewById<ImageView>(R.id.nextImage)
    val currentImage = view.findViewById<ImageView>(R.id.currentImage)

    companion object {
        private const val ANIMATION_DURATION = 150
    }

    val value: Int
        get() = Integer.parseInt(nextImage.tag.toString())

    fun setEventEnd(eventEnd: EventEnd) {
        this.eventEnd = eventEnd
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.slot_image_scrol, this)
        nextImage.translationY = height.toFloat()
    }

    fun setRandomValue(image: Int, numRoll: Int) {
        currentImage.animate()
            .translationY(-height.toFloat())
            .setDuration(ANIMATION_DURATION.toLong()).start()

        nextImage.translationY = nextImage.height.toFloat()
        nextImage.animate()
            .translationY(0f).setDuration(ANIMATION_DURATION.toLong())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
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

                    }
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationStart(animation: Animator) {

                }

            }).start()

    }

    //!! symbol is for asserting non-null to variables
    private fun setImage(currentImage: ImageView?, value: Int) {
        when (value) {
            Utils.nineImage -> currentImage!!.setImageResource(R.drawable.nine_image)
            Utils.jImage -> currentImage!!.setImageResource(R.drawable.j_image)
            Utils.kImage -> currentImage!!.setImageResource(R.drawable.k_image)
            Utils.aImage -> currentImage!!.setImageResource(R.drawable.a_image)
            Utils.runeImage -> currentImage!!.setImageResource(R.drawable.rune_image)
            Utils.wildImage -> currentImage!!.setImageResource(R.drawable.wild_image)
        }

        currentImage!!.tag = value
        lastResult = value
    }


}