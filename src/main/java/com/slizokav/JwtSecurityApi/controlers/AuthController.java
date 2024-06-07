package com.slizokav.JwtSecurityApi.controlers;

import com.slizokav.JwtSecurityApi.dto.response.BodyResponse;
import com.slizokav.JwtSecurityApi.dto.PersonDto;
import com.slizokav.JwtSecurityApi.model.Person;
import com.slizokav.JwtSecurityApi.security.JWTUtil;
import com.slizokav.JwtSecurityApi.service.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final PersonService personService;

    @Autowired
    public AuthController(JWTUtil jwtUtil, AuthenticationManager authenticationManager, ModelMapper modelMapper, PersonService personService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.personService = personService;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody PersonDto personDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(personDto.getUsername(), personDto.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new BodyResponse(HttpStatus.BAD_REQUEST.toString(), "Неправильный логин или пароль"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new BodyResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Возникла исключительная ситуация: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jwt = "Token: " + jwtUtil.generateToken(personDto.getUsername());
        return new ResponseEntity<>(new BodyResponse(HttpStatus.OK.toString(), jwt), HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody PersonDto personDto) {
        try {
            Person person = convertPersonDtoToPerson(personDto);
            personService.create(person);
        } catch (Exception e) {
            return new ResponseEntity<>(new BodyResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Возникла исключительная ситуация: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jwt = "Token: " + jwtUtil.generateToken(personDto.getUsername());
        return new ResponseEntity<>(new BodyResponse(HttpStatus.CREATED.toString(), jwt), HttpStatus.CREATED);
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin response";
    }

    public Person convertPersonDtoToPerson(PersonDto personDto) {
        return modelMapper.map(personDto, Person.class);
    }

}
