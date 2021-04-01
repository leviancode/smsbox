package com.leviancode.android.gsmbox.ui.settings.placeholders

import android.app.Dialog
import android.content.DialogInterface
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
import com.leviancode.android.gsmbox.databinding.DialogEditablePlaceholderBinding
import com.leviancode.android.gsmbox.helpers.TextUniqueChecker
import com.leviancode.android.gsmbox.utils.extensions.goBack
import com.leviancode.android.gsmbox.utils.hideKeyboard
import com.leviancode.android.gsmbox.utils.showKeyboard

class EditablePlaceholderDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogEditablePlaceholderBinding
    private val viewModel: PlaceholdersViewModel by viewModels({ requireParentFragment() })
    private val args: EditablePlaceholderDialogArgs by navArgs()
    private lateinit var textChecker: TextUniqueChecker

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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_editable_placeholder,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.model = args.placeholder.apply {
            setKeyword(getKeyword().removePrefix("#"))
        }
        setTitle(args.placeholder.getKeyword())
        showKeyboard(binding.editTextPlaceholdersName)
        observeUI()
    }

    private fun observeUI() {
        binding.toolbar.setNavigationOnClickListener { goBack() }
        textChecker = TextUniqueChecker { isUnique ->
            binding.model?.isKeyUnique = isUnique
        }
        binding.editTextPlaceholdersName.addTextChangedListener(textChecker)

        viewModel.keysWithoutCurrentPlaceholder(args.placeholder.placeholderId)
            .observe(viewLifecycleOwner) { list ->
                textChecker.comparisonList = list
            }
        binding.btnPlaceholderSave.setOnClickListener {
            viewModel.savePlaceholder(args.placeholder)
            goBack()
        }
    }

    private fun setTitle(name: String) {
        if (name.isNotBlank()) binding.toolbar.title = getString(R.string.edit_placeholder)
    }

    override fun onDismiss(dialog: DialogInterface) {
        hideKeyboard()
        super.onDismiss(dialog)
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }

    override fun onDestroy() {
        binding.editTextPlaceholdersName.removeTextChangedListener(textChecker)
        super.onDestroy()
    }
}