package com.nile.fortu.game.games

import android.widget.ImageView

data class SlotItem(
    val id: String,
    val currentImage: ImageView,
    var currentImageIndex: Int = 1,
    val nextImage: ImageView,
    var oldValue: Int = 0
)
