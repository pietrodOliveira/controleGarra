package com.example.controleGarra.controller;


import com.example.controleGarra.service.S_Comandos;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class C_Comandos {
    @PostMapping("/garra")
    private String postGarra(@RequestParam("servo") int servo,
                             @RequestParam("anguloServo") int angulo){
        C_ArduinoComm.enviarComando(C_ArduinoComm.getOutput() ,servo, angulo);
        return "index";
    }

    @PostMapping("/getPv")
    private String postPv(){
        return "pv/top";
    }
}
