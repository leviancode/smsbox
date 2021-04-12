package com.leviancode.android.gsmbox.ui.recipients.view.groups.edit

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.databinding.DialogEditableRecipientGroupBinding
import com.leviancode.android.gsmbox.ui.extra.ColorPickerDialog
import com.leviancode.android.gsmbox.utils.REQ_SELECT_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.utils.extensions.goBack
import com.leviancode.android.gsmbox.utils.extensions.setNavigationResult
import com.leviancode.android.gsmbox.utils.hideKeyboard
import com.leviancode.android.gsmbox.utils.showKeyboard

class EditableRecipientGroupDialog : BottomSheetDialogFragment()  {
    private lateinit var binding: DialogEditableRecipientGroupBinding
    private val viewModel: EditableRecipientGroupViewModel by viewModels()
    private val args: EditableRecipientGroupDialogArgs by navArgs()

    override fun getTheme(): Int = R.style.CustomBottomSheetDialogWithKeyboard

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.behavior.skipCollapsed = true
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_editable_recipient_group, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.loadGroup(args.groupId)
        setTitle(args.groupId != 0L)
        showKeyboard(binding.editTextRecipientGroupName)
        observeUI()
    }

    private fun setTitle(editMode: Boolean) {
        if (editMode) binding.toolbar.title = getString(R.string.edit_group)
    }

    private fun observeUI(){
       // setTextUniqueWatcher()
        binding.toolbar.setNavigationOnClickListener { closeDialog(null) }

        viewModel.closeDialogEvent.observe(viewLifecycleOwner){
            closeDialog(it)
        }

        viewModel.selectColorEvent.observe(viewLifecycleOwner){ selectColor(it) }
    }

   /* private fun setTextUniqueWatcher() {
        val textWatcher = TextUniqueWatcher { isUnique ->
            args.group.isNameUnique = isUnique
        }
        binding.editTextRecipientGroupName.addTextChangedListener(textWatcher)

        viewModel.getNamesWithoutCurrent(args.group.recipientGroupId)
            .observe(viewLifecycleOwner) { list ->
                textWatcher.wordList = list
            }
    }*/

    private fun selectColor(color: String){
        hideKeyboard()

        ColorPickerDialog(
            requireContext(),
            childFragmentManager,
            color
        ).show {
            viewModel.setIconColor(it)
        }
    }

    private fun closeDialog(result: RecipientGroup?) {
        setNavigationResult(result, REQ_SELECT_RECIPIENT_GROUP)
        goBack()
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }
}