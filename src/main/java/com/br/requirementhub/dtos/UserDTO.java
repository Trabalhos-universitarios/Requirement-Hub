package com.br.requirementhub.dtos;

import com.br.requirementhub.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private Role role;
}