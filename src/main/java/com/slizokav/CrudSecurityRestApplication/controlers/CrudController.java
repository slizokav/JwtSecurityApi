package com.slizokav.CrudSecurityRestApplication.controlers;

import com.slizokav.CrudSecurityRestApplication.dto.response.BodyResponse;
import com.slizokav.CrudSecurityRestApplication.dto.PersonDto;
import com.slizokav.CrudSecurityRestApplication.model.Person;
import com.slizokav.CrudSecurityRestApplication.security.JWTUtil;
import com.slizokav.CrudSecurityRestApplication.service.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CrudController {
    private final PersonService personService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public CrudController(PersonService personService, JWTUtil jwtUtil, ModelMapper modelMapper) {
        this.personService = personService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

    @GetMapping("testUser")
    public String mainPage() {
        return "testUser";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin response";
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody PersonDto personDto) {
        Person person = convertPersonDtoToPerson(personDto);
        personService.create(person);
        String jwt = "Token: " + jwtUtil.generateToken(person.getUsername());

        return new ResponseEntity<>(new BodyResponse(HttpStatus.CREATED.toString(), jwt), HttpStatus.CREATED);
    }

    public Person convertPersonDtoToPerson(PersonDto personDto) {
        return modelMapper.map(personDto, Person.class);
    }
}