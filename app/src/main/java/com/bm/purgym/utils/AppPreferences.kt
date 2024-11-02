package com.bm.purgym.utils

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bm.purgym.data.models.AdminModel
import com.bm.purgym.data.models.MemberModel
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "appPreferences")

class AppPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    fun getAdminListPreferences() =
        dataStore.data.map { it[ADMIN_LIST_PREFERENCES] ?: preferencesDefaultValue }

    fun getMemberListPreferences() =
        dataStore.data.map { it[MEMBER_LIST_PREFERENCES] ?: preferencesDefaultValue }

    suspend fun saveAdminList(
        adminList: MutableList<AdminModel>
    ) {
        val adminListJson = Json.encodeToString(adminList)
        dataStore.edit { prefs ->
            prefs[ADMIN_LIST_PREFERENCES] = adminListJson
        }
    }

    suspend fun saveMemberList(
        memberList: MutableList<MemberModel>
    ) {
        val memberListJson = Json.encodeToString(memberList)

        dataStore.edit { prefs ->
            prefs[MEMBER_LIST_PREFERENCES] = memberListJson
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppPreferences? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            val instance = AppPreferences(context.dataStore)
            INSTANCE = instance
            instance
        }

        private val ADMIN_LIST_PREFERENCES = stringPreferencesKey("adminList")
        private val MEMBER_LIST_PREFERENCES = stringPreferencesKey("memberList")

        const val preferencesDefaultValue: String = "[]"
    }
}