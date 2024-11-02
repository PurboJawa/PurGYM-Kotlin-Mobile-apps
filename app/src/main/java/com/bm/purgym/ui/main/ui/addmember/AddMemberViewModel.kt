package com.bm.purgym.ui.main.ui.addmember

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bm.purgym.data.local.LocalDataHolder
import com.bm.purgym.data.models.MemberModel

class AddMemberViewModel(private val localDataHolder: LocalDataHolder) : ViewModel() {
    val uriImage = MutableLiveData<String>()
    val registrationDate = MutableLiveData<Long>()
    val expirationDate = MutableLiveData<Long>()

    fun addMember(memberModel: MemberModel) = localDataHolder.addMember(memberModel)
}