package com.rag.chat.storage.rag_chat_storage.repository;

import com.rag.chat.storage.rag_chat_storage.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findBySessionIdOrderByCreatedAtAsc(Long sessionId, Pageable pageable);
    void deleteBySessionId(Long sessionId);
}
