package com.nile.fortu.game.games

import android.widget.ImageView

data class SlotItem(
    val id: String,
    val currentImage: ImageView,
    var currentImageId: Int? = null,
    val nextImage: ImageView,
    var oldValue: Int = 0
)
