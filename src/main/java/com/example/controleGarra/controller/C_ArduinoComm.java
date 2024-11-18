package com.example.controleGarra.controller;
import com.fazecast.jSerialComm.*;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

@Controller
public class C_ArduinoComm {

    private static OutputStream output;

    private SerialPort serialPort;

    public C_ArduinoComm() {
        output = null;
    }

    public static void main(String[] args) {
        C_ArduinoComm arduino = new C_ArduinoComm();
        arduino.initialize();
    }

    public void initialize() {
        SerialPort[] ports = SerialPort.getCommPorts();

        if (ports.length == 0) {
            System.out.println("Nenhuma porta serial encontrada.");
            return;
        }

        serialPort = ports[0]; // Use a primeira porta serial encontrada, você pode ajustar isso conforme necessário

        if (!serialPort.openPort()) {
            System.out.println("Falha ao abrir a porta serial.");
            return;
        }

        serialPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);

        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 10000, 0);


        BufferedReader input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));

        output = connectOutput(output, serialPort);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static OutputStream getOutput() {
        return output;
    }

    public static void enviarComando(OutputStream output, int servo, int angulo){
        try {
            output.write(servo);
            output.flush();
            try{Thread.sleep(300);} catch (Exception e){}

            output.write(angulo);
            output.flush();
            try{Thread.sleep(300);} catch (Exception e){}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void resetarBraco(OutputStream output){
        try{
            output.write(11);
            output.flush();
            try{Thread.sleep(300);} catch (Exception e){}

            output.write(0);
            output.flush();
            try{Thread.sleep(300);} catch (Exception e){}

            output.write(12);
            output.flush();
            try{Thread.sleep(300);} catch (Exception e){}

            output.write(0);
            output.flush();
            try{Thread.sleep(300);} catch (Exception e){}

            for(int i = 3; i < 5; i++){
                output.write(i);
                output.flush();
                try{Thread.sleep(300);} catch (Exception e){}

                output.write(0);
                output.flush();
                try{Thread.sleep(300);} catch (Exception e){}
            }

            output.write(5);
            output.flush();
            try{Thread.sleep(300);} catch (Exception e){}

            output.write(45);
            output.flush();
            try{Thread.sleep(300);} catch (Exception e){}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static OutputStream connectOutput(OutputStream output, SerialPort serialPort){
        try {
            output = serialPort.getOutputStream();
            if (output == null) {
                System.out.println("Falha ao obter o OutputStream.");
            } else {
                System.out.println("OutputStream obtido com sucesso.");
                return output;
            }
        } catch (Exception e) {
            // Tratamento de erro, caso a obtenção do OutputStream falhe
            System.out.println("Erro ao obter OutputStream: " + e.getMessage());
        }
        return null;
    }
}