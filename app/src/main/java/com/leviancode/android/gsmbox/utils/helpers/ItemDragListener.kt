package com.leviancode.android.gsmbox.utils.helpers

interface ItemDragListener {
    fun onItemMove(fromPosition: Int, toPosition: Int): Pair<Int, Int>
    fun onMoveFinished(firstId: Int, secondId: Int)
}