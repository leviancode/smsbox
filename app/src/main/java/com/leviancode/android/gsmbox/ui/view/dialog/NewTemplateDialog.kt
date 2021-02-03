package com.leviancode.android.gsmbox.ui.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.databinding.DialogNewTemplateBinding
import com.leviancode.android.gsmbox.databinding.RecipientNumberHolderBinding
import com.leviancode.android.gsmbox.ui.viewmodel.*


class NewTemplateDialog : AbstractFullScreenDialog(){
    private lateinit var binding: DialogNewTemplateBinding
    private val templateViewModel: TemplateViewModel by viewModels()
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
        templateViewModel.addRecipient()
        navController = requireParentFragment().findNavController()
        binding.viewModel = templateViewModel
        binding.editTextTemplateName.requestFocus()

        binding.toolbar.apply {
            title = getString(R.string.title_new_template)
            setNavigationOnClickListener { v: View? ->
                closeDialog()
            }
            setOnMenuItemClickListener { item ->
                saveTemplate()
                closeDialog()
                true
            }
        }


        val btn = binding.toolbar.menu.findItem(R.id.menu_save)
        binding.editTextTemplateName.doOnTextChanged { text, start, before, count ->
            btn.isEnabled = count > 0
        }

        observe()
    }

    private fun observe(){
        templateViewModel.saveRecipientDialogLiveEvent.observe(viewLifecycleOwner){
            showSaveRecipientDialog(it)
        }

        templateViewModel.addRecipientLiveEvent.observe(viewLifecycleOwner){
            addRecipientLayout(it)
        }

        templateViewModel.chooseColorLiveEvent.observe(viewLifecycleOwner){
            chooseColor(it)
        }
    }

    private fun chooseColor(defaultColor: Int){
        hideKeyboard()

        ColorPickerBottomSheet(
            requireContext(),
            childFragmentManager,
            defaultColor
        ).show {
            templateViewModel.template.setIconColor(it)
        }
    }

    private fun showSaveRecipientDialog(recipientId: String){
        /*val editText = TextInputEditText(requireContext()).apply {
            doOnTextChanged { text, start, before, count ->
                recipient.setName(text?.toString() ?: "")
            }
        }
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.enter_the_name, recipient.getPhoneNumber()))
            .setView(editText)
            .setNegativeButton(getString(R.string.cancel)){ dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton(getString(R.string.save)){ _, _ ->
                templateViewModel.saveRecipient(recipient)
                showToast(getString(R.string.recipient_saved))
            }.show()*/

        val action = NewTemplateDialogDirections.actionNewRecipient(recipientId)
        navController.navigate(action)
    }

    // TODO save button invisible after save template
    private fun addRecipientLayout(recipient: RecipientObservable) {
        val inflater = LayoutInflater.from(requireContext())
        val recipientBinding = DataBindingUtil.inflate<RecipientNumberHolderBinding>(
            inflater, R.layout.recipient_number_holder, binding.recipientsLayout, true)

        recipientBinding.recipient = recipient
        recipientBinding.viewModel = templateViewModel
        /*recipientBinding.editTextRecipientNumber.doOnTextChanged { text, start, before, count ->
            recipientBinding.btnRecipientSave.visibility =
                if (text.isNullOrBlank()) {
                    View.INVISIBLE
                } else {
                    View.VISIBLE
                }
        }*/
    }

    override fun isFieldsEmpty(): Boolean {
        return templateViewModel.template.isFieldsEmpty()
    }
    
    private fun saveTemplate(){
        saved = true
        templateViewModel.saveTemplate(args.groupId)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG = NewTemplateDialog::class.java.simpleName
    }
}