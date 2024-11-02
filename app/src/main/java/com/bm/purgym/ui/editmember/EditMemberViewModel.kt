package com.bm.purgym.ui.editmember

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bm.purgym.data.local.LocalDataHolder
import com.bm.purgym.data.models.MemberModel

class EditMemberViewModel(private val localDataHolder: LocalDataHolder) : ViewModel() {
    val uriImage = MutableLiveData<String>()
    val registrationDate = MutableLiveData<Long>()
    val expirationDate = MutableLiveData<Long>()

    fun editMember(memberModel: MemberModel) = localDataHolder.editMember(memberModel)
    fun deleteMember(memberModel: MemberModel) = localDataHolder.deleteMember(memberModel)
}