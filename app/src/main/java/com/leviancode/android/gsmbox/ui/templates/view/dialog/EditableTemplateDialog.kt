package com.leviancode.android.gsmbox.ui.templates.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.databinding.DialogEditableTemplateBinding
import com.leviancode.android.gsmbox.databinding.DialogEditableTemplateNumberHolderBinding
import com.leviancode.android.gsmbox.ui.templates.viewmodel.EditableTemplateViewModel
import com.leviancode.android.gsmbox.utils.*


class EditableTemplateDialog : AbstractFullScreenDialog() {
    private lateinit var binding: DialogEditableTemplateBinding
    private val viewModel: EditableTemplateViewModel by viewModels()
    private val args: EditableTemplateDialogArgs by navArgs()
    private lateinit var contactsLauncher: ActivityResultLauncher<Void>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_editable_template, container, false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactsLauncher = registerForActivityResult(PickContact()) { result ->
            viewModel.addContact(ContactsManager.parseUri(requireContext(), result))
        }
        viewModel.setTemplate(args.template)
        showKeyboard(binding.editTextTemplateName)

        binding.viewModel = viewModel
        binding.toolbar.apply {
            args.template.name.let {
                if (it.isNotBlank()) title = it
            }
            setNavigationOnClickListener { closeDialog() }
        }

        observeUI()
    }

    private fun observeUI() {
        viewModel.openRecipientDialogLiveEvent.observe(viewLifecycleOwner) { recipient ->
            showSaveRecipientDialog(recipient)
        }

        viewModel.addNumberFieldLiveEvent.observe(viewLifecycleOwner) { recipient ->
            addNumberField(recipient)
        }

        viewModel.removeRecipientLiveEvent.observe(viewLifecycleOwner) { view ->
            removeRecipientLayout(view)
        }

        viewModel.selectColorLiveEvent.observe(viewLifecycleOwner) { color ->
            selectColor(color)
        }

        viewModel.selectContactLiveEvent.observe(viewLifecycleOwner) { selectContact() }

        viewModel.closeDialogLiveEvent.observe(viewLifecycleOwner) {
            saved = it
            closeDialog()
        }
    }

    private fun selectContact() {
        hideKeyboard()
        ContactsManager.openContactsApp(requireContext(), contactsLauncher)
    }

    private fun showContactsDialog(recipient: Recipient) {
        getNavigationResult<String>(REQUEST_SELECTED)?.observe(viewLifecycleOwner) { result ->
            if (!result.isNullOrBlank()) {
                recipient.phoneNumber = result
                removeNavigationResult<String>(REQUEST_SELECTED)
            }
        }

        findNavController().navigate(
            EditableTemplateDialogDirections.actionSelectContact()
        )
    }

    private fun selectColor(color: Int) {
        hideKeyboard()

        ColorPickerDialog(
            requireContext(),
            childFragmentManager,
            color
        ).show {
            viewModel.setIconColor(it)
        }
    }

    private fun showSaveRecipientDialog(recipient: Recipient) {
        getNavigationResult<String>(REQUEST_SAVED)?.observe(viewLifecycleOwner) { result ->
            when (result) {
                RESULT_OK -> {
                    showToast(getString(R.string.recipient_saved))
                    hideKeyboard()
                }
            }
            removeNavigationResult<String>(REQUEST_SAVED)
        }

        findNavController().navigate(
            EditableTemplateDialogDirections.actionOpenEditableRecipient(recipient)
        )
    }

    private fun addNumberField(recipient: RecipientObservable) {
        val inflater = LayoutInflater.from(requireContext())
        val recipientBinding = DataBindingUtil.inflate<DialogEditableTemplateNumberHolderBinding>(
            inflater,
            R.layout.dialog_editable_template_number_holder,
            binding.recipientsLayout,
            true
        )

        recipientBinding.recipient = recipient
        recipientBinding.viewModel = viewModel

        if (binding.recipientsLayout.childCount > 1) {
            showKeyboard(recipientBinding.editTextRecipientNumber)
            recipientBinding.btnRemoveNumber.visibility = View.VISIBLE
        }
    }

    private fun removeRecipientLayout(view: View) {
        val parent = view.parent as ViewGroup
        val index = binding.recipientsLayout.indexOfChild(parent)
        val child = binding.recipientsLayout.getChildAt(index - 1)
            .findViewById<View>(R.id.edit_text_recipient_number)
        showKeyboard(child)
        binding.recipientsLayout.removeView(parent)
    }

    override fun isFieldsNotEmpty(): Boolean {
        return viewModel.isFieldsNotEmpty()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG = EditableTemplateDialog::class.java.simpleName
    }
}