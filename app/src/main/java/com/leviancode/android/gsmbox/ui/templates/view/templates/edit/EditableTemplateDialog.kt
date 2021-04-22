package com.leviancode.android.gsmbox.ui.templates.view.templates.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.databinding.DialogEditableTemplateBinding
import com.leviancode.android.gsmbox.databinding.DialogEditableTemplateNumberHolderBinding
import com.leviancode.android.gsmbox.ui.extra.ColorPickerDialog
import com.leviancode.android.gsmbox.ui.templates.view.AbstractFullScreenDialog
import com.leviancode.android.gsmbox.utils.helpers.TextUniqueWatcher
import com.leviancode.android.gsmbox.utils.*
import com.leviancode.android.gsmbox.utils.extensions.getNavigationResult
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.utils.extensions.removeNavigationResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class EditableTemplateDialog : AbstractFullScreenDialog() {
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
        setupViews()
        observeUI()
    }

    private fun setupViews(){
        setTitle(args.templateId != 0)
        binding.viewModel = viewModel
        viewModel.loadTemplate(args.templateId, args.groupId).observe(viewLifecycleOwner) {
            binding.template = it.template
            binding.recipients = it.recipients
            binding.executePendingBindings()
        }
        showKeyboard(binding.editTextTemplateName)
    }

    private fun setTitle(editMode: Boolean) {
        if (editMode) binding.toolbar.title = getString(R.string.edit_template)
    }

    private fun setTextUniqueWatcher() {
        val textWatcher = TextUniqueWatcher { isUnique ->
            viewModel.setTemplateNameUnique(isUnique)
        }
        viewModel.getNamesExclusiveById(args.templateId)
            .observe(viewLifecycleOwner) {
                textWatcher.wordList = it
            }
        binding.editTextTemplateName.addTextChangedListener(textWatcher)
    }

    private fun observeUI() {
     //   setTextUniqueWatcher()

        binding.toolbar.setNavigationOnClickListener { closeDialog(RESULT_CANCEL) }

        viewModel.addAllRecipientsViewEvent.observe(viewLifecycleOwner){ list ->
            list.forEach(::addRecipientView)
        }
        viewModel.getRecipientNumbersLiveData().observe(viewLifecycleOwner){ list ->
            updateAutoCompleteList(list)
        }

        viewModel.saveRecipientEvent.observe(viewLifecycleOwner) { recipient ->
            showEditableRecipientDialog(recipient)
        }

        viewModel.addRecipientViewEvent.observe(viewLifecycleOwner) { recipient ->
            addRecipientView(recipient)
        }

        viewModel.removeRecipientViewEvent.observe(viewLifecycleOwner) { view ->
            removeRecipientView(view)
        }

        viewModel.removeAllRecipientViewsEvent.observe(viewLifecycleOwner) {
            removeAllRecipientViews()
        }

        viewModel.selectColorEvent.observe(viewLifecycleOwner) { color ->
            selectColor(color)
        }

        viewModel.savedEvent.observe(viewLifecycleOwner) {
            closeDialog(RESULT_OK)
        }

        viewModel.selectRecipientEvent.observe(viewLifecycleOwner){
            showSelectRecipientDialog(it)
        }

        viewModel.selectRecipientGroupEvent.observe(viewLifecycleOwner){
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

    private fun showSelectRecipientGroupDialog(groupId: Int) {
        hideKeyboard()

        getNavigationResult<Int>(REQ_SELECT_RECIPIENT_GROUP)?.observe(viewLifecycleOwner) { result ->
            if (result != 0 && result != groupId) {
                viewModel.setRecipientGroup(result)
            }
            removeNavigationResult<Int>(REQ_SELECT_RECIPIENT_GROUP)
        }

        navigate {
            EditableTemplateDialogDirections.actionSelectRecipientGroup(groupId)
        }
    }

    private fun showSelectRecipientDialog(recipient: Recipient) {
        hideKeyboard()

        getNavigationResult<Recipient>(REQ_SELECT_RECIPIENT)?.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                viewModel.updateRecipient(recipient, result)
            }
            removeNavigationResult<Recipient>(REQ_SELECT_RECIPIENT)
        }

        navigate {
            EditableTemplateDialogDirections.actionSelectRecipient(recipient.recipientId)
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
            DataBindingUtil.getBinding<DialogEditableTemplateNumberHolderBinding>(it)?.apply {
                autoCompleteList = list
                executePendingBindings()
            }
        }
    }

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

    private fun showEditableRecipientDialog(recipient: Recipient) {
        navigate {
            EditableTemplateDialogDirections.actionOpenEditableRecipient(
                recipient.recipientId, recipient.getPhoneNumber()
            )
        }
    }

    private fun addRecipientView(recipient: Recipient) {
        val inflater = LayoutInflater.from(requireContext())
        val recipientBinding = DataBindingUtil.inflate<DialogEditableTemplateNumberHolderBinding>(
            inflater,
            R.layout.dialog_editable_template_number_holder,
            binding.recipientsLayout,
            true
        )

        recipientBinding.recipient = recipient
        recipientBinding.viewModel = viewModel
        lifecycleScope.launch {
            recipientBinding.autoCompleteList = viewModel.getRecipientNumbers()
        }

        if (binding.recipientsLayout.childCount > 1) {
            if(!binding.switchToRecipientGroup.isChecked && args.templateId == 0){
                showKeyboard(recipientBinding.editTextRecipientNumber)
            }
            recipientBinding.btnRemoveNumber.visibility = View.VISIBLE
        }
        recipientBinding.executePendingBindings()
    }

    private fun removeRecipientView(view: View) {
        val parent = view.parent as ViewGroup
        val index = binding.recipientsLayout.indexOfChild(parent)
        if (index > 0){
            val child = binding.recipientsLayout.getChildAt(index - 1)
                .findViewById<View>(R.id.edit_text_recipient_number)
            showKeyboard(child)
        }
        binding.recipientsLayout.removeView(parent)
    }

    private fun removeAllRecipientViews() {
        binding.recipientsLayout.removeAllViews()
    }

    override fun isDataEdited(): Boolean {
        return viewModel.isTemplateEdited()
    }

}