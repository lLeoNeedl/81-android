package com.nile.fortu.game.slotImagesScroll

interface EventEnd {

    fun eventEnd(result: Int, bet: Int)

    fun changeButtonState(enabled: Boolean)

    fun lockOrientationChange()

    fun unlockOrientationChange()
}
