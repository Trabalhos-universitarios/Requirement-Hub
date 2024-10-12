package com.br.requirementhub.dtos.user;

import com.br.requirementhub.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;

    private String name;

    private Role role;

    private String image;
}
