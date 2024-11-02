package com.bm.purgym.data.local

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.bm.purgym.data.models.AdminModel
import com.bm.purgym.data.models.MemberModel
import com.bm.purgym.utils.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class LocalDataHolder private constructor(
    private val appPreferences: AppPreferences
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _adminList = MutableLiveData<MutableList<AdminModel>>(mutableListOf())
    private val _memberList = MutableLiveData<MutableList<MemberModel>>(mutableListOf())

    init {
        val adminListPreferences = runBlocking {
            Json.decodeFromString(
                appPreferences.getAdminListPreferences().first()
            ) as MutableList<AdminModel>
        }
        val memberListPreferences = runBlocking {
            Json.decodeFromString(
                appPreferences.getMemberListPreferences().first()
            ) as MutableList<MemberModel>
        }

        _adminList.value = adminListPreferences
        _memberList.value = memberListPreferences
    }

    val adminList: LiveData<MutableList<AdminModel>> get() = _adminList
    val memberList: LiveData<MutableList<MemberModel>> get() = _memberList

    fun loginAdmin(adminModel: AdminModel)= _adminList.value?.any { it.username == adminModel.username && it.password == adminModel.password }
            ?: false


    fun registerAdmin(adminModel: AdminModel) {
        coroutineScope.launch {
            _adminList.value?.let {
                val idx = if (it.isEmpty()) {
                    0
                } else {
                    it.last().adminId + 1
                }
                adminModel.adminId = idx
                _adminList.value?.add(adminModel)
                appPreferences.saveAdminList(_adminList.value ?: mutableListOf())
            } ?: run {
                adminModel.adminId = 0
                _adminList.value?.add(adminModel)

                appPreferences.saveAdminList(_adminList.value ?: mutableListOf())
            }
        }
    }

    fun changeAdminPassword(adminId: Int, newPassword: String) {
        coroutineScope.launch {
            val adminList = _adminList.value?.toMutableList() ?: mutableListOf()
            val adminIndex = adminList.indexOfFirst { it.adminId == adminId }

            if (adminIndex != -1) {
                val updatedAdmin = adminList[adminIndex].copy(password = newPassword)
                adminList[adminIndex] = updatedAdmin
                _adminList.postValue(adminList)
                appPreferences.saveAdminList(_adminList.value ?: mutableListOf())
            } else {
                throw IllegalArgumentException("Admin with ID $adminId not found.")
            }
        }
    }

    fun addMember(memberModel: MemberModel) {
        coroutineScope.launch {
            _memberList.value?.let {
                val idx = if (it.isEmpty()) {
                    0
                } else {
                    it.last().memberId + 1
                }
                memberModel.memberId = idx
                _memberList.value?.add(memberModel)
                appPreferences.saveMemberList(_memberList.value ?: mutableListOf())
            } ?: run {
                memberModel.memberId = 0
                _memberList.value?.add(memberModel)
                appPreferences.saveMemberList(_memberList.value ?: mutableListOf())
            }
        }
    }

    fun editMember(memberModel: MemberModel) {
        val index = _memberList.value?.indexOfFirst { it.memberId == memberModel.memberId } ?: 0
        if (index != -1) {
            coroutineScope.launch {
                _memberList.value?.set(index, memberModel)
                _memberList.postValue(_memberList.value)

                appPreferences.saveMemberList(_memberList.value ?: mutableListOf())
            }
        } else {
            throw IllegalArgumentException("Member with ID ${memberModel.memberId} not found.")
        }
    }

    fun deleteMember(memberModel: MemberModel) {
        coroutineScope.launch {
            _memberList.value?.remove(memberModel)
            appPreferences.saveMemberList(_memberList.value ?: mutableListOf())
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LocalDataHolder? = null

        fun getInstance(appPreferences: AppPreferences): LocalDataHolder {
            return INSTANCE ?: synchronized(this) {
                val instance = LocalDataHolder(appPreferences)
                INSTANCE = instance
                instance
            }
        }
    }
}
