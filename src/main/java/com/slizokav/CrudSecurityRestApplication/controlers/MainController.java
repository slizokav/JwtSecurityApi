package com.slizokav.CrudSecurityRestApplication.controlers;

import com.slizokav.CrudSecurityRestApplication.dto.PersonDto;
import com.slizokav.CrudSecurityRestApplication.model.Person;
import com.slizokav.CrudSecurityRestApplication.security.JWTUtil;
import com.slizokav.CrudSecurityRestApplication.service.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MainController {
    private final PersonService personService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public MainController(PersonService personService, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.personService = personService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }

    @PostMapping("/registration")
    public Map<String, String> registration(@RequestBody PersonDto personDto) {
        Person person = convertPersonDtoToPerson(personDto);
        personService.create(person);

        String jwt = jwtUtil.generateToken(person.getUsername());

        return Map.of("token", jwt);
    }

    @PostMapping("/perform/login")
    public Map<String, String> performLogin(@RequestBody PersonDto personDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(personDto.getUsername(), personDto.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
           return Map.of("error", "Bad credentials");
        }
        String jwt = jwtUtil.generateToken(personDto.getUsername());
        return Map.of("token", jwt);
    }

    public Person convertPersonDtoToPerson(PersonDto personDto) {
        return modelMapper.map(personDto, Person.class);
    }

}