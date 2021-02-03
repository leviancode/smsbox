package com.leviancode.android.gsmbox.ui.view.dialog

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
import com.github.tamir7.contacts.Contact
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.databinding.DialogNewTemplateBinding
import com.leviancode.android.gsmbox.databinding.RecipientNumberHolderBinding
import com.leviancode.android.gsmbox.ui.viewmodel.*
import com.leviancode.android.gsmbox.utils.getNavigationResult


class NewTemplateDialog : AbstractFullScreenDialog(){
    private lateinit var binding: DialogNewTemplateBinding
    private val viewModel: TemplateViewModel by viewModels()
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
        navController = requireParentFragment().findNavController()
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

        viewModel.selectColorLiveEvent.observe(viewLifecycleOwner){ color ->
            selectColor(color)
        }

        viewModel.removeRecipientLiveEvent.observe(viewLifecycleOwner){ view ->
            removeRecipientLayout(view)
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
        getNavigationResult<String>(ContactsBottomSheetDialog.SAVED_REQUEST_KEY)?.observe(viewLifecycleOwner){ result ->
            if (!result.isNullOrBlank()){
                recipient.setPhoneNumber(result)
            }
        }

        hideKeyboard()
        val action = NewTemplateDialogDirections.actionSelectContact()
        navController.navigate(action)

    }

    private fun selectColor(defaultColor: Int){
        hideKeyboard()

        ColorPickerBottomSheet(
            requireContext(),
            childFragmentManager,
            defaultColor
        ).show {
            viewModel.template.setIconColor(it)
        }
    }

    private fun showSaveRecipientDialog(recipient: RecipientObservable){
        getNavigationResult<Boolean>(NewRecipientDialog.SAVED_REQUEST_KEY)?.observe(viewLifecycleOwner){ result ->
            if (result){
                showToast(getString(R.string.recipient_saved))
                recipient.saved = true
                hideKeyboard()
            }
        }

        val action = NewTemplateDialogDirections.actionNewRecipient(recipient.data.id)
        navController.navigate(action)
    }

    private fun addRecipientLayout(recipient: RecipientObservable) {
        val inflater = LayoutInflater.from(requireContext())
        val recipientBinding = DataBindingUtil.inflate<RecipientNumberHolderBinding>(
            inflater, R.layout.recipient_number_holder, binding.recipientsLayout, true)

        recipientBinding.recipient = recipient
        recipientBinding.viewModel = viewModel

        if (binding.recipientsLayout.childCount > 1){
            recipientBinding.btnRemoveNumber.visibility = View.VISIBLE
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