#include <Arduino.h>
#include <SoftwareSerial.h>

static const byte ledPin = 4;
static byte ledStatus = LOW;

SoftwareSerial bluetooth(2, 3); // RX, TX

void setup() {
	Serial.begin(9600);
	bluetooth.begin(9600);
	
	pinMode(ledPin, OUTPUT);
	digitalWrite(ledPin, ledStatus);
}

void loop() {
	if(bluetooth.available()) {
		char c = bluetooth.read();
		switch (c) {
		case 's':
		case 'S':
			bluetooth.println(ledStatus ? "HIGH" : "LOW");
			break;
		
		case 't':
		case 'T':
			ledStatus = ledStatus == HIGH ? LOW : HIGH;
			digitalWrite(ledPin, ledStatus);
			bluetooth.println("OK");

		default:
			break;
		}
	}
	
}