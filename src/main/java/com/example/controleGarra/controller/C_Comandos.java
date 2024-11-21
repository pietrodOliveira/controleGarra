package com.example.controleGarra.controller;


import com.example.controleGarra.service.S_Comandos;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class C_Comandos {
    @PostMapping("/garra")
    @ResponseBody
    private ResponseEntity<String> postGarra(@RequestBody List<Map<String, Object>> motorAngulos){
        try{
            for(Map<String, Object> comando : motorAngulos){
                if(!comando.get("motor").toString().isEmpty() && !comando.get("angulo").toString().isEmpty()){
                    int servo = Integer.parseInt(comando.get("motor").toString());
                    int angulo = Integer.parseInt(comando.get("angulo").toString());

                    System.out.println("servo: "+servo);
                    System.out.println("angulo: "+angulo);

                    C_ArduinoComm.enviarComando(C_ArduinoComm.getOutput() ,servo, angulo);

                    Thread.sleep(1000);
                }
            }
            return ResponseEntity.ok("Comando executado com sucesso");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao enviar comandos");
        }
    }

    @PostMapping("/getPv")
    private String postPv(){
        return "pv/top";
    }

    @PostMapping("/reset")
    private String postReset(){
        C_ArduinoComm.resetarBraco(C_ArduinoComm.getOutput());

        return "index";
    }
}
