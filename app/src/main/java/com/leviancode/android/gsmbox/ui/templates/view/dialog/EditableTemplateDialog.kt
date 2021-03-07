package com.leviancode.android.gsmbox.ui.templates.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientObservable
import com.leviancode.android.gsmbox.databinding.DialogEditableTemplateBinding
import com.leviancode.android.gsmbox.databinding.DialogEditableTemplateNumberHolderBinding
import com.leviancode.android.gsmbox.ui.templates.viewmodel.EditableTemplateViewModel
import com.leviancode.android.gsmbox.utils.*


class EditableTemplateDialog : AbstractFullScreenDialog() {
    private lateinit var binding: DialogEditableTemplateBinding
    private val viewModel: EditableTemplateViewModel by viewModels()
    private val args: EditableTemplateDialogArgs by navArgs()
    private var phoneNumberList = listOf<String>()

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
        binding.viewModel = viewModel

        setupViews()
        observeUI()
    }

    private fun setupViews(){
        binding.toolbar.apply {
            args.template.name.let {
                if (it.isNotBlank()) title = it
            }
            setNavigationOnClickListener { closeDialog(RESULT_CANCEL) }
        }

        args.template.recipients.ifNotEmpty { list ->
            list.forEach { addNumberField(RecipientObservable(it)) }
        }
        viewModel.setTemplate(args.template)
        showKeyboard(binding.editTextTemplateName)
    }

    private fun observeUI() {
        viewModel.recipients.observe(viewLifecycleOwner){ list ->
            phoneNumberList = list.map { it.phoneNumber }
            updateAutoCompleteList(phoneNumberList)
        }

        viewModel.saveRecipientLiveEvent.observe(viewLifecycleOwner) { recipient ->
            showEditableRecipientDialog(recipient)
        }

        viewModel.addNumberFieldLiveEvent.observe(viewLifecycleOwner) { recipient ->
            addNumberField(recipient)
        }

        viewModel.removeRecipientLiveEvent.observe(viewLifecycleOwner) { view ->
            removeRecipientLayout(view)
        }

        viewModel.removeAllRecipientLiveEvent.observe(viewLifecycleOwner) {
            removeAllRecipientLayouts()
        }

        viewModel.selectColorLiveEvent.observe(viewLifecycleOwner) { color ->
            selectColor(color)
        }

        viewModel.savedLiveEvent.observe(viewLifecycleOwner) {
            closeDialog(RESULT_OK)
        }

        viewModel.selectRecipientLiveEvent.observe(viewLifecycleOwner){
            showSelectRecipientDialog(it.getRecipientId())
        }

        viewModel.selectRecipientGroupLiveEvent.observe(viewLifecycleOwner){
            showSelectRecipientGroupDialog(it)
        }

        binding.switchToRecipientGroup.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                hideRecipientsLayout()
                showRecipientGroupLayout()
            } else {
                hideRecipientGroupLayout()
                showRecipientsLayout()
            }
            viewModel.recipientGroupMode = isChecked
        }
    }

    private fun showSelectRecipientGroupDialog(groupName: String) {
        hideKeyboard()

        getNavigationResult<String>(REQUEST_SELECTED)?.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                viewModel.setRecipientGroup(result)
                removeNavigationResult<String>(REQUEST_SELECTED)
            }
        }

        navigate {
            EditableTemplateDialogDirections.actionSelectRecipientGroup(groupName, false)
        }
    }

    private fun showSelectRecipientDialog(recipientId: String) {
        hideKeyboard()

        getNavigationResult<Recipient>(REQUEST_SELECT_RECIPIENT)?.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                viewModel.addRecipient(result)
                removeNavigationResult<Recipient>(REQUEST_SELECT_RECIPIENT)
            }
        }

        navigate{
            EditableTemplateDialogDirections.actionSelectRecipient(recipientId)
        }
    }

    private fun showRecipientsLayout() {
        binding.recipientsLayout.visibility = View.VISIBLE
        binding.btnTemplateAddRecipient.isEnabled = true
    }

    private fun hideRecipientsLayout() {
        binding.recipientsLayout.visibility = View.GONE
        binding.btnTemplateAddRecipient.isEnabled = false
    }

    private fun showRecipientGroupLayout() {
        binding.recipientGroupLayout.visibility = View.VISIBLE
    }

    private fun hideRecipientGroupLayout() {
        binding.recipientGroupLayout.visibility = View.GONE
    }

    private fun updateAutoCompleteList(list: List<String>){
        binding.recipientsLayout.children.forEach {
            val bindingView =
                DataBindingUtil.getBinding<DialogEditableTemplateNumberHolderBinding>(it)
            bindingView?.autoCompleteList = list
            bindingView?.executePendingBindings()
        }
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

    private fun showEditableRecipientDialog(recipient: Recipient) {
        navigate {
            EditableTemplateDialogDirections.actionOpenEditableRecipient(recipient)
        }
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
        recipientBinding.autoCompleteList = phoneNumberList
        recipientBinding.viewModel = viewModel

        if (binding.recipientsLayout.childCount > 1) {
            if(!binding.switchToRecipientGroup.isChecked){
                showKeyboard(recipientBinding.editTextRecipientNumber)
            }
            recipientBinding.btnRemoveNumber.visibility = View.VISIBLE
        }
        recipientBinding.executePendingBindings()
    }

    private fun removeRecipientLayout(view: View) {
        val parent = view.parent as ViewGroup
        val index = binding.recipientsLayout.indexOfChild(parent)
        val child = binding.recipientsLayout.getChildAt(index - 1)
            .findViewById<View>(R.id.edit_text_recipient_number)
        showKeyboard(child)
        binding.recipientsLayout.removeView(parent)
    }

    private fun removeAllRecipientLayouts() {
        binding.recipientsLayout.removeAllViews()
    }

    override fun isDataEdited(): Boolean {
        return viewModel.isTemplateEdited()
    }

    companion object {
        private val TAG = EditableTemplateDialog::class.java.simpleName
    }
}