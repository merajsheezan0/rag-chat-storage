package com.rag.chat.storage.rag_chat_storage.controller;

import com.rag.chat.storage.rag_chat_storage.entity.ChatMessage;
import com.rag.chat.storage.rag_chat_storage.service.ChatMessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessions/{sessionId}/messages")
public class MessageController {

    private final ChatMessageService messageService;

    public MessageController(ChatMessageService messageService) {
        this.messageService = messageService;
    }

    public record AddMessageRequest(
            @NotBlank String sender,
            @NotBlank String content,
            String contextJson
    ) {}

    // Add a message
    @PostMapping
    public ResponseEntity<Long> addMessage(@PathVariable Long sessionId, @Valid @RequestBody AddMessageRequest req) {
        ChatMessage m = new ChatMessage();
        m.setSender(req.sender());
        m.setContent(req.content());
        m.setContextJson(req.contextJson());
        ChatMessage saved = messageService.addMessage(sessionId, m);
        return ResponseEntity.status(201).body(saved.getId());
    }

    // Get paginated messages
    @GetMapping
    public ResponseEntity<Page<ChatMessage>> getMessages(
            @PathVariable Long sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(messageService.getMessages(sessionId, page, size));
    }
}
