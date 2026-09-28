package com.example.gymcanamaster

import android.app.Application
import com.example.gymcanamaster.data.AppContainer
import com.example.gymcanamaster.data.AppDataContainer

class GymcanaMasterApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}