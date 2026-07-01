package com.flow.payflow.controller;

import com.flow.payflow.config.security.TokenService;
import com.flow.payflow.dto.AuthDto;
import com.flow.payflow.entity.Store;
import com.flow.payflow.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/transaction/auth/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private StoreRepository storeRepository;

    @PostMapping
    public Store login(@RequestBody AuthDto authDto) {

        var usernamePassword =
                new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());

        authenticationManager.authenticate(usernamePassword);

        Optional<Store> storeOptional =  storeRepository.findByEmail(authDto.email());
        Store store = storeOptional.get();
        String token = tokenService.generateToken(store);
        store.setToken(token);

        return store;
    }

}
