package com.example.eureka.client.order.infrastructure.client.user;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserResponseDto {

    private Long id;
    private String username;
    private String role;
    private String slackId;
    private UUID hubId;
    private UUID companyId; // 담당 업체
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
