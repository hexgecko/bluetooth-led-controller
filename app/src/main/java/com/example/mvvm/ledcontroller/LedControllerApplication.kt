package com.example.mvvm.ledcontroller

import android.app.Application
import com.example.mvvm.ledcontroller.model.LedModel
import com.example.mvvm.ledcontroller.model.ProdLedModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

@Suppress("unused")
class LedControllerApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(module {
                single<LedModel> { ProdLedModel() }
            })
        }
    }
}
