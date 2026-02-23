package com.kidTrack.controllers;

import com.kidTrack.models.Conversation;
import com.kidTrack.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {
    
    @Autowired
    private ChatService chatService;
    
    /**
     * Get or create a conversation between two users
     */
    @PostMapping("/conversation")
    public ResponseEntity<Conversation> getOrCreateConversation(@RequestBody Map<String, Integer> request) {
        Integer user1Id = request.get("user1Id");
        Integer user2Id = request.get("user2Id");
        return ResponseEntity.ok(chatService.getOrCreateConversation(user1Id, user2Id));
    }
    
    /**
     * Get all conversations for a user
     */
    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getUserConversations(@PathVariable Integer userId) {
        return ResponseEntity.ok(chatService.getUserConversations(userId));
    }
    
    /**
     * Get all messages in a conversation
     */
    @GetMapping("/conversation/{conversationId}/messages")
    public ResponseEntity<List<Map<String, Object>>> getConversationMessages(
            @PathVariable Integer conversationId,
            @RequestParam Integer userId) {
        return ResponseEntity.ok(chatService.getConversationMessages(conversationId, userId));
    }
    
    /**
     * Send a message in a conversation
     */
    @PostMapping("/conversation/{conversationId}/message")
    public ResponseEntity<Map<String, Object>> sendMessage(
            @PathVariable Integer conversationId,
            @RequestBody Map<String, Object> request) {
        Integer senderId = (Integer) request.get("senderId");
        String messageText = (String) request.get("messageText");
        String fileAttach = (String) request.get("fileAttach");
        
        return ResponseEntity.ok(chatService.sendMessage(conversationId, senderId, messageText, fileAttach));
    }
    
    /**
     * Mark messages as read
     */
    @PostMapping("/conversation/{conversationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Integer conversationId,
            @RequestBody Map<String, Integer> request) {
        Integer userId = request.get("userId");
        chatService.markMessagesAsRead(conversationId, userId);
        return ResponseEntity.ok().build();
    }
}
