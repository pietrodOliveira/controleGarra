#include <VarSpeedServo.h>

VarSpeedServo meuServo;
int portaServo = 0;
int anguloServo = 0;
void setup() {
  Serial.begin(9600);
  for(int i = 2; i < 7; i++){
    meuServo.attach(i);
    meuServo.write(0);
    delay(200);
  }
}

void loop() {
  if(Serial.available() > 0){
    portaServo = Serial.read();
    meuServo.attach(portaServo);
    delay(1000);
    if(Serial.available() > 0){
      anguloServo = Serial.read();
      meuServo.write(anguloServo);
      delay(1000);
    }
  }
  delay(1000);
}
