package com.bm.purgym.ui.auth.register

import androidx.lifecycle.ViewModel
import com.bm.purgym.data.local.LocalDataHolder
import com.bm.purgym.data.models.AdminModel

class RegisterViewModel(private val localDataHolder: LocalDataHolder) : ViewModel() {
    fun register(adminModel: AdminModel) {
        localDataHolder.registerAdmin(adminModel)
    }
}