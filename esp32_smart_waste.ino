#include <ESP32Servo.h>
#include <HTTPClient.h>
#include <WiFi.h>

// Set these before uploading.
const char* WIFI_SSID = "YOUR_WIFI_NAME";
const char* WIFI_PASSWORD = "YOUR_WIFI_PASSWORD";

// Replace this with the computer's Wi-Fi/LAN IPv4 address, not localhost.
const char* API_BASE_URL = "http://172.16.50.153:8080";
const long BIN_ID = 3; // BIN-LSK-001; change this to the target bin's database ID.

const int redLED = 27;
const int greenLED = 12;

const int lidTrigPin = 26;
const int lidEchoPin = 25;
const int fillTrigPin = 18;
const int fillEchoPin = 17;

const int servoPin = 16;
Servo myServo;

const int detectionDistance = 20;
const float emptyThreshold = 18.0;
const float fullThreshold = 5.0;
const unsigned long readingIntervalMs = 30000;
const unsigned long lidCommandPollIntervalMs = 1000;
const unsigned long lidStateRetryIntervalMs = 2000;

unsigned long lastReadingSentAt = 0;
unsigned long lastLidCommandPollAt = 0;
unsigned long lastLidStateAttemptAt = 0;
bool wifiConnectedMessagePrinted = false;
bool firstReadingSent = false;
bool lidIsOpen = false;
bool lidStateKnown = false;
bool lidStateReportPending = true;
String lidCommand = "AUTO";

float measureDistance(int trigPin, int echoPin) {
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  const unsigned long duration = pulseIn(echoPin, HIGH, 30000);
  if (duration == 0) {
    return -1.0;
  }

  return duration * 0.0343 / 2.0;
}

float calculateFillPercentage(float distanceCm) {
  const float percentage =
    (emptyThreshold - distanceCm) * 100.0 / (emptyThreshold - fullThreshold);
  return constrain(percentage, 0.0, 100.0);
}

void sendFillReading(float distanceCm) {
  if (WiFi.status() != WL_CONNECTED) {
    return;
  }

  HTTPClient http;
  const String url = String(API_BASE_URL) + "/api/bins/" + String(BIN_ID) + "/readings";
  if (!http.begin(url)) {
    return;
  }

  http.addHeader("Content-Type", "application/json");

  const float fillPercentage = calculateFillPercentage(distanceCm);
  const String payload = "{\"fillPercentage\":" + String(fillPercentage, 2)
    + ",\"distanceCm\":" + String(distanceCm, 2) + "}";

  const int statusCode = http.POST(payload);
  if (statusCode >= 200 && statusCode < 300) {
    Serial.println("Reading sent to backend.");
  }

  http.end();
}

void pollLidCommand() {
  if (WiFi.status() != WL_CONNECTED) {
    return;
  }

  HTTPClient http;
  const String url = String(API_BASE_URL) + "/api/bins/" + String(BIN_ID) + "/lid";
  if (!http.begin(url)) {
    return;
  }

  const int statusCode = http.GET();
  if (statusCode == 200) {
    const String response = http.getString();
    const int keyPosition = response.indexOf("\"command\"");
    const int colonPosition = keyPosition < 0 ? -1 : response.indexOf(':', keyPosition);
    const int valueStart = colonPosition < 0 ? -1 : response.indexOf('"', colonPosition + 1);
    const int valueEnd = valueStart < 0 ? -1 : response.indexOf('"', valueStart + 1);

    if (valueStart >= 0 && valueEnd > valueStart) {
      const String receivedCommand = response.substring(valueStart + 1, valueEnd);
      if (receivedCommand == "AUTO" || receivedCommand == "OPEN"
          || receivedCommand == "CLOSED") {
        lidCommand = receivedCommand;
      }
    }
  }

  http.end();
}

void reportLidState() {
  if (WiFi.status() != WL_CONNECTED || !lidStateReportPending) {
    return;
  }

  HTTPClient http;
  const String url = String(API_BASE_URL) + "/api/bins/" + String(BIN_ID) + "/lid/state";
  if (!http.begin(url)) {
    return;
  }

  http.addHeader("Content-Type", "application/json");
  const String state = lidIsOpen ? "OPEN" : "CLOSED";
  const String payload = "{\"state\":\"" + state + "\"}";
  const int statusCode = http.PUT(payload);
  if (statusCode >= 200 && statusCode < 300) {
    lidStateReportPending = false;
  }

  http.end();
}

void applyLidState(bool open) {
  if (lidStateKnown && lidIsOpen == open) {
    return;
  }

  lidIsOpen = open;
  lidStateKnown = true;
  lidStateReportPending = true;
  myServo.write(open ? 0 : 90);
  digitalWrite(greenLED, open ? HIGH : LOW);
  digitalWrite(redLED, open ? LOW : HIGH);
}
void setup() {
  Serial.begin(115200);

  pinMode(redLED, OUTPUT);
  pinMode(greenLED, OUTPUT);
  pinMode(lidTrigPin, OUTPUT);
  pinMode(lidEchoPin, INPUT);
  pinMode(fillTrigPin, OUTPUT);
  pinMode(fillEchoPin, INPUT);

  myServo.setPeriodHertz(50);
  myServo.attach(servoPin, 500, 2400);
  myServo.write(90);
  digitalWrite(redLED, HIGH);
  digitalWrite(greenLED, LOW);

  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
}

void loop() {
  if (WiFi.status() == WL_CONNECTED && !wifiConnectedMessagePrinted) {
    Serial.println("WiFi connected. Readings are being sent to the backend.");
    wifiConnectedMessagePrinted = true;
  }

  if (WiFi.status() == WL_CONNECTED
      && millis() - lastLidCommandPollAt >= lidCommandPollIntervalMs) {
    pollLidCommand();
    lastLidCommandPollAt = millis();
  }

  const float lidDistance = measureDistance(lidTrigPin, lidEchoPin);
  if (lidCommand == "OPEN") {
    applyLidState(true);
  } else if (lidCommand == "CLOSED") {
    applyLidState(false);
  } else {
    applyLidState(lidDistance > 0 && lidDistance <= detectionDistance);
  }

  if (lidStateReportPending && WiFi.status() == WL_CONNECTED
      && (lastLidStateAttemptAt == 0
          || millis() - lastLidStateAttemptAt >= lidStateRetryIntervalMs)) {
    reportLidState();
    lastLidStateAttemptAt = millis();
  }

  delay(100);
  const float fillDistance = measureDistance(fillTrigPin, fillEchoPin);

  if (fillDistance >= 0 && WiFi.status() == WL_CONNECTED
      && (!firstReadingSent || millis() - lastReadingSentAt >= readingIntervalMs)) {
    sendFillReading(fillDistance);
    lastReadingSentAt = millis();
    firstReadingSent = true;
  }

  delay(500);
}