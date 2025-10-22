package com.rag.chat.storage.rag_chat_storage.service;

import com.rag.chat.storage.rag_chat_storage.entity.ChatSession;
import com.rag.chat.storage.rag_chat_storage.repository.ChatSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ChatSessionService {

    private final ChatSessionRepository sessionRepo;

    private static final Logger logger = LoggerFactory.getLogger(ChatSessionService.class);

    public ChatSessionService(ChatSessionRepository sessionRepo) {
        this.sessionRepo = sessionRepo;
    }

    @Transactional
    public ChatSession createSession(String userId, String name) {
        logger.info("Starting creation of chat session for userId : {} and username: {}", userId, name);
        ChatSession s = new ChatSession();
        s.setUserId(userId);
        s.setName(name == null ? "New Chat" : name);
        s.setCreatedAt(Instant.now());
        s.setUpdatedAt(Instant.now());
        return sessionRepo.save(s);
    }

    @Transactional
    public ChatSession renameSession(Long sessionId, String newName) {
        logger.info("Renaming chat session id: {} to new name: {}", sessionId, newName);
        ChatSession s = sessionRepo.findById(sessionId).orElseThrow(() -> new IllegalArgumentException("Session not found"));
        s.setName(newName);
        s.setUpdatedAt(Instant.now());
        return sessionRepo.save(s);
    }

    @Transactional
    public void deleteSession(Long sessionId) {
        logger.info("Deleting chat session id: {}", sessionId);
        sessionRepo.deleteById(sessionId);
    }

    @Transactional
    public ChatSession setFavorite(Long sessionId, boolean favorite) {
        logger.info("Setting favorite={} for chat session id: {}", favorite, sessionId);
        ChatSession s = sessionRepo.findById(sessionId).orElseThrow(() -> new IllegalArgumentException("Session not found"));
        s.setFavorite(favorite);
        s.setUpdatedAt(Instant.now());
        return sessionRepo.save(s);
    }
}
