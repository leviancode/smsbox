package com.leviancode.android.gsmbox.ui.base

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.utils.callbacks.ItemDragHelperCallback
import com.leviancode.android.gsmbox.utils.callbacks.ItemDragListener
import com.leviancode.android.gsmbox.utils.extensions.layoutInflater
import com.leviancode.android.gsmbox.utils.logI
import java.util.*


open class BaseListAdapter<T : BaseEntity, B : ViewDataBinding>(
    @LayoutRes private val layRes: Int,
    open inline val bind: (binding: B, item: T, position: Int) -> Unit
) : ListAdapter<T, BaseListAdapter.GenericViewHolder<B>>(BaseDiffUtil()), ItemDragListener {

    private var touchHelper: ItemTouchHelper? = null

    open var onSubmitComplete: () -> Unit = {}

    var onFinishDrag: () -> Unit = {}

    open fun addItem(item: T) {
        val newList = currentList.toMutableList().apply { add(item) }
        submitList(newList, onSubmitComplete)
    }

    open fun addItems(items: List<T>) {
        val newList = currentList.toMutableList().apply { addAll(items) }
        submitList(newList, onSubmitComplete)
    }

    open fun updateAll(items: List<T>) {
        submitList(items, onSubmitComplete)
    }

    fun initDragNDrop(recyclerView: RecyclerView, onFinish: () -> Unit) {
        val dragHelper = ItemDragHelperCallback(this)
        touchHelper = ItemTouchHelper(dragHelper)
        touchHelper?.attachToRecyclerView(recyclerView)
        onFinishDrag = onFinish
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<B> {
        val binding = DataBindingUtil.inflate<B>(parent.layoutInflater, layRes, parent, false)
        return GenericViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericViewHolder<B>, position: Int) {
        bind(holder.binding, getItem(position), position)

        touchHelper?.apply {
            holder.binding.root.setOnLongClickListener {
                startDrag(holder)
                true
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val newList = currentList.toMutableList()
        newList[fromPosition].position = toPosition
        newList[toPosition].position = fromPosition
        updateAll(newList.sortedBy { it.position })
    }

    override fun onMoveFinished() {
        onFinishDrag()
    }

    class GenericViewHolder<B : ViewDataBinding>(val binding: B) :
        RecyclerView.ViewHolder(binding.root)

    class BaseDiffUtil<E : BaseEntity> : DiffUtil.ItemCallback<E>() {
        override fun areItemsTheSame(oldItem: E, newItem: E): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: E, newItem: E): Boolean {
            return oldItem == newItem
        }

    }

}