package com.slizokav.CrudSecurityRestApplication.service;

import com.slizokav.CrudSecurityRestApplication.model.Person;
import com.slizokav.CrudSecurityRestApplication.repositories.PersonRepository;
import com.slizokav.CrudSecurityRestApplication.security.PersonUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonUserDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUsername(username);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("Такого пользователя не существует");
        }
        return new PersonUserDetails(person.get());
    }
}
