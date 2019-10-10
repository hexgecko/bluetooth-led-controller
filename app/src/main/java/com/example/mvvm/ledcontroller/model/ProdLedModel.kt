package com.example.mvvm.ledcontroller.model

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.io.*
import java.util.*

class ProdLedModel: LedModel {

    companion object {
        const val DEVICE_NAME = "LED CONTROLLER"
        private val SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    private val adapter = BluetoothAdapter.getDefaultAdapter()
    private var socket: BluetoothSocket? = null
    private var toggleThread: ToggleThread? = null

    override val ledStatus = MutableLiveData<Boolean>().apply { value = null }

    override val busy = MutableLiveData<Boolean>().apply { value = true }

    override val error = MutableLiveData<String>()

    override fun onToggle() {
        // Wait for the old thread to finnish
        toggleThread?.join()

        // Start a new toggle thread
        toggleThread = ToggleThread()
        toggleThread?.start()
    }

    init {
        if(adapter == null) {
            error.value = "Bluetooth adapter is not available!"
        } else if(!adapter.isEnabled) {
            error.value = "Bluetooth is disabled, check setting!"
        } else {
            // loop through the connected devices to find our controller.
            val device = adapter.bondedDevices.find { it.name == DEVICE_NAME }
            adapter.cancelDiscovery()

            if(device == null) {
                error.value = "Bluetooth target not found!"
            } else {
                // Try to create a new socket
                try {
                    socket = device.createRfcommSocketToServiceRecord(SPP_UUID)
                } catch(e: IOException) {
                    error.value = e.message
                }

                ConnectThread().start()
            }
        }
    }

    inner class ConnectThread: Thread() {
        override fun run() {
            // Try to connect to the socket
            if(socket != null) {
                var i=0
                while(true) {
                    try {
                        socket?.connect()
                        val writer = BufferedWriter(OutputStreamWriter(socket!!.outputStream, "ASCII"))
                        writer.write("S")
                        writer.flush()

                        val reader =
                            BufferedReader(InputStreamReader(socket!!.inputStream, "ASCII"))
                        when (reader.readLine()) {
                            "LOW" -> ledStatus.postValue(false)
                            "HIGH" -> ledStatus.postValue(true)
                        }
                    } catch (e: IOException) {
                        if(i++ < 3) {
                            sleep(10)
                            continue
                        }
                        error.postValue(e.toString())
                    }
                    busy.postValue(false)
                    break
                }
            } else {
                error.postValue("Bluetooth device is not connected!")
            }
        }
    }

    inner class ToggleThread: Thread() {
        override fun run() {
            if(socket != null) {
                var i=0
                while(true) {
                    try {
                        val writer = BufferedWriter(OutputStreamWriter(socket!!.outputStream, "ASCII"))
                        writer.write("T")
                        writer.flush()
                        sleep(50)

                        val reader = BufferedReader(InputStreamReader(socket!!.inputStream, "ASCII"))
                        when(reader.readLine()) {
                            "OK" -> {
                                writer.write("S")
                                writer.flush()
                                sleep(50)

                                when (reader.readLine()) {
                                    "LOW" -> ledStatus.postValue(false)
                                    "HIGH" -> ledStatus.postValue(true)
                                }
                            }
                            else -> throw IOException("Bluetooth failed to toggle led!")
                        }
                    } catch (e: IOException) {
                        Log.d("ToggleThread", e.toString())
                        if(i++ < 3) {
                            sleep(100)
                            continue
                        }
                        error.postValue(e.toString())
                    }

                    break
                }

            } else {
                error.postValue("Bluetooth device is not connected!")
            }
        }
    }
}
