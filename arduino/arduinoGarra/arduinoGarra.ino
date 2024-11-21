#include <Servo.h>

Servo servo1;
Servo servo2;
Servo servo3;
Servo servo4;
Servo servo5;

int portaServo = 0;
int anguloServo = 0;
int espera = 450;

void setup() {
  Serial.begin(9600);

  servo1.attach(11);
  servo1.write(0);

  servo2.attach(12);
  servo2.write(0);

  servo3.attach(3);
  servo3.write(0);

  servo4.attach(4);
  servo4.write(0);

  servo5.attach(5);
  servo5.write(45);

  /*meuServo.attach(11);
  meuServo.write(0);
  delay(100);

  meuServo.attach(12);
  meuServo.write(0);
  delay(100);

  for(int i = 3; i < 6; i++){
    meuServo.attach(i);
    meuServo.write(0);
    delay(100);
  }*/
}

void loop() {
  Serial.begin(9600);
  if(Serial.available() > 0){
    portaServo = Serial.read();
    switch(portaServo){
      case 11:
        delay(espera);
        if(Serial.available() > 0){
          anguloServo = Serial.read();
          servo1.write(anguloServo);
          delay(espera);
        }
        break;
      case 12:
        delay(espera);
        if(Serial.available() > 0){
          anguloServo = Serial.read();
          servo2.write(anguloServo);
          delay(espera);
        }
        break;
      case 3:
        delay(espera);
        if(Serial.available() > 0){
          anguloServo = Serial.read();
          servo3.write(anguloServo);
          delay(espera);
        }
        break;
      case 4:
        delay(espera);
        if(Serial.available() > 0){
          anguloServo = Serial.read();
          servo4.write(anguloServo);
          delay(espera);
        }
        break;
      case 5:
        delay(espera);
        if(Serial.available() > 0){
          anguloServo = Serial.read();
          servo5.write(anguloServo);
          delay(espera);
        }
        break;
    }

    portaServo = 0;
    anguloServo = 0;
    Serial.end();
  }
  delay(150);
}
