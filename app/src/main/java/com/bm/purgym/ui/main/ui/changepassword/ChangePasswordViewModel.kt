package com.bm.purgym.ui.main.ui.changepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bm.purgym.data.local.LocalDataHolder

class ChangePasswordViewModel(private val localDataHolder: LocalDataHolder) : ViewModel() {
    fun changeAdminPassword(adminId: Int, newPassword: String) {
        localDataHolder.changeAdminPassword(adminId, newPassword)
    }
}