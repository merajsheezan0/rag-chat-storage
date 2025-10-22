package com.rag.chat.storage.rag_chat_storage.controller;

import com.rag.chat.storage.rag_chat_storage.entity.ChatSession;
import com.rag.chat.storage.rag_chat_storage.service.ChatSessionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {

    private final ChatSessionService sessionService;

    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

    public SessionController(ChatSessionService sessionService) {
        this.sessionService = sessionService;
    }

    // DTOs
    public record CreateSessionRequest(
            @NotBlank String userId,
            @Size(min = 1, max = 100) String name
    ) {}

    public record SessionResponse(Long id, String userId, String name, boolean favorite, Instant createdAt, Instant updatedAt) {}

    // Create a new session
    @PostMapping
    public ResponseEntity<SessionResponse> create(@Valid @RequestBody CreateSessionRequest req) {
        logger.info("SessionController create called with userId: {}, name: {}", req.userId(), req.name());
        ChatSession s = sessionService.createSession(req.userId(), req.name());
        return ResponseEntity.status(201)
                .body(new SessionResponse(s.getId(), s.getUserId(), s.getName(), s.isFavorite(), s.getCreatedAt(), s.getUpdatedAt()));
    }

    // Rename session
    @PatchMapping("/{id}/rename")
    public ResponseEntity<SessionResponse> rename(@PathVariable Long id, @RequestBody Map<String, String> body) {
        logger.info("SessionController rename called with id: {}, new name: {}", id, body.get("name"));
        ChatSession s = sessionService.renameSession(id, body.get("name"));
        return ResponseEntity.ok(new SessionResponse(s.getId(), s.getUserId(), s.getName(), s.isFavorite(), s.getCreatedAt(), s.getUpdatedAt()));
    }

    // Mark/unmark favorite
    @PatchMapping("/{id}/favorite")
    public ResponseEntity<SessionResponse> setFavorite(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        logger.info("SessionController setFavorite called with id: {}, favorite: {}", id, body.get("favorite"));
        boolean favorite = body.get("favorite");
        ChatSession s = sessionService.setFavorite(id, favorite);
        return ResponseEntity.ok(new SessionResponse(s.getId(), s.getUserId(), s.getName(), s.isFavorite(), s.getCreatedAt(), s.getUpdatedAt()));
    }

    // Delete session
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("SessionController delete called with id: {}", id);
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
