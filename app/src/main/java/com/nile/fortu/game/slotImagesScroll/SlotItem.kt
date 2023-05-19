package com.nile.fortu.game.slotImagesScroll

import android.widget.ImageView

data class SlotItem(
    val currentImage: ImageView,
    val nextImage: ImageView,
    val layoutHeight: Float,
    var oldValue: Int = 0,
    var lastResult: Int = 0
)
