package com.app.emoji_drawer.ui.drawing


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DrawingViewModel : ViewModel() {
    private val _menuPosition = MutableLiveData(MenuPositions.BOTTOM)
    val menuPosition: LiveData<MenuPositions> = _menuPosition

    fun setMenuPosition(value: MenuPositions) {
        _menuPosition.postValue(value)
    }

    fun changeMenuPosition() {
        _menuPosition.value = _menuPosition.value?.nextPosition()
    }

    fun getMenuPosition(): MenuPositions {
        return menuPosition.value ?: MenuPositions.BOTTOM
    }


    enum class MenuPositions{
        TOP, BOTTOM;

        fun nextPosition() : MenuPositions{
            return when (this) {
                TOP -> BOTTOM
                BOTTOM -> TOP
            }
        }
    }
}