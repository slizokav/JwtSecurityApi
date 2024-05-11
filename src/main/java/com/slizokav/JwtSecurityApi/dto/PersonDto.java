package com.slizokav.JwtSecurityApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonDto {
    private String username;
    private String password;

}
