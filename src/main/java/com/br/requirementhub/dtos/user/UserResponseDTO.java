package com.br.requirementhub.dtos.user;

import com.br.requirementhub.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private Role role;
}