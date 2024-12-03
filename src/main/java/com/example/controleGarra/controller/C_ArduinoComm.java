package com.example.controleGarra.controller;

import com.fazecast.jSerialComm.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class C_ArduinoComm {

    private static OutputStream output;

    private static SerialPort serialPort;
    private static BufferedReader input;
    private static int delay = 600;

    public C_ArduinoComm() {
        output = null;
    }

    public static void initialize() {
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
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void DesconectarPortaSerial() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("Porta serial fechada.");
        }
    }

    public static void enviarComando(int servo, int angulo) {
        try {
            output.write((servo + ";" + angulo).getBytes());
            output.flush();
            Thread.sleep(delay);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/reset")
    private String postReset() throws InterruptedException {
        resetarBraco();

        return "index";
    }

    public static void resetarBraco() {
        List<Map<String, Object>> comandos = new ArrayList<>();

        Map<String, Object> comando1 = new HashMap<>();
        comando1.put("motor", 11);
        comando1.put("angulo", 0);
        comandos.add(comando1);

        Map<String, Object> comando2 = new HashMap<>();
        comando2.put("motor", 12);
        comando2.put("angulo", 0);
        comandos.add(comando2);

        Map<String, Object> comando3 = new HashMap<>();
        comando3.put("motor", 3);
        comando3.put("angulo", 0);
        comandos.add(comando3);

        Map<String, Object> comando4 = new HashMap<>();
        comando4.put("motor", 4);
        comando4.put("angulo", 0);
        comandos.add(comando4);

        Map<String, Object> comando5 = new HashMap<>();
        comando5.put("motor", 5);
        comando5.put("angulo", 45);
        comandos.add(comando5);

        C_Comandos.RodarComandos(comandos);
    }

    @PostMapping("/reconnect")
    @ResponseBody
    private int reconnect() {
        int attempts = 0;
        int maxAttempts = 3;
        int waitTimeMs = 50;
        while (attempts < maxAttempts) {
            System.out.println("Tentando reconectar à porta serial... (tentativa " + (attempts + 1) + ")");
            initialize();

            if (serialPort != null && serialPort.isOpen()) {
                System.out.println("Reconexão bem-sucedida.");
                return 1;
            }

            attempts++;
            try {
                Thread.sleep(waitTimeMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Não foi possível reconectar após " + maxAttempts + " tentativas.");
        return 0;
    }
}