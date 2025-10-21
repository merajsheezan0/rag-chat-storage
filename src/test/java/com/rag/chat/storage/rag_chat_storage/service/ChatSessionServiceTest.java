package com.rag.chat.storage.rag_chat_storage.service;

import com.rag.chat.storage.rag_chat_storage.entity.ChatSession;
import com.rag.chat.storage.rag_chat_storage.repository.ChatSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatSessionServiceTest {

    @Mock
    private ChatSessionRepository sessionRepo;

    @InjectMocks
    private ChatSessionService sessionService;

    private ChatSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        session = new ChatSession();
        session.setId(1L);
        session.setUserId("user123");
        session.setName("Old Chat");
        session.setCreatedAt(Instant.now());
        session.setUpdatedAt(Instant.now());
    }

    @Test
    void testCreateSession_WithName() {
        when(sessionRepo.save(any(ChatSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChatSession result = sessionService.createSession("user123", "My Chat");

        assertNotNull(result);
        assertEquals("user123", result.getUserId());
        assertEquals("My Chat", result.getName());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        verify(sessionRepo, times(1)).save(any(ChatSession.class));
    }

    @Test
    void testCreateSession_WithoutName_UsesDefault() {
        when(sessionRepo.save(any(ChatSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChatSession result = sessionService.createSession("user123", null);

        assertEquals("New Chat", result.getName());
        verify(sessionRepo, times(1)).save(any(ChatSession.class));
    }

    @Test
    void testRenameSession_Success() {
        when(sessionRepo.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepo.save(any(ChatSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChatSession updated = sessionService.renameSession(1L, "Renamed Chat");

        assertEquals("Renamed Chat", updated.getName());
        verify(sessionRepo, times(1)).findById(1L);
        verify(sessionRepo, times(1)).save(session);
    }

    @Test
    void testRenameSession_NotFound_ThrowsException() {
        when(sessionRepo.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> sessionService.renameSession(99L, "New Name")
        );

        assertEquals("Session not found", ex.getMessage());
        verify(sessionRepo, times(1)).findById(99L);
        verify(sessionRepo, never()).save(any());
    }

    @Test
    void testDeleteSession() {
        doNothing().when(sessionRepo).deleteById(1L);

        sessionService.deleteSession(1L);

        verify(sessionRepo, times(1)).deleteById(1L);
    }

    @Test
    void testSetFavorite_Success() {
        when(sessionRepo.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepo.save(any(ChatSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChatSession updated = sessionService.setFavorite(1L, true);

        assertTrue(updated.isFavorite());
        verify(sessionRepo, times(1)).findById(1L);
        verify(sessionRepo, times(1)).save(session);
    }

    @Test
    void testSetFavorite_NotFound_ThrowsException() {
        when(sessionRepo.findById(100L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> sessionService.setFavorite(100L, true)
        );

        assertEquals("Session not found", ex.getMessage());
        verify(sessionRepo, times(1)).findById(100L);
        verify(sessionRepo, never()).save(any());
    }
}
