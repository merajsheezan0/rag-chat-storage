package com.rag.chat.storage.rag_chat_storage.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    @JsonBackReference
    private ChatSession session;

    @Column(name = "sender", nullable = false)
    private String sender; // e.g. USER, ASSISTANT, SYSTEM

    @Lob
    @Column(name = "content")
    private String content;

    @Lob
    @Column(name = "context_json")
    private String contextJson;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
}
