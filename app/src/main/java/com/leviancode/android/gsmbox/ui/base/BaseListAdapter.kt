package com.leviancode.android.gsmbox.ui.base

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.utils.extensions.layoutInflater

open class BaseListAdapter<T : BaseEntity, B : ViewDataBinding>(
    @LayoutRes private val layRes: Int,
    open inline val bind: (binding: B, item: T, position: Int) -> Unit
) : ListAdapter<T, BaseListAdapter.GenericViewHolder<B>>(BaseDiffUtil()) {

    open var onSubmitComplete: () -> Unit = {}

    open fun addItem(item: T){
        val newList = currentList.toMutableList().apply { add(item) }
        submitList(newList, onSubmitComplete)
    }

    open fun addItems(items: List<T>){
        val newList = currentList.toMutableList().apply { addAll(items) }
        submitList(newList, onSubmitComplete)
    }

    open fun updateAll(items: List<T>){
        submitList(items, onSubmitComplete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<B> {
        val binding = DataBindingUtil.inflate<B>(parent.layoutInflater, layRes, parent, false)
        return GenericViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericViewHolder<B>, position: Int) {
        bind(holder.binding, getItem(position), position)
    }

    class GenericViewHolder<B: ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)

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