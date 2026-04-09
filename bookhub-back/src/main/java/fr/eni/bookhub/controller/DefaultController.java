package fr.eni.bookhub.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @GetMapping("/healthcheck")
    public String index(){
        return "OK";
    }
}
