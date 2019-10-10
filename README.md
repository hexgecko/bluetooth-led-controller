# LED Controller

Both the Arduino project use [Plaformia](https://platformio.org/) for programming the Arduino.

## Arduino - Setup HC-05 Bluetooth Module

This firmware setup the HC-05 Bluetooth module with a new name and password.

## Arduino - Firmware

This firmware read a command from the client and send information back or updated the LED.

"S" -> "LOW" or HIGH
"T" -> "OK" and toggle the LED

## Android - MVVM App

The application is using the View - ViewModel - Model architecture for controlling the led.

It simple a button that send the Toggle command to arduino and toogle the led.
It also read the status when connected and after the button have been pressed.
