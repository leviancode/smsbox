package com.leviancode.android.gsmbox.ui.view.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentBottomSheetBinding


class BottomSheetDialog : BottomSheetDialogFragment(), View.OnClickListener {
    lateinit var binding: FragmentBottomSheetBinding
    private var mListener: ItemClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bottom_sheet,
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
        val TAG = BottomSheetDialog::class.java.simpleName
        const val TEMPLATE_TAG = "new_template"
        const val RECIPIENT_TAG = "new_recipient"
    }
}