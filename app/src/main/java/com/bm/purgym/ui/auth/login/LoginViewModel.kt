package com.bm.purgym.ui.auth.login

import androidx.lifecycle.ViewModel
import com.bm.purgym.data.local.LocalDataHolder
import com.bm.purgym.data.models.AdminModel

class LoginViewModel(private val localDataHolder: LocalDataHolder) : ViewModel() {
    fun login(adminModel: AdminModel) = localDataHolder.loginAdmin(adminModel)
}