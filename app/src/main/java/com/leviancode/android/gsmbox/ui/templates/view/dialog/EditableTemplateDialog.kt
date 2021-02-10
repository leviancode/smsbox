package com.leviancode.android.gsmbox.ui.templates.view.dialog

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.data.model.TemplateObservable
import com.leviancode.android.gsmbox.databinding.DialogEditableTemplateBinding
import com.leviancode.android.gsmbox.databinding.DialogEditableTemplateNumberHolderBinding
import com.leviancode.android.gsmbox.ui.templates.viewmodel.EditableTemplateViewModel
import com.leviancode.android.gsmbox.utils.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class EditableTemplateDialog : AbstractFullScreenDialog(){
    private lateinit var binding: DialogEditableTemplateBinding
    private val viewModel: EditableTemplateViewModel by viewModels()
    private val args: EditableTemplateDialogArgs by navArgs()

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

        if (args.templateId != null){
            loadTemplate()
        } else {
            showKeyboard()
            viewModel.createTemplate(args.groupId)
        }

        binding.viewModel = viewModel
        binding.editTextTemplateName.requestFocus()
        binding.toolbar.apply {
            setNavigationOnClickListener { closeDialog() }
        }

        observe()
    }

    private fun loadTemplate(){
        lifecycleScope.launch {
            viewModel.loadTemplateById(args.templateId!!).collect {
                binding.toolbar.title = it
            }
        }
    }

    private fun observe(){
        viewModel.openRecipientDialogLiveEvent.observe(viewLifecycleOwner){ recipient ->
            showSaveRecipientDialog(recipient)
        }

        viewModel.addNumberFieldLiveEvent.observe(viewLifecycleOwner){ recipient ->
            addNumberField(recipient)
        }

        viewModel.removeRecipientLiveEvent.observe(viewLifecycleOwner){ view ->
            removeRecipientLayout(view)
        }

        viewModel.selectColorLiveEvent.observe(viewLifecycleOwner){ color ->
            selectColor(color)
        }

        viewModel.selectContactLiveEvent.observe(viewLifecycleOwner){ recipient ->
            selectContact(recipient)
        }

        viewModel.closeDialogLiveEvent.observe(viewLifecycleOwner){
            saved = it
            closeDialog()
        }

        viewModel.fieldsNotEmptyLiveEvent.observe(viewLifecycleOwner){ enabled ->
            binding.btnTemplateSave.apply {
                if (enabled) setTextColor(resources.getColor(R.color.secondary, null))
                else setTextColor(resources.getColor(R.color.ltGrey, null))
                isEnabled = enabled
            }
        }
    }

    private fun selectContact(recipient: Recipient){
        hideKeyboard()

        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : BasePermissionListener() {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    showContactsDialog(recipient)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                   showToast(getString(R.string.permission_dined))
                }
            }).check()
    }

    private fun showContactsDialog(recipient: Recipient) {
        getNavigationResult<String>(REQUEST_KEY_SELECTED)?.observe(viewLifecycleOwner){ result ->
            if (!result.isNullOrBlank()){
                recipient.phoneNumber = result
                removeNavigationResult<String>(REQUEST_KEY_SELECTED)
            }
        }

        findNavController().navigate(
            EditableTemplateDialogDirections.actionSelectContact()
        )
    }

    private fun selectColor(color: Int){
        hideKeyboard()

        ColorPickerDialog(
            requireContext(),
            childFragmentManager,
            color
        ).show {
            viewModel.setIconColor(it)
        }
    }

    private fun showSaveRecipientDialog(recipient: Recipient){
        getNavigationResult<String>(REQUEST_KEY_SAVED)?.observe(viewLifecycleOwner){ result ->
            when (result){
                RESULT_OK -> {
                    showToast(getString(R.string.recipient_saved))
                    hideKeyboard()
                }
            }
            removeNavigationResult<String>(REQUEST_KEY_SAVED)
        }

        findNavController().navigate(
            EditableTemplateDialogDirections.actionOpenEditableRecipient(recipient)
        )
    }

    private fun addNumberField(recipient: RecipientObservable) {
        val inflater = LayoutInflater.from(requireContext())
        val recipientBinding = DataBindingUtil.inflate<DialogEditableTemplateNumberHolderBinding>(
            inflater, R.layout.dialog_editable_template_number_holder, binding.recipientsLayout, true)

        recipientBinding.recipient = recipient
        recipientBinding.viewModel = viewModel

        if (binding.recipientsLayout.childCount > 1){
            recipientBinding.btnRemoveNumber.visibility = View.VISIBLE
            recipientBinding.editTextRecipientNumber.requestFocus()
            showKeyboard()
        }
    }

    private fun removeRecipientLayout(view: View){
        binding.recipientsLayout.removeView(view.parent as ViewGroup)
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