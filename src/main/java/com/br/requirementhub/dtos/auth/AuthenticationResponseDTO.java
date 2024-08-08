package com.br.requirementhub.dtos.auth;
import com.br.requirementhub.enums.Role;


import com.br.requirementhub.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponseDTO {
    private String token;
    private Role role;
}