package com.slizokav.CrudSecurityRestApplication.controlers;

import com.slizokav.CrudSecurityRestApplication.dto.response.BodyResponse;
import com.slizokav.CrudSecurityRestApplication.dto.PersonDto;
import com.slizokav.CrudSecurityRestApplication.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody PersonDto personDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(personDto.getUsername(), personDto.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new BodyResponse(HttpStatus.BAD_REQUEST.toString(), "Неправильный логин или пароль"), HttpStatus.BAD_REQUEST);
        }

        String jwt = "Token: " + jwtUtil.generateToken(personDto.getUsername());
        return new ResponseEntity<>(new BodyResponse(HttpStatus.OK.toString(), jwt), HttpStatus.OK);
    }
}
