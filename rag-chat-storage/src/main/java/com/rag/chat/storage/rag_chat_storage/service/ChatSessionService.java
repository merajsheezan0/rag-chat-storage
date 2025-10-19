package com.rag.chat.storage.rag_chat_storage.service;

import com.rag.chat.storage.rag_chat_storage.entity.ChatSession;
import com.rag.chat.storage.rag_chat_storage.repository.ChatSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ChatSessionService {

    private final ChatSessionRepository sessionRepo;

    public ChatSessionService(ChatSessionRepository sessionRepo) {
        this.sessionRepo = sessionRepo;
    }

    @Transactional
    public ChatSession createSession(String userId, String name) {
        ChatSession s = new ChatSession();
        s.setUserId(userId);
        s.setName(name == null ? "New Chat" : name);
        s.setCreatedAt(Instant.now());
        s.setUpdatedAt(Instant.now());
        return sessionRepo.save(s);
    }

    @Transactional
    public ChatSession renameSession(Long sessionId, String newName) {
        ChatSession s = sessionRepo.findById(sessionId).orElseThrow(() -> new IllegalArgumentException("Session not found"));
        s.setName(newName);
        s.setUpdatedAt(Instant.now());
        return sessionRepo.save(s);
    }

    @Transactional
    public void deleteSession(Long sessionId) {
        sessionRepo.deleteById(sessionId);
    }

    @Transactional
    public ChatSession setFavorite(Long sessionId, boolean favorite) {
        ChatSession s = sessionRepo.findById(sessionId).orElseThrow(() -> new IllegalArgumentException("Session not found"));
        s.setFavorite(favorite);
        s.setUpdatedAt(Instant.now());
        return sessionRepo.save(s);
    }
}
