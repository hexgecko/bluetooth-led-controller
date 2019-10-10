package com.example.mvvm.ledcontroller.model

import androidx.lifecycle.MutableLiveData

class FakeLedModel: LedModel {

    override val ledStatus = MutableLiveData<Boolean>().apply { value = null }

    override val busy = MutableLiveData<Boolean>().apply { value = false }

    override val error = MutableLiveData<String>()

    override fun onToggle() {
        if(ledStatus.value == null) {
            ledStatus.value = false
            error.value = "Test error!"
        } else ledStatus.value?.let {
            when(it) {
                true -> ledStatus.value = false
                false -> ledStatus.value = true
            }
        }
    }
}
