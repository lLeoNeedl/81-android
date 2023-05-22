package com.nile.fortu.game.games

import android.widget.ImageView

data class SlotItem(
    val id: String,
    val currentImage: ImageView,
    var prevImageIndex: Int = 0,
    var currentImageIndex: Int = 1,
    var nextImageIndex: Int = 2,
    val nextImage: ImageView,
    var oldValue: Int = 0
)
