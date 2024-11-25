#include <Servo.h>
#include <EEPROM.h>

Servo servo1, servo2, servo3, servo4, servo5;

int portaServo = 0;
int anguloServo = 0;
int espera = 450;

int posServo1 = 0;  // Valor inicial (exemplo)
int posServo2 = 0;
int posServo3 = 0;
int posServo4 = 0;
int posServo5 = 45;

void setup() {
  Serial.begin(9600);

  posServo1 = EEPROM.read(0);
  posServo2 = EEPROM.read(1);
  posServo3 = EEPROM.read(2);
  posServo4 = EEPROM.read(3);
  posServo5 = EEPROM.read(4);

  servo1.attach(11);
  servo1.write(posServo1);

  servo2.attach(12);
  servo2.write(posServo2);

  servo3.attach(3);
  servo3.write(posServo3);

  servo4.attach(4);
  servo4.write(posServo4);

  servo5.attach(5);
  servo5.write(posServo5);
}

void loop() {
  if(Serial.available() > 0){
    String comandos = Serial.readString();
    comandos.trim();
    int pos = comandos.indexOf(';');

    if(pos != -1){
      String strServo = comandos.substring(0, pos);
      String strAngulo = comandos.substring(pos + 1);

      portaServo = strServo.toInt();
      anguloServo = strAngulo.toInt();
    }
    switch(portaServo){
      case 11:
        servo1.write(anguloServo);
        EEPROM.write(0, anguloServo);
        delay(espera);
        break;
      case 12:
        servo2.write(anguloServo);
        EEPROM.write(1, anguloServo);
        delay(espera);
        break;
      case 3:
        servo3.write(anguloServo);
        EEPROM.write(2, anguloServo);
        delay(espera);
        break;
      case 4:
        servo4.write(anguloServo);
        EEPROM.write(3, anguloServo);
        delay(espera);
        break;
      case 5:
        servo5.write(anguloServo);
        EEPROM.write(4, anguloServo);
        delay(espera);
        break;
    }
  }
  delay(150);
}
