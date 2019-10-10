package com.example.mvvm.ledcontroller.model

import androidx.lifecycle.LiveData

interface LedModel {
    val ledStatus: LiveData<Boolean>
    val busy: LiveData<Boolean>
    val error: LiveData<String>
    fun onToggle()
}
