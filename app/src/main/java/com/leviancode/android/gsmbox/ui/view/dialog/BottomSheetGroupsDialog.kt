package com.leviancode.android.gsmbox.ui.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat.setAccessibilityPaneTitle
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.BottomSheetGroupsListAdapter
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.databinding.DialogBottomSheetGroupsBinding

class BottomSheetGroupsDialog(val data: List<TemplateGroup>) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogBottomSheetGroupsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_bottom_sheet_groups,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAccessibilityPaneTitle(view, "Template Groups")

        binding.listAdapter = BottomSheetGroupsListAdapter(data)

        val buttonNewGroup = binding.bottomSheetGroupsButtonView
        val bottomSheet = binding.bottomSheetGroupsLayout
        val sheetBehavior = BottomSheetBehavior.from(bottomSheet)

        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val offset = if (slideOffset <= 0) {
                    bottomSheet.y + sheetBehavior.peekHeight - buttonNewGroup.height
                } else {
                    (bottomSheet.height - buttonNewGroup.height).toFloat()
                }
                buttonNewGroup.animate().y(offset).setDuration(0).start()
            }

        })
    }

    companion object{
        val TAG = BottomSheetGroupsDialog::class.java.simpleName
    }
}