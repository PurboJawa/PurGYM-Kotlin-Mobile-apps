package com.bm.purgym.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class AdminModel(
    var adminId: Int = 0,
    var username: String = "",
    var password: String = "",
) : Parcelable