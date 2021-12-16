package com.leviancode.android.gsmbox.ui.screens.templates.templates.edit

import androidx.core.view.isVisible
import androidx.fragment.app.clearFragmentResultListener
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentTemplateEditBinding
import com.leviancode.android.gsmbox.databinding.ViewNumberHolderBinding
import com.leviancode.android.gsmbox.ui.base.BaseFragment
import com.leviancode.android.gsmbox.ui.base.BaseListAdapter
import com.leviancode.android.gsmbox.ui.dialogs.ColorPickerDialog
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientUI
import com.leviancode.android.gsmbox.ui.screens.recipients.groups.select.SelectRecipientGroupDialog
import com.leviancode.android.gsmbox.ui.screens.recipients.recipients.dialog.RecipientSelectListDialog
import com.leviancode.android.gsmbox.ui.screens.templates.templates.edit.adapters.DropdownAdapter
import com.leviancode.android.gsmbox.ui.screens.templates.templates.edit.adapters.SpaceTokenizer
import com.leviancode.android.gsmbox.utils.REQ_SAVE_RECIPIENT_ID
import com.leviancode.android.gsmbox.utils.extensions.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class TemplateEditFragment : BaseFragment<FragmentTemplateEditBinding>(R.layout.fragment_template_edit) {
    private val viewModel: TemplateEditViewModel by viewModel()
    private val args: TemplateEditFragmentArgs by navArgs()
    private var firstLoad: Boolean = true

    override val showConfirmationDialogBeforeQuit: Boolean
        get() = !viewModel.isDataSavedOrNotChanged()

    override val showKeyboardOnStarted: Boolean = true

    override fun onCreated() {
        binding.viewModel = viewModel
        if (args.templateId != 0) {
            changeTitle()
        }
        observeData()
        observeEvents()
        setupPlaceHoldersDropDown()
    }

    private fun setupPlaceHoldersDropDown() {
        viewModel.placeholders.observe(viewLifecycleOwner) { list ->
            val adapter = DropdownAdapter(requireContext(), list)
            binding.editTextTemplateMessage.apply {
                setAdapter(adapter)
                setTokenizer(SpaceTokenizer())
            }
        }

    }

    private fun observeData() {
        viewModel.loadTemplate(args.groupId, args.templateId).observe(viewLifecycleOwner) {
            binding.template = it
            binding.executePendingBindings()
        }
    }

    private fun changeTitle() {
        binding.toolbar.title = getString(R.string.edit_template)
    }

    private fun observeEvents() {
        binding.toolbar.setNavigationOnClickListener { close() }

        viewModel.apply {
            addRecipientEvent.observe(viewLifecycleOwner) { recipient ->
                addRecipientView(recipient)
                firstLoad = false
            }
            updateRecipientsEvent.observe(viewLifecycleOwner) { recipients ->
                updateRecipientViews(recipients)
                firstLoad = false
            }
            saveRecipientEvent.observe(viewLifecycleOwner) { recipient ->
                showEditRecipientFragment(recipient)
            }
            selectColorEvent.observe(viewLifecycleOwner) { color ->
                selectColor(color)
            }
            quitEvent.observe(viewLifecycleOwner) { close() }

            selectRecipientEvent.observe(viewLifecycleOwner) {
                showSelectRecipientDialog(it.first, it.second)
            }
            selectRecipientGroupEvent.observe(viewLifecycleOwner) {
                showSelectRecipientGroupDialog(it)
            }
        }
    }

    private fun showSelectRecipientGroupDialog(group: RecipientGroupUI) {
        SelectRecipientGroupDialog.show(childFragmentManager, group.id) { selected ->
            viewModel.updateRecipientGroup(selected)
        }
    }

    private fun showSelectRecipientDialog(
        currentRecipient: RecipientUI,
        allTypedPhoneNumbers: List<String>
    ) {
        RecipientSelectListDialog.show(
            childFragmentManager,
            currentRecipient.getPhoneNumber(),
            allTypedPhoneNumbers
        ) { selected ->
            viewModel.updateRecipient(currentRecipient, selected)
        }
    }

    private fun selectColor(color: String) {
        ColorPickerDialog(
            requireContext(),
            childFragmentManager,
            color
        ).show {
            viewModel.setIconColor(it)
        }
    }

    private fun showEditRecipientFragment(recipient: RecipientUI) {
        setNavigationResultListener<Int>(REQ_SAVE_RECIPIENT_ID) { id ->
            viewModel.updateRecipient(recipient, id)
            clearFragmentResultListener(REQ_SAVE_RECIPIENT_ID)
        }

        navigate {
            TemplateEditFragmentDirections.actionOpenEditableRecipient(
                phoneNumber = recipient.getPhoneNumber(),
                recipientName = recipient.getName()
            )
        }
    }

    fun updateRecipientViews(recipients: List<RecipientUI>){
        binding.recipientsLayout.removeAllViews()
        recipients.forEach(::addRecipientView)
    }

    private fun addRecipientView(recipient: RecipientUI) {
        val showKeyboard = !binding.switchToRecipientGroup.isChecked && !firstLoad
        binding.recipientsLayout.addRecipientView(recipient, viewModel,viewLifecycleOwner, showKeyboard)
    }

}