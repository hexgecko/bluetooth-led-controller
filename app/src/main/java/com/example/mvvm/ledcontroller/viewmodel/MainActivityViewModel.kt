package com.example.mvvm.ledcontroller.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.mvvm.ledcontroller.R
import com.example.mvvm.ledcontroller.model.LedModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainActivityViewModel: ViewModel(), KoinComponent {

    private val ledModel by inject<LedModel>()

    val ledStatusText = Transformations.map(ledModel.ledStatus) { status ->
        if(status == true) {
            R.string.text_led_on
        } else {
            R.string.text_led_off
        }
    }

    val toggleEnable = Transformations.map(ledModel.busy) { busy -> !busy }

    val error = ledModel.error

    fun onToggle() = ledModel.onToggle()

    init {
        ledModel.ledStatus.observeForever { }
    }
}
