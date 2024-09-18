package com.example.eureka.client.order.global.chat;

import com.example.eureka.client.order.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final VertexAiGeminiChatModel vertexAiGeminiChatModel;

    @GetMapping("/chat")
    public ResponseEntity<ResponseDto<ChatResponseDto>> makeRequest(@RequestBody String message) {

        String vertexAiGeminiResponse  = vertexAiGeminiChatModel.call(message);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                new ChatResponseDto(null, vertexAiGeminiResponse)
            ));
    }
}


