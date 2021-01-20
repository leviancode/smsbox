package com.leviancode.android.gsmbox.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentNewItemDialogBinding


class NewItemDialogFragment : BottomSheetDialogFragment(), View.OnClickListener {
    lateinit var binding: FragmentNewItemDialogBinding
    private var mListener: ItemClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_item_dialog,
            container,
            false
        )
        binding.tvBtnAddDevice.setOnClickListener(this)
        binding.tvBtnAddTemplate.setOnClickListener(this)
        binding.ibCloseDialog.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View) {
        if (v is TextView){
            mListener?.onItemClick(v.tag.toString())
        }
        dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemClickListener) {
            mListener = context
        } else {
            throw RuntimeException(
                "$context must implement ItemClickListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface ItemClickListener {
        fun onItemClick(itemTag: String)
    }

    companion object{
        const val TAG = "NewItemDialogFragment"
        const val TEMPLATE_TAG = "new_template"
        const val DEVICE_TAG = "new_device"
    }
}