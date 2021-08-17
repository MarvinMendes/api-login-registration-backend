package com.marvin.apiuserregisteremailsender.controller;

import com.marvin.apiuserregisteremailsender.domain.registration.RegistrationRequest;
import com.marvin.apiuserregisteremailsender.exceptions.*;
import com.marvin.apiuserregisteremailsender.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/registration")
public class UserAppController {

    private AppUserService appUserService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request) throws UserAlreadyRegisterException, EmailFailedException {
        return appUserService.save(request);
    }

    @GetMapping(path = "confirm")
    public String confirmation(@RequestParam("token") String token) throws TokenExpiredException, TokenNotFoundException, UserWrongTokenException {
        return appUserService.confirmToken(token);
    }
}
