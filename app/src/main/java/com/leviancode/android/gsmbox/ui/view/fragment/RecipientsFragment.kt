package com.leviancode.android.gsmbox.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.viewmodel.RecipientViewModel

class RecipientsFragment : Fragment() {
    private lateinit var viewModel: RecipientViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
                ViewModelProvider(this).get(RecipientViewModel::class.java)
        return inflater.inflate(R.layout.fragment_recipients, container, false)
    }
}