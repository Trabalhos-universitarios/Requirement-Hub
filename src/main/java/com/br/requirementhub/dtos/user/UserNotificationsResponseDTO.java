package com.br.requirementhub.dtos.user;

import com.br.requirementhub.enums.Role;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserNotificationsResponseDTO {
    private List<Long> notifications;
}