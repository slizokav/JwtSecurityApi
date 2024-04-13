package com.slizokav.CrudSecurityRestApplication.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonDto {
    private String username;
    private String password;

}
