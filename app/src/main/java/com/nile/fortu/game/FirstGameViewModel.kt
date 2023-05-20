package com.nile.fortu.game

import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nile.fortu.game.slotImagesScroll.SlotItem
import java.util.UUID

class FirstGameViewModel : ViewModel() {

    private val _slotsList = MutableLiveData(listOf<SlotItem>())
    val slotList: LiveData<List<SlotItem>>
        get() = _slotsList

    private var countDown = 0
    private var currentBet = 0
    private var score = 0

    fun createItemFromView(view: FrameLayout) {
        val slotItem = SlotItem(
            id = UUID.randomUUID().toString(),
            currentImage = view.getChildAt(CURRENT_IMAGE_INDEX) as ImageView,
            nextImage = view.getChildAt(NEXT_IMAGE_INDEX) as ImageView,
            nextImageTranslationY = view.height.toFloat()
        )
        val listOfItems = _slotsList.value?.toMutableList()
        listOfItems?.add(slotItem)
        _slotsList.value = listOfItems
    }

    fun updateImageIdInItem(item: SlotItem, imageId: Int) {
        val listOfItems = _slotsList.value?.toMutableList() ?: mutableListOf()
        listOfItems.forEach {
            if (item.id == it.id) {
                it.currentImageId = imageId
            }
        }
        _slotsList.value = listOfItems
    }

    fun updateTranslation(item: SlotItem) {
        val listOfItems = _slotsList.value?.toMutableList() ?: mutableListOf()
        listOfItems.forEach {
            if (item.id == it.id) {
                it.nextImageTranslationY = item.nextImage.translationY
            }
        }
        _slotsList.value = listOfItems
    }

    companion object {
        private const val CURRENT_IMAGE_INDEX = 0
        private const val NEXT_IMAGE_INDEX = 1
    }
}