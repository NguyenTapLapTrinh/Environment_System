#include <WiFi.h>
#include <MQ135.h>
#include <Keypad.h>
#include <EEPROM.h>
#include "WiFiManager.h"
#include <LiquidCrystal.h>
#include <analogWrite.h>
#include <FirebaseESP32.h>
#include "DHT.h"
#define PIN_MQ135 39
#define DHTPIN 21
#define DHTTYPE DHT11
#define FIREBASE_HOST "main-7d188-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "22xMLKW14pBpzKmCCBt8N73rZJoEJgLq5lIYZ1kC"

int ct = 0;
bool wifi = false;
const int rs = 13 , en = 12, d4 = 27, d5 = 26, d6 = 25, d7 = 33, led = 19, btn = 32,
          LCD_0 = 22, LCD_1 = 23, LCD_2 = 4, LCD_3 = 16, LCD_4 = 17, LCD_5 = 5, LCD_6 = 18;
int len = 0;
int raw_data;
int wait = 0;
bool bootln = 0;
int count = 0;
String sav_key = "";
String ID = "";
String WIFI_SSID;
String WIFI_PASSWORD;
bool lock = true;
const byte ROWS = 4; //four rows
const byte COLS = 3; //three columns
char keys[ROWS][COLS] = {
  {'1', '2', '3'},
  {'4', '5', '6'},
  {'7', '8', '9'},
  {'*', '0', '#'}
};
byte rowPins[ROWS] = {LCD_3, LCD_4, LCD_5, LCD_6};
byte colPins[COLS] = {LCD_0, LCD_1, LCD_2};

DHT dht(DHTPIN, DHTTYPE);
MQ135 mq135_sensor(PIN_MQ135);
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);
Keypad keypad = Keypad( makeKeymap(keys), rowPins, colPins, ROWS, COLS );
FirebaseData fbdb;
WiFiManager vs;

void writeStringToEEPROM(int addrOffset, const String &strToWrite)
{
  byte len = strToWrite.length();
  EEPROM.write(addrOffset, len);
  for (int i = 0; i < len; i++)
  {
    EEPROM.write(addrOffset + 1 + i, strToWrite[i]);
  }
}

String readStringFromEEPROM(int addrOffset)
{
  int newStrLen = EEPROM.read(addrOffset);
  char data[newStrLen + 1];
  for (int i = 0; i < newStrLen; i++)
  {
    data[i] = EEPROM.read(addrOffset + 1 + i);
  }
  data[newStrLen] = '\0';
  return String(data);
}

void displayMenu1() {
  lcd.setCursor(0, 0);
  lcd.print("Connected       ");
  lcd.setCursor(0, 1);
  lcd.print("ID Device: " + ID);
}

void displayMenu2() {
  lcd.setCursor(0, 0);
  lcd.print("Enter ID: ");
  lcd.setCursor(0, 1);
  lcd.print("#=OK    *=Clear");
}

void connectWiFi(String text) {
  while (WiFi.status() != WL_CONNECTED)
  {
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print(text);
    lcd.setCursor(0, 1);
    lcd.print(WIFI_SSID);
    lcd.setCursor(14, 1);
    lcd.print(ct);
    delay(1000);
    ct += 1;
    if (ct == 30) {
      wifi = true;
      ct = 0;
      break;
    }
  }
  ct = 0;
}

void configWiFi(String text) {
  if (wifi) {
    WiFiManager wm;
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print(text);
    lcd.setCursor(0, 1);
    lcd.print("Config Wifi");
    WiFi.mode(WIFI_STA); // explicitly set mode, esp defaults to STA+AP
    wm.resetSettings();
    bool res;
    res = wm.autoConnect("Enviroment", "123456789");
    if (!res) {
      ESP.restart();
    }
    else {
      //if you get here you have connected to the WiFi
      EEPROM.writeString(100, vs.getWiFiSSID());
      EEPROM.writeString(200, vs.getWiFiPass());
      EEPROM.commit();
      wifi = false;
    }
  }
}

void setup()
{
  EEPROM.begin(300);
  Serial.begin(115200);
  dht.begin();
  lcd.begin(16, 2);
  analogWrite(14, 60);
  WIFI_SSID = EEPROM.readString(100);
  WIFI_PASSWORD = EEPROM.readString(200);
  WiFi.begin(WIFI_SSID.c_str(), WIFI_PASSWORD.c_str());
  connectWiFi("Connecting to");
  configWiFi("Fail to connect");
  lcd.clear();
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
  Firebase.setReadTimeout(fbdb, 1000 * 60);
  Firebase.setwriteSizeLimit(fbdb, "tiny");
  ID = EEPROM.readString(0);
  if (ID < "00000" || ID > "99999") ID = "00000";
  pinMode(btn, INPUT);
  pinMode(led, OUTPUT);
  digitalWrite(led, HIGH);

}

void loop() {
  connectWiFi("Reconect to");
  configWiFi("Lost connection");
  if (!bootln) {
    displayMenu1();
    int h = dht.readHumidity();
    // Read temperature as Celsius (the default)
    int t = dht.readTemperature();
    int hic = dht.computeHeatIndex(t, h, false);
    float rzero = mq135_sensor.getRZero();
    int correctedPPM = mq135_sensor.getCorrectedPPM(t, h) + 400;
    if (count == 50) {
      if (ID > "00000" && ID < "99999") {
        Firebase.setInt(fbdb, "/ThongSo/" + ID + "/Air_Quality", correctedPPM);
        Firebase.setInt(fbdb, "/ThongSo/" + ID + "/Temp", t);
        Firebase.setInt(fbdb, "/ThongSo/" + ID + "/Humidity", h);
        Firebase.setInt(fbdb, "/ThongSo/" + ID + "/Heat_index", hic);
      }
      count = 0;
    }
    count += 1;
    delay(100);
    if (!digitalRead(btn)) {
      wait += 1;
      digitalWrite(led, LOW);
    }
    else {
      wait = 0;
      digitalWrite(led, HIGH);
    }
    if (wait == 30) {
      wait = 0;
      digitalWrite(led, HIGH);
      lcd.clear();
      bootln = true;
    }
  }
  else {
    displayMenu2();
    char cur_key = keypad.getKey();
    if (cur_key == '#') {
      if (sav_key != "") {
        lcd.clear();
        bootln = false;
        ID = sav_key;
        EEPROM.writeString(0, ID);
        EEPROM.commit();
        sav_key = "";
      }
    }
    else if (cur_key == '*') {
      if (len > 0) {
        sav_key.remove(len - 1, 1);
        len -= 1;
        lcd.clear();
        displayMenu2();
        lcd.setCursor(10, 0);
        lcd.print(sav_key);
      }
    }
    else if (!(cur_key == '\0')) {
      len += 1;
      sav_key += String(cur_key);
      lcd.setCursor(10, 0);
      lcd.print(sav_key);
    }
  }
}
