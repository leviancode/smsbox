package com.leviancode.android.gsmbox.ui.templates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.leviancode.android.gsmbox.R

class TemplatesFragment : Fragment() {
    private lateinit var templatesViewModel: TemplatesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        templatesViewModel =
                ViewModelProvider(this).get(TemplatesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_templates, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        templatesViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }
}