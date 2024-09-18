package com.example.eureka.client.order.global.chat;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDto {

    private UUID id;
//    private String chatGptAnswer;
    private String geminiAnswer;
}
