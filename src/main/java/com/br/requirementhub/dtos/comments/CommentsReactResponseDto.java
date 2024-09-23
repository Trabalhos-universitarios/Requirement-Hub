package com.br.requirementhub.dtos.comments;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CommentsReactResponseDto {

    private Long id;
    private Long commentId;
    private String userId;
    private String emoji;
}
