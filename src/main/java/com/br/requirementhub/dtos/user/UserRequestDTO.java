package com.br.requirementhub.dtos.user;

import com.br.requirementhub.enums.Role;
import lombok.Data;

@Data
public class UserRequestDTO {
    private String name;
    private Role role;
}