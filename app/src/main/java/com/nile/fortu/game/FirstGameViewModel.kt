package com.nile.fortu.game

import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nile.fortu.game.slotImagesScroll.SlotItem
import java.util.UUID

class FirstGameViewModel : ViewModel() {

    private val _slotList = MutableLiveData(listOf<SlotItem>())
    val slotList: LiveData<List<SlotItem>>
        get() = _slotList

    private var currentBet = 0
    private var score = 0

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

    companion object {
        private const val CURRENT_IMAGE_INDEX = 0
        private const val NEXT_IMAGE_INDEX = 1
    }
}