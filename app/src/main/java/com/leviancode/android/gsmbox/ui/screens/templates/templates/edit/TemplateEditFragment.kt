package com.leviancode.android.gsmbox.ui.screens.templates.templates.edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.utils.*
import com.leviancode.android.gsmbox.databinding.FragmentTemplateEditBinding
import com.leviancode.android.gsmbox.databinding.ViewNumberHolderBinding
import com.leviancode.android.gsmbox.ui.base.BaseFragment
import com.leviancode.android.gsmbox.ui.dialogs.ColorPickerDialog
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientUI
import com.leviancode.android.gsmbox.ui.screens.recipients.groups.select.SelectRecipientGroupDialog
import com.leviancode.android.gsmbox.ui.screens.templates.templates.edit.adapters.DropdownAdapter
import com.leviancode.android.gsmbox.ui.screens.templates.templates.edit.adapters.SpaceTokenizer
import com.leviancode.android.gsmbox.utils.extensions.getNavigationResult
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.utils.extensions.removeNavigationResult
import org.koin.androidx.viewmodel.ext.android.viewModel


class TemplateEditFragment : BaseFragment<FragmentTemplateEditBinding>(R.layout.fragment_template_edit) {
    private val viewModel: TemplateEditViewModel by viewModel()
    private val args: TemplateEditFragmentArgs by navArgs()
    private var firstLoad: Boolean = true

    override val showConfirmationDialogBeforeQuit: Boolean
        get() = viewModel.isDataSavedOrNotChanged()

    override fun onCreated() {
        binding.viewModel = viewModel
        if (args.templateId != 0){
            changeTitle()
        } else {
            showKeyboard()
        }
        observeData()
        observeEvents()
        setupPlaceholdersAutocomplete()
    }


    private fun setupPlaceholdersAutocomplete() {
        viewModel.placeholders.observe(viewLifecycleOwner){ list ->
            val adapter = DropdownAdapter(requireContext(), list)
            binding.editTextTemplateMessage.apply {
                setAdapter(adapter)
                setTokenizer(SpaceTokenizer())
            }
        }

    }

    private fun observeData() {
        if (args.templateId != 0){
            viewModel.loadTemplate(args.templateId)
        } else {
            viewModel.createEmptyTemplate(args.groupId)
        }
        viewModel.data.observe(viewLifecycleOwner){
            binding.template = it
            binding.executePendingBindings()
        }
    }

    private fun changeTitle() {
        binding.toolbar.title = getString(R.string.edit_template)
    }

    private fun observeEvents() {
        binding.toolbar.setNavigationOnClickListener { close() }

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

        viewModel.quitEvent.observe(viewLifecycleOwner) { close() }

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

    private fun showSelectRecipientGroupDialog(group: RecipientGroupUI) {
        hideKeyboard()

        SelectRecipientGroupDialog.show(childFragmentManager, group.id) { selected ->
            viewModel.updateRecipientGroup(selected)
        }
    }

    private fun showSelectRecipientDialog(
        currentRecipient: RecipientUI,
        allTypedPhoneNumbers: List<String>
    ) {
        hideKeyboard()

        getNavigationResult<RecipientUI?>(REQ_SELECT_RECIPIENT)?.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                viewModel.updateRecipient(currentRecipient, result)
            }
            removeNavigationResult<RecipientUI?>(REQ_SELECT_RECIPIENT)
        }

        navigate {
            TemplateEditFragmentDirections.actionSelectRecipient(
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

    private fun showEditableRecipientDialog(recipient: RecipientUI) {
        getNavigationResult<RecipientUI>(REQ_SAVE_RECIPIENT)?.observe(viewLifecycleOwner) { savedRecipient ->
            viewModel.updateRecipient(recipient, savedRecipient)
            removeNavigationResult<RecipientUI>(REQ_SAVE_RECIPIENT)
        }
        navigate {
            TemplateEditFragmentDirections.actionOpenEditableRecipient(
                recipient.id, recipient.getPhoneNumber()
            )
        }
    }

    private fun addRecipientView(recipient: RecipientUI) {
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
                recipientBinding.editTextRecipientNumber.showKeyboard()
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
            child.showKeyboard()
        }
        binding.recipientsLayout.removeView(parent)
    }

    private fun removeAllRecipientViews() {
        binding.recipientsLayout.removeAllViews()
    }

}