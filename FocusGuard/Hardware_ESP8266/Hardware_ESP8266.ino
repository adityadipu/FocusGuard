#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <ArduinoJson.h>
#include <math.h>

// --- Pin Definitions ---
#define OLED_SDA D2
#define OLED_SCL D1
#define START_BUTTON_PIN D5
#define INC_BUTTON_PIN   D6
#define DEC_BUTTON_PIN   D7
#define RED_PIN   D3
#define GREEN_PIN D4
#define BLUE_PIN  1
#define BUZZER_PIN D8

// --- Wi-Fi Details ---
const char* ssid = "DNS";
const char* password = "@LIONASHISH@";

// --- Web Server ---
ESP8266WebServer server(80);

// --- OLED Configuration ---
#define SCREEN_WIDTH 128
#define SCREEN_HEIGHT 64
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1);

// --- Musical Notes and Melodies ---
#define NOTE_C5  523
#define NOTE_E5  659
#define NOTE_G5  784
#define NOTE_C6  1047
int sessionEndMelody[] = { NOTE_C5, NOTE_E5, NOTE_G5, NOTE_C6 };
int sessionEndDurations[] = { 100, 100, 100, 200 };
int buttonPressMelody[] = { NOTE_C5, NOTE_G5 };
int buttonPressDurations[] = { 50, 75 };

// --- State Machine Variables ---
enum State { IDLE, FOCUS, BREAK, PAUSED };
State currentState = IDLE;
long timerSeconds = 25 * 60;
long remainingSeconds = 0;
long breakSeconds = 5 * 60;
unsigned long lastSecondMillis = 0;

// --- Menu System Variables ---
enum DisplayMode { TIMER_MODE, MENU_MODE, EDIT_MODE, SHOW_IP_MODE };
DisplayMode currentDisplayMode = TIMER_MODE;
const int MENU_ITEM_COUNT = 4;
int selectedMenuItem = 0;
int editingMenuItem = -1;
const char* menuItems[MENU_ITEM_COUNT] = {"Focus Time", "Break Time", "Show IP", "Exit Menu"};
unsigned long ipShowStartTime = 0;

// --- Button State & Timing Variables ---
unsigned long buttonDownTime = 0;
unsigned long lastButtonPressTime = 0;
bool longPressActive = false;

// --- Helper function to play a melody ---
void playMelody(int melody[], int durations[], int noteCount) {
  for (int thisNote = 0; thisNote < noteCount; thisNote++) {
    tone(BUZZER_PIN, melody[thisNote], durations[thisNote]);
    delay(durations[thisNote] + 50);
  }
}

// --- Custom function to draw the circular progress bar ---
void drawArc(int x, int y, int r, int start_angle, int end_angle) {
  for (int i = start_angle; i <= end_angle; i++) {
    float angle_rad = i * 0.0174533;
    int x_point = x + r * cos(angle_rad);
    int y_point = y + r * sin(angle_rad);
    display.drawPixel(x_point, y_point, WHITE);
  }
}

// --- SERVER HANDLER FUNCTIONS ---

// --- CORRECTED handleStatus() FUNCTION ---
void handleStatus() {
  StaticJsonDocument<200> doc;
  doc["state"] = currentState;
  doc["remaining_seconds"] = remainingSeconds;
  doc["focus_duration"] = timerSeconds;
  doc["break_duration"] = breakSeconds;
  
  // Use a character array instead of a String object for memory safety
  char output[200];
  serializeJson(doc, output);
  
  server.send(200, "application/json", output);
}
// -----------------------------------------

void handleSetTimes() {
  if (server.hasArg("focus") && server.hasArg("break")) {
    int focusMinutes = server.arg("focus").toInt();
    int breakMinutes = server.arg("break").toInt();
    if (focusMinutes > 0) timerSeconds = focusMinutes * 60;
    if (breakMinutes > 0) breakSeconds = breakMinutes * 60;
    server.send(200, "text/plain", "Times updated");
  } else {
    server.send(400, "text/plain", "Missing arguments");
  }
}

void handleStart() {
  if (currentState == IDLE) { currentState = FOCUS; remainingSeconds = timerSeconds; }
  server.send(200, "text/plain", "Timer started");
}

void handlePause() {
  if (currentState == FOCUS) currentState = PAUSED;
  else if (currentState == PAUSED) currentState = FOCUS;
  server.send(200, "text/plain", "Pause toggled");
}

void handleReset() {
  currentState = IDLE;
  remainingSeconds = 0;
  server.send(200, "text/plain", "Timer reset");
}


void setup() {
  Serial.begin(115200);
  pinMode(START_BUTTON_PIN, INPUT_PULLUP);
  pinMode(INC_BUTTON_PIN, INPUT_PULLUP);
  pinMode(DEC_BUTTON_PIN, INPUT_PULLUP);
  pinMode(RED_PIN, OUTPUT);
  pinMode(GREEN_PIN, OUTPUT);
  pinMode(BLUE_PIN, OUTPUT);
  pinMode(BUZZER_PIN, OUTPUT);

  display.begin(SSD1306_SWITCHCAPVCC, 0x3C);
  display.clearDisplay();
  display.setTextSize(1);
  display.setTextColor(WHITE);
  display.setCursor(0, 0);
  display.println("Focus Guard Server");
  display.display();

  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) { delay(500); }

  display.clearDisplay();
  display.setCursor(0, 0);
  display.println("Connected!");
  display.println("IP Address:");
  display.setTextSize(1);
  display.println(WiFi.localIP());
  display.display();
  delay(2000);

  server.on("/status", HTTP_GET, handleStatus);
  server.on("/set", HTTP_GET, handleSetTimes);
  server.on("/start", HTTP_GET, handleStart);
  server.on("/pause", HTTP_GET, handlePause);
  server.on("/reset", HTTP_GET, handleReset);
  server.begin();
}

void loop() {
  server.handleClient();
  handleButtons();
  updateTimer();
  updateDisplay();
  updateRGB();

  if (currentDisplayMode == SHOW_IP_MODE && millis() - ipShowStartTime > 3000) {
    currentDisplayMode = MENU_MODE;
  }
}

void handleButtons() {
    bool startPressed = (digitalRead(START_BUTTON_PIN) == LOW);
    bool incPressed = (digitalRead(INC_BUTTON_PIN) == LOW);
    bool decPressed = (digitalRead(DEC_BUTTON_PIN) == LOW);

    if (currentDisplayMode == SHOW_IP_MODE) return;

    if (startPressed) {
        if (buttonDownTime == 0) { buttonDownTime = millis(); }
        if (currentState == IDLE && currentDisplayMode == TIMER_MODE && !longPressActive && (millis() - buttonDownTime > 1000)) {
            longPressActive = true;
            playMelody(buttonPressMelody, buttonPressDurations, 2);
            currentDisplayMode = MENU_MODE;
            selectedMenuItem = 0;
        }
    } else {
        if (buttonDownTime > 0 && !longPressActive) {
            playMelody(buttonPressMelody, buttonPressDurations, 2);
            if (currentDisplayMode == TIMER_MODE) {
                if (currentState == IDLE) { currentState = FOCUS; remainingSeconds = timerSeconds; }
                else if (currentState == FOCUS) { currentState = PAUSED; }
                else if (currentState == PAUSED) { currentState = FOCUS; }
                else if (currentState == BREAK) { currentState = IDLE; }
            } else if (currentDisplayMode == MENU_MODE) {
                if (selectedMenuItem == 0 || selectedMenuItem == 1) { // Edit Time
                    currentDisplayMode = EDIT_MODE;
                    editingMenuItem = selectedMenuItem;
                } else if (selectedMenuItem == 2) { // Show IP
                    currentDisplayMode = SHOW_IP_MODE;
                    ipShowStartTime = millis();
                } else if (selectedMenuItem == 3) { // Exit Menu
                    currentDisplayMode = TIMER_MODE;
                }
            } else if (currentDisplayMode == EDIT_MODE) {
                currentDisplayMode = MENU_MODE;
                editingMenuItem = -1;
            }
        }
        buttonDownTime = 0;
        longPressActive = false;
    }

    if (millis() - lastButtonPressTime > 200) {
        if (incPressed || decPressed) {
            lastButtonPressTime = millis();
            if (currentDisplayMode == MENU_MODE) {
                if (incPressed) { selectedMenuItem = (selectedMenuItem - 1 + MENU_ITEM_COUNT) % MENU_ITEM_COUNT; }
                if (decPressed) { selectedMenuItem = (selectedMenuItem + 1) % MENU_ITEM_COUNT; }
            } else if (currentDisplayMode == EDIT_MODE) {
                playMelody(buttonPressMelody, buttonPressDurations, 2);
                if (editingMenuItem == 0) {
                    if (incPressed) timerSeconds += 60;
                    if (decPressed) timerSeconds -= 60;
                    if (timerSeconds < 60) timerSeconds = 60;
                } else if (editingMenuItem == 1) {
                    if (incPressed) breakSeconds += 60;
                    if (decPressed) breakSeconds -= 60;
                    if (breakSeconds < 60) breakSeconds = 60;
                }
            } else if (currentState == IDLE) {
                playMelody(buttonPressMelody, buttonPressDurations, 2);
                if (incPressed) { timerSeconds += 60; }
                if (decPressed) { timerSeconds -= 60; if (timerSeconds < 60) timerSeconds = 60; }
            }
        }
    }
}

void updateTimer() {
  if ((currentState == FOCUS || currentState == BREAK) && (millis() - lastSecondMillis >= 1000)) {
    remainingSeconds--;
    lastSecondMillis = millis();
    if (remainingSeconds < 0) {
      if (currentState == FOCUS) {
        currentState = BREAK;
        remainingSeconds = breakSeconds;
        playMelody(sessionEndMelody, sessionEndDurations, 4);
      } else if (currentState == BREAK) {
        currentState = IDLE;
        playMelody(sessionEndMelody, sessionEndDurations, 4);
      }
    }
  }
}

void updateDisplay() {
    display.clearDisplay();
    display.setTextColor(WHITE);

    if (currentDisplayMode == TIMER_MODE) {
        int centerX = 64, centerY = 32, radius = 30;
        long totalDuration = 0, elapsedSeconds = 0;

        if (currentState == FOCUS) { totalDuration = timerSeconds; elapsedSeconds = totalDuration - remainingSeconds; }
        else if (currentState == BREAK) { totalDuration = breakSeconds; elapsedSeconds = totalDuration - remainingSeconds; }

        if (totalDuration > 0) {
            for (int i = 0; i < 360; i += 15) {
                float angle_rad = i * 0.0174533;
                display.drawPixel(centerX + radius * cos(angle_rad), centerY + radius * sin(angle_rad), WHITE);
            }
            int progress_angle = map(elapsedSeconds, 0, totalDuration, -90, 270);
            drawArc(centerX, centerY, radius, -90, progress_angle);
        } else {
            display.drawCircle(centerX, centerY, radius, WHITE);
        }

        long displayTime = (currentState == IDLE) ? timerSeconds : remainingSeconds;
        int minutes = displayTime / 60;
        int seconds = displayTime % 60;
        char timeStr[6];
        sprintf(timeStr, "%02d:%02d", minutes, seconds);

        display.setTextSize(1);
        switch (currentState) {
            case IDLE:   display.setCursor(36, 25); display.print("SET TIME"); break;
            case FOCUS:  display.setCursor(40, 22); display.print("Focusing"); break;
            case BREAK:  display.setCursor(42, 22); display.print("On Break"); break;
            case PAUSED: display.setCursor(45, 22); display.print("Paused"); break;
        }

        display.setTextSize(2);
        display.setCursor(38, 35);
        if (currentState != IDLE) display.print(timeStr);

    } else if (currentDisplayMode == SHOW_IP_MODE) {
        display.setTextSize(1);
        display.setCursor(0, 10);
        display.println("Device IP Address:");
        display.setTextSize(2);
        display.setCursor(0, 30);
        display.println(WiFi.localIP().toString());
    } else { // Handles MENU_MODE and EDIT_MODE
        display.setTextSize(1);
        display.setCursor(0, 0);
        display.println("--- SETTINGS ---");
        for (int i = 0; i < MENU_ITEM_COUNT; i++) {
            display.setCursor(0, 10 + (i * 12));
            if (i == selectedMenuItem) display.print("> ");
            else display.print("  ");
            display.print(menuItems[i]);

            if (i == 0) { display.print(" "); display.print(timerSeconds / 60); display.print("m"); }
            else if (i == 1) { display.print(" "); display.print(breakSeconds / 60); display.print("m"); }
            if (i == editingMenuItem) display.print(" <--");
        }
    }
    display.display();
}

void updateRGB() {
  switch (currentState) {
    case IDLE:   setRGBColor(0, 0, 255); break;
    case FOCUS:  setRGBColor(255, 0, 0); break;
    case BREAK:  setRGBColor(0, 255, 0); break;
    case PAUSED: setRGBColor(255, 165, 0); break;
  }
}

void setRGBColor(int r, int g, int b) {
  analogWrite(RED_PIN, r);
  analogWrite(GREEN_PIN, g);
  analogWrite(BLUE_PIN, b);
}