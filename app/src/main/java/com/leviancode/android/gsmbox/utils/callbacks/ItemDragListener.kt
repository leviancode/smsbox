package com.leviancode.android.gsmbox.utils.callbacks

interface ItemDragListener {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onMoveFinished()
}