package com.brainymobile.android.smsbox.utils.callbacks

interface ItemDragListener {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onMoveFinished()
}