package com.leviancode.android.gsmbox.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.viewmodel.RecipientsViewModel

class RecipientsFragment : Fragment() {
    private lateinit var viewModel: RecipientsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
                ViewModelProvider(this).get(RecipientsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recipients, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        viewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }
}