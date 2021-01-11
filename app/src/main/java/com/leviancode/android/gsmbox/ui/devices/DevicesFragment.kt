package com.leviancode.android.gsmbox.ui.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.leviancode.android.gsmbox.R

class DevicesFragment : Fragment() {
    private lateinit var devicesViewModel: DevicesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        devicesViewModel =
                ViewModelProvider(this).get(DevicesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_devices, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        devicesViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }
}