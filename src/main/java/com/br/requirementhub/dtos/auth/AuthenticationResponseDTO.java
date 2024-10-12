package com.br.requirementhub.dtos.auth;
import com.br.requirementhub.enums.Role;


import com.br.requirementhub.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {
    private Long id;
    private String token;
    private String role;
    private String name;
    private String image;
}
