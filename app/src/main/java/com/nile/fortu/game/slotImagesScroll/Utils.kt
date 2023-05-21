package com.nile.fortu.game.slotImagesScroll

sealed class Utils {
    object FirstGameSlots {
        var nineImage = 0
        var jImage = 1
        var kImage = 2
        var aImage = 3
        var runeImage = 4
        var wildImage = 5
    }

    object SecondGameUtils {
        var jImage = 0
        var tenImage = 1
        var qImage = 2
        var kImage = 3
        var horseImage = 4
        var dragonImage = 5
        var flowerImage = 6
        var logoImage = 7
    }

    companion object {
        var balance = 1000
        fun resetBalance() {
            if (balance == 0) {
                balance = 1000
            }
        }
    }
}