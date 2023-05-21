package com.nile.fortu.game.slotImagesScroll

import android.widget.ImageView
import com.nile.fortu.game.R

data class SlotItem(
    val id: String,
    val currentImage: ImageView,
    var currentImageId: Int? = null,
    val nextImage: ImageView,
    var oldValue: Int = 0
)
