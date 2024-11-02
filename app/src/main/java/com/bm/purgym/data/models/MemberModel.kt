package com.bm.purgym.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class MemberModel(
    var memberId: Int = 0,
    var profilePic: String = "",
    var name: String = "",
    var gender: String = "",
    var age: String = "",
    var height: String = "",
    var weight: String = "",
    var telp: String = "",
    var address: String = "",
    var registrationDate: Long = 0L,
    var expirationDate: Long = 0L,
) : Parcelable