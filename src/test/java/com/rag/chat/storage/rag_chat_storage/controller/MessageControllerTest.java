package com.rag.chat.storage.rag_chat_storage.controller;

import com.rag.chat.storage.rag_chat_storage.entity.ChatMessage;
import com.rag.chat.storage.rag_chat_storage.service.ChatMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MessageControllerTest {

    @Mock
    private ChatMessageService messageService;

    @InjectMocks
    private MessageController messageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddMessage() {
        Long sessionId = 1L;
        MessageController.AddMessageRequest request = new MessageController.AddMessageRequest(
                "user1",
                "Hello, world!",
                "{\"context\":\"test\"}"
        );

        ChatMessage savedMessage = new ChatMessage();
        savedMessage.setId(100L);
        savedMessage.setSender(request.sender());
        savedMessage.setContent(request.content());
        savedMessage.setContextJson(request.contextJson());

        when(messageService.addMessage(eq(sessionId), any(ChatMessage.class))).thenReturn(savedMessage);

        ResponseEntity<Long> response = messageController.addMessage(sessionId, request);

        // Verify response
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(100L);

        // Capture the argument passed to service
        ArgumentCaptor<ChatMessage> captor = ArgumentCaptor.forClass(ChatMessage.class);
        verify(messageService, times(1)).addMessage(eq(sessionId), captor.capture());

        ChatMessage captured = captor.getValue();
        assertThat(captured.getSender()).isEqualTo(request.sender());
        assertThat(captured.getContent()).isEqualTo(request.content());
        assertThat(captured.getContextJson()).isEqualTo(request.contextJson());
    }

    @Test
    void testGetMessages() {
        Long sessionId = 1L;
        int page = 0;
        int size = 50;

        ChatMessage msg1 = new ChatMessage();
        msg1.setId(1L);
        msg1.setSender("user1");
        msg1.setContent("Hello");

        ChatMessage msg2 = new ChatMessage();
        msg2.setId(2L);
        msg2.setSender("ai");
        msg2.setContent("Hi there!");

        Page<ChatMessage> pageResult = new PageImpl<>(List.of(msg1, msg2), PageRequest.of(page, size), 2);
        when(messageService.getMessages(sessionId, page, size)).thenReturn(pageResult);

        ResponseEntity<Page<ChatMessage>> response = messageController.getMessages(sessionId, page, size);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(2);
        assertThat(response.getBody().getContent()).containsExactly(msg1, msg2);

        verify(messageService, times(1)).getMessages(sessionId, page, size);
    }
}
