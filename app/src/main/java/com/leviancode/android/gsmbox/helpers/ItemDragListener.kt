package com.leviancode.android.gsmbox.helpers

interface ItemDragListener {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onMoveFinished()
}