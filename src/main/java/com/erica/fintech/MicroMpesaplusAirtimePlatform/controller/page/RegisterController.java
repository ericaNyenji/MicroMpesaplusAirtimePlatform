package com.erica.fintech.MicroMpesaplusAirtimePlatform.controller.page;

import com.erica.fintech.MicroMpesaplusAirtimePlatform.service.RegisterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public String register(@RequestParam String firstName, @RequestParam String middleName, @RequestParam String surname, @RequestParam String nationalId, @RequestParam String phone,            @RequestParam String password ) throws Exception {

        registerService.register(firstName, middleName, surname, nationalId, phone, password);

        return "redirect:/login";
    }
}
