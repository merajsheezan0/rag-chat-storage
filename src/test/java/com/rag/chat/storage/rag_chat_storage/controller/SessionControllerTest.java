package com.rag.chat.storage.rag_chat_storage.controller;

import com.rag.chat.storage.rag_chat_storage.entity.ChatSession;
import com.rag.chat.storage.rag_chat_storage.service.ChatSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SessionControllerTest {

    @Mock
    private ChatSessionService sessionService;

    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSession() {
        SessionController.CreateSessionRequest request =
                new SessionController.CreateSessionRequest("user123", "My Session");

        ChatSession mockSession = new ChatSession();
        mockSession.setId(1L);
        mockSession.setUserId(request.userId());
        mockSession.setName(request.name());
        mockSession.setFavorite(false);
        mockSession.setCreatedAt(Instant.now());
        mockSession.setUpdatedAt(Instant.now());

        when(sessionService.createSession(request.userId(), request.name())).thenReturn(mockSession);

        ResponseEntity<SessionController.SessionResponse> response = sessionController.create(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(1L);
        assertThat(response.getBody().userId()).isEqualTo(request.userId());
        assertThat(response.getBody().name()).isEqualTo(request.name());

        verify(sessionService, times(1)).createSession(request.userId(), request.name());
    }

    @Test
    void testRenameSession() {
        Long sessionId = 1L;
        String newName = "Renamed Session";
        Map<String, String> body = Map.of("name", newName);

        ChatSession mockSession = new ChatSession();
        mockSession.setId(sessionId);
        mockSession.setUserId("user123");
        mockSession.setName(newName);
        mockSession.setFavorite(false);
        mockSession.setCreatedAt(Instant.now());
        mockSession.setUpdatedAt(Instant.now());

        when(sessionService.renameSession(sessionId, newName)).thenReturn(mockSession);

        ResponseEntity<SessionController.SessionResponse> response = sessionController.rename(sessionId, body);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo(newName);

        verify(sessionService, times(1)).renameSession(sessionId, newName);
    }

    @Test
    void testSetFavorite() {
        Long sessionId = 1L;
        boolean favorite = true;
        Map<String, Boolean> body = Map.of("favorite", favorite);

        ChatSession mockSession = new ChatSession();
        mockSession.setId(sessionId);
        mockSession.setUserId("user123");
        mockSession.setName("Session1");
        mockSession.setFavorite(favorite);
        mockSession.setCreatedAt(Instant.now());
        mockSession.setUpdatedAt(Instant.now());

        when(sessionService.setFavorite(sessionId, favorite)).thenReturn(mockSession);

        ResponseEntity<SessionController.SessionResponse> response = sessionController.setFavorite(sessionId, body);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().favorite()).isTrue();

        verify(sessionService, times(1)).setFavorite(sessionId, favorite);
    }

    @Test
    void testDeleteSession() {
        Long sessionId = 1L;

        doNothing().when(sessionService).deleteSession(sessionId);

        ResponseEntity<Void> response = sessionController.delete(sessionId);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);

        verify(sessionService, times(1)).deleteSession(sessionId);
    }
}

