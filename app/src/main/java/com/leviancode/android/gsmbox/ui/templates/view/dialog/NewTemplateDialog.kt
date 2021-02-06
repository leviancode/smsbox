package com.leviancode.android.gsmbox.ui.templates.view.dialog

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.data.model.TemplateObservable
import com.leviancode.android.gsmbox.databinding.DialogNewTemplateBinding
import com.leviancode.android.gsmbox.databinding.DialogNewTemplateNumberHolderBinding
import com.leviancode.android.gsmbox.ui.templates.viewmodel.NewTemplateDialogViewModel
import com.leviancode.android.gsmbox.utils.*


class NewTemplateDialog : AbstractFullScreenDialog(){
    private lateinit var binding: DialogNewTemplateBinding
    private val viewModel: NewTemplateDialogViewModel by viewModels()
    private val args: NewTemplateDialogArgs by navArgs()
    private lateinit var navController: NavController
    override var saved = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_new_template, container, false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        viewModel.addRecipient()
        binding.viewModel = viewModel
        binding.editTextTemplateName.requestFocus()

        binding.toolbar.apply {
            setNavigationOnClickListener { v: View? ->
                closeDialog()
            }
            setOnMenuItemClickListener { item ->
                saveTemplate()
                closeDialog()
                true
            }
        }

        observe()
    }

    private fun observe(){
        viewModel.saveRecipientDialogLiveEvent.observe(viewLifecycleOwner){ recipient ->
            showSaveRecipientDialog(recipient)
        }

        viewModel.addRecipientLiveEvent.observe(viewLifecycleOwner){ recipient ->
            addRecipientLayout(recipient)
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

        val btnSave = binding.toolbar.menu.findItem(R.id.menu_save)
        viewModel.fieldsNotEmptyLiveEvent.observe(viewLifecycleOwner){
            btnSave.isEnabled = it
        }
    }

    private fun selectContact(recipient: RecipientObservable){
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

    private fun showContactsDialog(recipient: RecipientObservable) {
        getNavigationResult<String>(REQUEST_KEY_SELECTED)?.observe(viewLifecycleOwner){ result ->
            if (!result.isNullOrBlank()){
                recipient.setPhoneNumber(result)
                removeNavigationResult<String>(REQUEST_KEY_SELECTED)
            }
        }

        val action = NewTemplateDialogDirections.actionSelectContact()
        navController.navigate(action)
    }

    private fun selectColor(template: TemplateObservable){
        hideKeyboard()

        ColorPickerDialog(
            requireContext(),
            childFragmentManager,
            template.getTemplateIconColor()
        ).show {
            template.setTemplateIconColor(it)
        }
    }

    private fun showSaveRecipientDialog(recipient: RecipientObservable){
        getNavigationResult<String>(REQUEST_KEY_SAVED)?.observe(viewLifecycleOwner){ result ->
            when (result){
                RESULT_OK -> {
                    showToast(getString(R.string.recipient_saved))
                    hideKeyboard()
                }
            }
            removeNavigationResult<String>(REQUEST_KEY_SAVED)
        }

        val action = NewTemplateDialogDirections.actionNewRecipient(recipient.data.recipientId)
        navController.navigate(action)
    }

    private fun addRecipientLayout(recipient: RecipientObservable) {
        val inflater = LayoutInflater.from(requireContext())
        val recipientBinding = DataBindingUtil.inflate<DialogNewTemplateNumberHolderBinding>(
            inflater, R.layout.dialog_new_template_number_holder, binding.recipientsLayout, true)

        recipientBinding.recipient = recipient
        recipientBinding.viewModel = viewModel

        if (binding.recipientsLayout.childCount > 1){
            recipientBinding.btnRemoveNumber.visibility = View.VISIBLE
            recipientBinding.editTextRecipientNumber.requestFocus()
        }
    }

    private fun removeRecipientLayout(view: View){
        binding.recipientsLayout.removeView(view.parent as ViewGroup)
    }

    override fun isFieldsNotEmpty(): Boolean {
        return viewModel.isFieldsNotEmpty()
    }
    
    private fun saveTemplate(){
        saved = true
        viewModel.saveTemplate(args.groupId)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG = NewTemplateDialog::class.java.simpleName
    }
}