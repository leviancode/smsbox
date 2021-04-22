package com.leviancode.android.gsmbox.ui.templates.view.groups.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogEditableTemplateGroupBinding
import com.leviancode.android.gsmbox.ui.extra.ColorPickerDialog
import com.leviancode.android.gsmbox.ui.templates.view.AbstractFullScreenDialog
import com.leviancode.android.gsmbox.utils.helpers.TextUniqueWatcher
import com.leviancode.android.gsmbox.utils.RESULT_CANCEL
import com.leviancode.android.gsmbox.utils.RESULT_OK
import com.leviancode.android.gsmbox.utils.hideKeyboard
import com.leviancode.android.gsmbox.utils.showKeyboard

class EditableTemplateGroupDialog : AbstractFullScreenDialog() {
    private lateinit var binding: DialogEditableTemplateGroupBinding
    private val viewModel: EditableTemplateGroupViewModel by viewModels()
    private val args: EditableTemplateGroupDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogEditableTemplateGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.loadGroup(args.groupId)
        setTitle(args.groupId != 0)
        showKeyboard(binding.editTextTemplateGroupName)
        observeUI()
    }

    private fun setTitle(editMode: Boolean) {
        if (editMode) binding.toolbar.title = getString(R.string.edit_group)
    }

    private fun observeUI() {
       // setTextUniqueWatcher()

        binding.toolbar.setNavigationOnClickListener { closeDialog(RESULT_CANCEL) }

        viewModel.chooseColorLiveEvent.observe(viewLifecycleOwner) { selectColor(it) }

        viewModel.savedLiveEvent.observe(viewLifecycleOwner) { closeDialog(RESULT_OK) }
    }

    /*private fun setTextUniqueWatcher() {
        val textWatcher = TextUniqueWatcher { isUnique -> viewModel.data.isNameUnique = isUnique }
        binding.editTextTemplateGroupName.addTextChangedListener(textWatcher)
        viewModel.getNamesExclusiveCurrent(args.group.getName())
            .observe(viewLifecycleOwner) {
                textWatcher.wordList = it
            }
    }*/

    private fun selectColor(color: String) {
        hideKeyboard()

        ColorPickerDialog(
            requireContext(),
            childFragmentManager,
            color
        ).show {
            viewModel.setIconColor(it)
        }
    }

    override fun isDataEdited(): Boolean {
        return viewModel.isGroupEdited()
    }
}