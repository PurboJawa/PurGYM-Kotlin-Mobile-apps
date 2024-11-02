package com.bm.purgym.ui.main.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bm.purgym.data.local.LocalDataHolder
import com.bm.purgym.data.models.MemberModel
import com.bm.purgym.utils.Constants.expirationChecker

class HomeViewModel(
    private val localDataHolder: LocalDataHolder,
) : ViewModel() {
    val memberType = MutableLiveData(0)
    val memberList = MutableLiveData<MutableList<MemberModel>>(mutableListOf())
    val currentTimeMillis = MutableLiveData(System.currentTimeMillis())

    init {
        getAllMembers()
    }

    fun searchMember(name: String) {
        val searchedMember = memberList.value?.filter {
            it.name.contains(name)
        }?.toMutableList()
        memberList.postValue(searchedMember ?: mutableListOf())
    }

    fun getAllMembers() {
        val fetchedMember = localDataHolder.memberList.value
        memberList.postValue(fetchedMember ?: mutableListOf())
    }

    fun getActiveMembers() {
        val fetchedMember = localDataHolder.memberList.value
        val newMemberList = mutableListOf<MemberModel>()
        fetchedMember?.let {
            it.forEach { member ->
                if (expirationChecker(member.expirationDate, currentTimeMillis.value ?: 0L)) {
                    newMemberList.add(member)
                }
            }
        }
        memberList.postValue(newMemberList)
    }

    fun getUnactiveMembers() {
        val fetchedMember = localDataHolder.memberList.value
        val newMemberList = mutableListOf<MemberModel>()
        fetchedMember?.let {
            it.forEach { member ->
                if (!expirationChecker(member.expirationDate, currentTimeMillis.value ?: 0L)) {
                    newMemberList.add(member)
                }
            }
        }
        memberList.postValue(newMemberList)
    }
}