package com.bm.purgym.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bm.purgym.data.local.LocalDataHolder

class MainViewModel(private val localDataHolder: LocalDataHolder) : ViewModel() {
    fun getAdminUsername(adminId: Int) =
        localDataHolder.adminList.value?.find { it.adminId == adminId }?.username ?: "Admin"
}