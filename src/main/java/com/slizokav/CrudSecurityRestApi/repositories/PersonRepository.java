package com.slizokav.CrudSecurityRestApi.repositories;

import com.slizokav.CrudSecurityRestApi.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String name);
}
