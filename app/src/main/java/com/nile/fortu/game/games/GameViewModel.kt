package com.nile.fortu.game.games

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.UUID
import kotlin.math.max
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val _slotList = MutableLiveData(listOf<SlotItem>())
    val slotList: LiveData<List<SlotItem>>
        get() = _slotList

    private val _currentBet = MutableLiveData(MIN_BET)
    val currentBet: LiveData<Int>
        get() = _currentBet

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    fun createItemFromView(index: Int, view: LinearLayout) {
        val slotItem = SlotItem(
            id = UUID.randomUUID().toString(),
            currentImage = view.getChildAt(CURRENT_IMAGE_INDEX) as ImageView,
            nextImage = view.getChildAt(NEXT_IMAGE_INDEX) as ImageView
        )
        val listOfItems = slotList.value?.toMutableList() ?: mutableListOf()

        if (listOfItems.size <= index) {
            listOfItems.add(slotItem)
        } else {
            listOfItems.forEachIndexed { currentIndex, item ->
                if (currentIndex == index) {
                    slotItem.currentImageIndex = item.currentImageIndex
                    slotItem.prevImageIndex = item.prevImageIndex
                    slotItem.nextImageIndex = item.nextImageIndex
                    listOfItems[currentIndex] = slotItem
                }
            }
        }
        _slotList.value = listOfItems
    }

    fun updateImageIdInItem(item: SlotItem, imageIndex: Int, maxValue: Int) {
        val listOfItems = _slotList.value?.toList() ?: listOf()
        listOfItems.forEach {
            if (item.id == it.id) {
                it.currentImageIndex = imageIndex
                it.prevImageIndex = Random.nextInt(0, maxValue)
                it.nextImageIndex = Random.nextInt(0, maxValue)
            }
        }
        _slotList.value = listOfItems
    }

    fun increaseBet() {
        var bet = _currentBet.value ?: 0
        bet += 20
        _currentBet.value = bet
    }

    fun decreaseBet() {
        var bet = _currentBet.value ?: 0
        bet -= 20
        _currentBet.value = bet
    }

    fun updateScore(score: Int) {
        _score.value = score
        Utils.balance += score
    }

    companion object {
        private const val CURRENT_IMAGE_INDEX = 1
        private const val NEXT_IMAGE_INDEX = 2
        const val MIN_BET = 20
    }
}