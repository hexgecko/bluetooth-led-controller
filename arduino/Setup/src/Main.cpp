#include <Arduino.h>
#include <SoftwareSerial.h>

static const byte ledPin = 4;

SoftwareSerial bluetooth(2, 3); // RX, TX

void sendCommand(const char* command) {
	bluetooth.println(command);
	Serial.println(command);

	digitalWrite(ledPin, HIGH);
	delay(500);
	while(bluetooth.available()) {
		Serial.write(bluetooth.read());
	}
	digitalWrite(ledPin, LOW);
	delay(100);
}

void setup(){

	pinMode(ledPin, OUTPUT);

	Serial.begin(9600);
	bluetooth.begin(38400);

	Serial.println("Serial and bluetooth initalized...");
	
	sendCommand("AT");
	sendCommand("AT+VERSION");
	sendCommand("AT+ROLE");
	sendCommand("AT+PSWD=3188");
	sendCommand("AT+NAME=LED CONTROLLER");
}

void loop() {
}
