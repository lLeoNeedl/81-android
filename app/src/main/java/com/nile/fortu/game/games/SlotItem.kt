package com.nile.fortu.game.games

import android.widget.ImageView

data class SlotItem(
    val id: String,
    var prevImageIndex: Int = 0,
    var currentImageIndex: Int = 1,
    var nextImageIndex: Int = 2,
    var oldValue: Int = 0
)
