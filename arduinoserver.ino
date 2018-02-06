#include <Ethernet.h>
#include <SPI.h>

IPAddress ip(192, 168, 115, 1);
byte mac[] = {0xAA, 0xBB, 0xCC, 0xDD, 0xEE, 0xFF};

EthernetServer server = EthernetServer(1234);

void setup() {

  Serial.begin(9600);

  Ethernet.begin(mac, ip);

  delay(1000);

  server.begin();

  Serial.println("Waiting for client...");

}

void loop() {

  EthernetClient client = server.available();

  if(client == true){

    Serial.println("Connected");

  }

}
