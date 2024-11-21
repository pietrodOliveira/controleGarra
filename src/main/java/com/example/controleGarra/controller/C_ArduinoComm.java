package com.example.controleGarra.controller;

import com.fazecast.jSerialComm.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

@Controller
public class C_ArduinoComm {

    private static OutputStream output;

    private static SerialPort serialPort;
    private static BufferedReader input;
    private static int delay = 450;

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

        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("Porta serial fechada.");
        }

        serialPort = ports[0]; // Use a primeira porta serial encontrada, você pode ajustar isso conforme necessário

        if (!serialPort.openPort()) {
            System.out.println("Falha ao abrir a porta serial.");
            return;
        }

        serialPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);

        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 10000, 0);

        input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));

        try {
            output = serialPort.getOutputStream();
            if (output == null) {
                System.out.println("Falha ao obter o OutputStream.");
            } else {
                System.out.println("OutputStream obtido com sucesso. Porta aberta: " + serialPort.getDescriptivePortName());
            }
        } catch (Exception e) {
            // Tratamento de erro, caso a obtenção do OutputStream falhe
            System.out.println("Erro ao obter OutputStream: " + e.getMessage());
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static OutputStream getOutput() {
        return output;
    }

    public static BufferedReader getInput() {
        return input;
    }

    public static void enviarComando(OutputStream output, int servo, int angulo) {
        try {
            output.write(servo);
            output.flush();

            Thread.sleep(delay);

            output.write(angulo);
            output.flush();

            Thread.sleep(delay + 150);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void resetarBraco(OutputStream output) {
        try {
            output.write(11);
            output.flush();
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
            }

            output.write(0);
            output.flush();
            try {
                Thread.sleep(delay + 150);
            } catch (Exception e) {
            }

            output.write(12);
            output.flush();
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
            }

            output.write(0);
            output.flush();
            try {
                Thread.sleep(delay + 150);
            } catch (Exception e) {
            }

            for (int i = 3; i < 5; i++) {
                output.write(i);
                output.flush();
                try {
                    Thread.sleep(delay);
                } catch (Exception e) {
                }

                output.write(0);
                output.flush();
                try {
                    Thread.sleep(delay +150);
                } catch (Exception e) {
                }
            }

            output.write(5);
            output.flush();
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
            }

            output.write(45);
            output.flush();
            try {
                Thread.sleep(delay + 150);
            } catch (Exception e) {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/reconnect")
    @ResponseBody
    private String reconnect() {
        int attempts = 0;
        int maxAttempts = 3;
        int waitTimeMs = 50;
        while (attempts < maxAttempts) {
            System.out.println("Tentando reconectar à porta serial... (tentativa " + (attempts + 1) + ")");
            initialize();

            if (serialPort != null && serialPort.isOpen()) {
                System.out.println("Reconexão bem-sucedida.");
                return "top";
            }

            attempts++;
            try {
                Thread.sleep(waitTimeMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Não foi possível reconectar após " + maxAttempts + " tentativas.");
        return "paia";
    }
}