package com.example.mvvm.ledcontroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvm.ledcontroller.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        val ledStatus = findViewById<TextView>(R.id.led_status)
        bindStatusText(ledStatus, viewModel.ledStatusText)

        val toggle = findViewById<Button>(R.id.toggle)
        bindToggleClick(toggle, viewModel::onToggle)
        bindToggleEnable(toggle, viewModel.toggleEnable)

        bindError(viewModel.error)
    }

    private fun bindStatusText(textView: TextView, resId: LiveData<Int>) {
        resId.observe(this, Observer {
            textView.setText(it)
        })
    }

    private fun bindToggleClick(button: Button, callback: () -> Unit) {
        button.setOnClickListener { callback() }
    }

    private fun bindToggleEnable(button: Button, enable: LiveData<Boolean>) {
        enable.observe(this, Observer { button.isEnabled = it })
    }

    private fun bindError(error: LiveData<String>) {
        error.observe(this, Observer {
            Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
        })
    }
}
