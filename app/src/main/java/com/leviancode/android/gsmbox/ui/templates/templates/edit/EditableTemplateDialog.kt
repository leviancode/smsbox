package com.leviancode.android.gsmbox.ui.templates.templates.edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.recipients.GroupWithRecipients
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.databinding.DialogEditableTemplateBinding
import com.leviancode.android.gsmbox.databinding.ViewNumberHolderBinding
import com.leviancode.android.gsmbox.ui.extra.ColorPickerDialog
import com.leviancode.android.gsmbox.ui.extra.AbstractFullScreenEditableDialog
import com.leviancode.android.gsmbox.utils.*
import com.leviancode.android.gsmbox.utils.extensions.getNavigationResult
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.utils.extensions.removeNavigationResult
import kotlinx.coroutines.launch


class EditableTemplateDialog :
    AbstractFullScreenEditableDialog<DialogEditableTemplateBinding>(R.layout.dialog_editable_template) {
    private lateinit var binding: DialogEditableTemplateBinding
    private val viewModel: EditableTemplateViewModel by viewModels()
    private val args: EditableTemplateDialogArgs by navArgs()
    private var firstLoad: Boolean = true

    override fun setupViews(binding: DialogEditableTemplateBinding) {
        this.binding = binding
        binding.viewModel = viewModel
        if (args.templateId != 0){
            changeTitle()
        } else {
            showKeyboard(binding.editTextTemplateName)
        }
        fetchData()
        observeEvents()
    }

    private fun fetchData() {
        viewModel.loadTemplate(args.templateId, args.groupId).observe(viewLifecycleOwner) {
            binding.template = it.template
            binding.recipients = it.recipients
            binding.executePendingBindings()
        }
    }

    private fun changeTitle() {
        binding.toolbar.title = getString(R.string.edit_template)
    }

    private fun observeEvents() {
        binding.toolbar.setNavigationOnClickListener { closeDialog(RESULT_CANCEL) }

        viewModel.addManyRecipientsViewEvent.observe(viewLifecycleOwner) { list ->
            list.forEach(::addRecipientView)
            firstLoad = false
        }

        viewModel.addSingleRecipientViewEvent.observe(viewLifecycleOwner) { recipient ->
            addRecipientView(recipient)
            firstLoad = false
        }

        viewModel.saveRecipientEvent.observe(viewLifecycleOwner) { recipient ->
            showEditableRecipientDialog(recipient)
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

        viewModel.savedTemplateEvent.observe(viewLifecycleOwner) {
            closeDialog(RESULT_OK)
        }

        viewModel.selectRecipientEvent.observe(viewLifecycleOwner) {
            showSelectRecipientDialog(it.first, it.second)
        }

        viewModel.selectRecipientGroupEvent.observe(viewLifecycleOwner) {
            showSelectRecipientGroupDialog(it)
        }

        binding.switchToRecipientGroup.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
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
                lifecycleScope.launch {
                    viewModel.updateRecipientGroup(result)?.let {
                        updateRecipientsBinding(it)
                    }
                }
            }
            removeNavigationResult<Int>(REQ_SELECT_RECIPIENT_GROUP)
        }

        navigate {
            EditableTemplateDialogDirections.actionSelectRecipientGroup(groupId)
        }
    }

    private fun updateRecipientsBinding(value: GroupWithRecipients){
        binding.recipients = value
        binding.executePendingBindings()
    }

    private fun showSelectRecipientDialog(currentRecipient: Recipient, allTypedPhoneNumbers: List<String>) {
        hideKeyboard()

        getNavigationResult<Recipient?>(REQ_SELECT_RECIPIENT)?.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                viewModel.updateRecipient(currentRecipient, result)
            }
            removeNavigationResult<Recipient?>(REQ_SELECT_RECIPIENT)
        }

        navigate {
            EditableTemplateDialogDirections.actionSelectRecipient(
                currentRecipient.getPhoneNumber(), allTypedPhoneNumbers.toTypedArray()
            )
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
        getNavigationResult<Boolean>(REQ_SAVE_RECIPIENT)?.observe(viewLifecycleOwner) { isSaved ->
            log("recipient save: $isSaved")
            if (isSaved) {
                recipient.isPhoneNumberUnique = false
            }
            removeNavigationResult<Boolean>(REQ_SAVE_RECIPIENT)
        }
        navigate {
            EditableTemplateDialogDirections.actionOpenEditableRecipient(
                recipient.recipientId, recipient.getPhoneNumber()
            )
        }
    }

    private fun addRecipientView(recipient: Recipient) {
        val inflater = LayoutInflater.from(requireContext())
        val recipientBinding = ViewNumberHolderBinding.inflate(
            inflater,
            binding.recipientsLayout,
            true
        )
        recipientBinding.lifecycleOwner = this
        recipientBinding.viewModel = viewModel
        recipientBinding.recipient = recipient

        if (binding.recipientsLayout.childCount > 1) {
            if (!binding.switchToRecipientGroup.isChecked && !firstLoad) {
                showKeyboard(recipientBinding.editTextRecipientNumber)
            }
            recipientBinding.btnRemoveNumber.visibility = View.VISIBLE
        }
        recipientBinding.executePendingBindings()
    }

    private fun removeRecipientView(view: View) {
        val parent = view.parent as ViewGroup
        val index = binding.recipientsLayout.indexOfChild(parent)
        if (index > 0) {
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