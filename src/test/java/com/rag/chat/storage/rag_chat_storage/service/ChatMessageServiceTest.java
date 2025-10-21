package com.rag.chat.storage.rag_chat_storage.service;

import com.rag.chat.storage.rag_chat_storage.entity.ChatMessage;
import com.rag.chat.storage.rag_chat_storage.entity.ChatSession;
import com.rag.chat.storage.rag_chat_storage.repository.ChatMessageRepository;
import com.rag.chat.storage.rag_chat_storage.repository.ChatSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatMessageServiceTest {

    @Mock
    private ChatSessionRepository sessionRepo;

    @Mock
    private ChatMessageRepository messageRepo;

    @InjectMocks
    private ChatMessageService messageService;

    private ChatSession session;
    private ChatMessage message;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        session = new ChatSession();
        session.setId(1L);
        session.setName("Session 1");
        session.setCreatedAt(Instant.now());
        session.setUpdatedAt(Instant.now());

        message = new ChatMessage();
        message.setId(10L);
        message.setSender("user");
        message.setContent("Hello");
        message.setCreatedAt(Instant.now());
    }

    @Test
    void testAddMessage_Success() {
        // Arrange
        when(sessionRepo.findById(1L)).thenReturn(Optional.of(session));
        when(messageRepo.save(any(ChatMessage.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(sessionRepo.save(any(ChatSession.class))).thenReturn(session);

        // Act
        ChatMessage savedMessage = messageService.addMessage(1L, message);

        // Assert
        assertNotNull(savedMessage);
        assertEquals("Hello", savedMessage.getContent());
        assertEquals(session, savedMessage.getSession());

        // Verify interactions
        verify(sessionRepo, times(1)).findById(1L);
        verify(messageRepo, times(1)).save(any(ChatMessage.class));
        verify(sessionRepo, times(1)).save(any(ChatSession.class));
    }

    @Test
    void testAddMessage_SessionNotFound_ThrowsException() {
        // Arrange
        when(sessionRepo.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> messageService.addMessage(99L, message)
        );

        assertEquals("Session not found", ex.getMessage());
        verify(sessionRepo, times(1)).findById(99L);
        verifyNoInteractions(messageRepo);
    }

    @Test
    void testAddMessage_UpdatesSessionTimestamp() {
        when(sessionRepo.findById(1L)).thenReturn(Optional.of(session));
        when(messageRepo.save(any(ChatMessage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Instant beforeUpdate = session.getUpdatedAt();

        messageService.addMessage(1L, message);

        ArgumentCaptor<ChatSession> sessionCaptor = ArgumentCaptor.forClass(ChatSession.class);
        verify(sessionRepo, times(1)).save(sessionCaptor.capture());
        Instant afterUpdate = sessionCaptor.getValue().getUpdatedAt();

        assertTrue(afterUpdate.isAfter(beforeUpdate), "Session updatedAt should be updated");
    }

    @Test
    void testGetMessages_ReturnsPagedMessages() {
        // Arrange
        List<ChatMessage> messages = List.of(message);
        Page<ChatMessage> page = new PageImpl<>(messages);
        when(messageRepo.findBySessionIdOrderByCreatedAtAsc(eq(1L), any(PageRequest.class)))
                .thenReturn(page);

        // Act
        Page<ChatMessage> result = messageService.getMessages(1L, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Hello", result.getContent().get(0).getContent());
        verify(messageRepo, times(1))
                .findBySessionIdOrderByCreatedAtAsc(eq(1L), any(PageRequest.class));
    }
}

