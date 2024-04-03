package com.slizokav.CrudSecurityRestApplication.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "description")
    private String description;

    public Person(String username, String password, String description) {
        this.username = username;
        this.password = password;
        this.description = description;
    }

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
