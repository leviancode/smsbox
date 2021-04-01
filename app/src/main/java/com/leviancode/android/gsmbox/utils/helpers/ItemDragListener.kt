package com.leviancode.android.gsmbox.utils.helpers

interface ItemDragListener {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onMoveFinished()
}