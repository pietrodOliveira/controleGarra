package com.example.controleGarra.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class C_Comandos {
    @PostMapping("/garra")
    @ResponseBody
    private ResponseEntity<String> postGarra(@RequestBody List<Map<String, Object>> motorAngulos){
       return RodarComandos(motorAngulos);
    }

    @PostMapping("/getPv")
    private String postPv(){
        return "pv/top";
    }

    public static ResponseEntity<String> RodarComandos(List<Map<String, Object>> motorAngulos){
        try{
            C_ArduinoComm.initialize();
            for(Map<String, Object> comando : motorAngulos){
                if(!comando.get("motor").toString().isEmpty() && !comando.get("angulo").toString().isEmpty()){
                    int servo = Integer.parseInt(comando.get("motor").toString());
                    int angulo = Integer.parseInt(comando.get("angulo").toString());

                    System.out.println("Comando: "+servo+";"+angulo);

                    C_ArduinoComm.enviarComando(servo, angulo);

                    Thread.sleep(1000);
                }
            }
            C_ArduinoComm.DesconectarPortaSerial();
            return ResponseEntity.ok("Comando executado com sucesso");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao enviar comandos");
        }
    }
}
