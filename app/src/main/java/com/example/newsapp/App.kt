package com.example.newsapp

import android.app.Application
import com.example.newsapp.di.apiModule
import com.example.newsapp.di.appModule
import com.example.newsapp.di.roomDBmodule
import com.example.newsapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application(){
    override fun onCreate() {
        super.onCreate()

        setUpKoin()
    }

    private fun setUpKoin() {
        startKoin {
            androidContext(this@App)
            modules(
                roomDBmodule,
                viewModelModule,
                appModule,
                apiModule
            )
        }
    }
}