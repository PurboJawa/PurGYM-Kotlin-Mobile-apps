package com.bm.purgym

import android.app.Application
import com.bm.purgym.di.dataHolderModule
import com.bm.purgym.di.dataStoreModule
import com.bm.purgym.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PurGYM: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PurGYM)
            modules(
                dataStoreModule,
                dataHolderModule,
                viewModelModule
            )
        }
    }
}