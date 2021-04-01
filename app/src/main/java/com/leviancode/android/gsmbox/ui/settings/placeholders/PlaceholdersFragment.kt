package com.leviancode.android.gsmbox.ui.settings.placeholders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.placeholders.Placeholder
import com.leviancode.android.gsmbox.databinding.FragmentPlaceholdersBinding
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu.Companion.DELETE
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu.Companion.EDIT
import com.leviancode.android.gsmbox.ui.extra.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.extra.alertdialogs.DiscardAlertDialog
import com.leviancode.android.gsmbox.ui.extra.alertdialogs.InfoDialog
import com.leviancode.android.gsmbox.utils.extensions.goBack
import com.leviancode.android.gsmbox.utils.extensions.navigate

class PlaceholdersFragment : Fragment() {
    private lateinit var binding: FragmentPlaceholdersBinding
    private val viewModel: PlaceholdersViewModel by viewModels()
    private lateinit var listAdapter: PlaceholderListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceholdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter = PlaceholderListAdapter(viewModel)
        binding.placeholdersRecyclerView.adapter = listAdapter
        binding.viewModel = viewModel
        observeUI()
    }

    private fun observeUI() {
        binding.toolbarPlaceholders.setNavigationOnClickListener { goBack() }
        binding.toolbarPlaceholders.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.menu_info -> openInfoDialog()
            }
            true
        }

        viewModel.placeholders.observe(viewLifecycleOwner){
            listAdapter.submitList(it)
        }

        viewModel.addPlaceholderEvent.observe(viewLifecycleOwner){
            openEditableDialog(it)
        }

        viewModel.onPopupMenuClickEvent.observe(viewLifecycleOwner){
            showPopupMenu(it.first, it.second)
        }

        binding.placeholdersRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            private val fab = binding.fabAddPlaceholder
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy >0 && fab.isShown) fab.hide()
                else if (dy <0 && !fab.isShown) fab.show()
            }
        })
    }

    private fun showPopupMenu(view: View, placeholder: Placeholder) {
        ItemPopupMenu(requireContext(), view).showEditDelete { result ->
            when(result){
                EDIT -> openEditableDialog(placeholder)
                DELETE -> showConfirmationDialog(placeholder)
            }
        }
    }

    private fun showConfirmationDialog(placeholder: Placeholder){
        DeleteConfirmationAlertDialog.show(
            requireContext(),
            getString(R.string.delete_placeholder),
            getString(R.string.delete_placeholder_confirmation)
        ) { confirmed ->
            if (confirmed) viewModel.deletePlaceholder(placeholder)
        }
    }

    private fun openEditableDialog(placeholder: Placeholder) {
        navigate {
            PlaceholdersFragmentDirections.actionOpenEditablePlaceholder(placeholder.copy())
        }
    }

    private fun openInfoDialog() {
        InfoDialog.show(requireContext(), getString(R.string.placeholders_info))
    }
}