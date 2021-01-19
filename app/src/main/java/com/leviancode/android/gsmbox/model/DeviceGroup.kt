package com.leviancode.android.gsmbox.model

import java.util.*
import kotlin.collections.ArrayList

data class DeviceGroup(
    var id: String = UUID.randomUUID().toString(),
    var name: String
) : ArrayList<Device>()
