package com.example.prvaspiralaeldarbuzadzic19398

import android.app.Application
import android.content.Context

class App : Application() {

    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    companion object {
        private var instance: App? = null

        fun applicationContext(): Context {
            return instance as App
        }
    }
}
