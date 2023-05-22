package com.nile.fortu.game.games

import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.UUID

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

    fun createItemFromView(index: Int, view: FrameLayout) {
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
                    slotItem.currentImageId = item.currentImageId
                    listOfItems[currentIndex] = slotItem
                }
            }
        }
        _slotList.value = listOfItems
    }

    fun updateImageIdInItem(item: SlotItem, imageId: Int) {
        val listOfItems = _slotList.value?.toList() ?: listOf()
        listOfItems.forEach {
            if (item.id == it.id) {
                it.currentImageId = imageId
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
        private const val CURRENT_IMAGE_INDEX = 0
        private const val NEXT_IMAGE_INDEX = 1
        const val MIN_BET = 20
    }
}