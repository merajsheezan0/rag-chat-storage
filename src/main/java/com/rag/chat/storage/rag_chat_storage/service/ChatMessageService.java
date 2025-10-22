package com.rag.chat.storage.rag_chat_storage.service;

import com.rag.chat.storage.rag_chat_storage.entity.ChatMessage;
import com.rag.chat.storage.rag_chat_storage.entity.ChatSession;
import com.rag.chat.storage.rag_chat_storage.repository.ChatMessageRepository;
import com.rag.chat.storage.rag_chat_storage.repository.ChatSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Service
public class ChatMessageService {
    private final ChatSessionRepository sessionRepo;
    private final ChatMessageRepository messageRepo;

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageService.class);

    public ChatMessageService(ChatSessionRepository sessionRepo, ChatMessageRepository messageRepo) {
        this.sessionRepo = sessionRepo;
        this.messageRepo = messageRepo;
    }

    @Transactional
    public ChatMessage addMessage(Long sessionId, ChatMessage message) {
        logger.info("Adding message to session {}: {}", sessionId, message);
        ChatSession s = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        message.setSession(s);
        ChatMessage saved = messageRepo.save(message);
        s.setUpdatedAt(Instant.now());
        sessionRepo.save(s);
        logger.info("Message added with ID {} for session id {}", saved.getId(), sessionId);
        return saved;
    }

    public Page<ChatMessage> getMessages(Long sessionId, int page, int size) {
        logger.info("Retrieving messages for session {}: page {}, size {}", sessionId, page, size);
        return messageRepo.findBySessionIdOrderByCreatedAtAsc(sessionId, PageRequest.of(page, size));
    }
}
