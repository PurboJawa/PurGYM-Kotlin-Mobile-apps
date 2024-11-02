package com.bm.purgym.di

import com.bm.purgym.data.local.LocalDataHolder
import com.bm.purgym.ui.auth.login.LoginViewModel
import com.bm.purgym.ui.auth.register.RegisterViewModel
import com.bm.purgym.ui.editmember.EditMemberViewModel
import com.bm.purgym.ui.main.MainViewModel
import com.bm.purgym.ui.main.ui.addmember.AddMemberViewModel
import com.bm.purgym.ui.main.ui.changepassword.ChangePasswordViewModel
import com.bm.purgym.ui.main.ui.home.HomeViewModel
import com.bm.purgym.utils.AppPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dataStoreModule = module {
    factory { AppPreferences.getInstance(androidContext()) }
}

val dataHolderModule = module {
    factory { LocalDataHolder.getInstance(get()) }
}

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { AddMemberViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { EditMemberViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { ChangePasswordViewModel(get()) }
}