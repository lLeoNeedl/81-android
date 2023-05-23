package com.nile.fortu.game.games

object Utils {

    var balance = 1000
    fun resetBalance() {
        if (balance == 0) {
            balance = 1000
        }
    }
}