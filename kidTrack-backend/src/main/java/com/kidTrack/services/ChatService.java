package com.kidTrack.services;

import com.kidTrack.models.Chat;
import com.kidTrack.models.Conversation;
import com.kidTrack.repositories.ChatRepository;
import com.kidTrack.repositories.ConversationRepository;
import com.kidTrack.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatService {
    
    @Autowired
    private ChatRepository chatRepository;
    
    @Autowired
    private ConversationRepository conversationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get or create a conversation between two users
     */
    public Conversation getOrCreateConversation(Integer user1Id, Integer user2Id) {
        Optional<Conversation> existing = conversationRepository.findConversationBetweenUsers(user1Id, user2Id);
        
        if (existing.isPresent()) {
            return existing.get();
        }
        
        // Create new conversation
        Conversation conversation = new Conversation();
        conversation.setUser1Id(user1Id);
        conversation.setUser2Id(user2Id);
        return conversationRepository.save(conversation);
    }
    
    /**
     * Get all conversations for a user with last message info
     */
    public List<Map<String, Object>> getUserConversations(Integer userId) {
        List<Conversation> conversations = conversationRepository.findConversationsByUserId(userId);
        
        return conversations.stream()
            .map(conv -> {
                Map<String, Object> dto = new HashMap<>();
                dto.put("id", conv.getId());
                dto.put("lastMessageAt", conv.getLastMessageAt());
                
                // Get other user info
                Integer otherUserId = conv.getUser1Id().equals(userId) ? conv.getUser2Id() : conv.getUser1Id();
                userRepository.findById(otherUserId).ifPresent(user -> {
                    dto.put("otherUserId", user.getId());
                    dto.put("otherUserName", user.getFirstName() + " " + user.getLastName());
                    dto.put("otherUserEmail", user.getEmail());
                });
                
                // Get last message
                List<Chat> messages = chatRepository.findByConversationIdOrderBySentAtAsc(conv.getId());
                if (!messages.isEmpty()) {
                    Chat lastMessage = messages.get(messages.size() - 1);
                    dto.put("lastMessage", lastMessage.getMessageText());
                    dto.put("lastMessageSenderId", lastMessage.getSenderId());
                }
                
                // Get unread count
                long unreadCount = chatRepository.findUnreadMessagesByConversationAndReceiver(conv.getId(), userId).size();
                dto.put("unreadCount", unreadCount);
                
                return dto;
            })
            .sorted((a, b) -> ((Date) b.get("lastMessageAt")).compareTo((Date) a.get("lastMessageAt")))
            .collect(Collectors.toList());
    }
    
    /**
     * Get all messages in a conversation
     */
    public List<Map<String, Object>> getConversationMessages(Integer conversationId, Integer currentUserId) {
        List<Chat> messages = chatRepository.findByConversationIdOrderBySentAtAsc(conversationId);
        
        // Mark messages as read
        messages.stream()
            .filter(msg -> !msg.getSenderId().equals(currentUserId) && !msg.getIsRead())
            .forEach(msg -> {
                msg.setIsRead(true);
                chatRepository.save(msg);
            });
        
        return messages.stream()
            .map(this::convertMessageToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Send a message in a conversation
     */
    public Map<String, Object> sendMessage(Integer conversationId, Integer senderId, String messageText, String fileAttach) {
        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new RuntimeException("Conversation not found"));
        
        Chat chat = new Chat();
        chat.setConversation(conversation);
        chat.setSenderId(senderId);
        chat.setMessageText(messageText);
        chat.setFileAttach(fileAttach);
        chat.setIsRead(false);
        
        Chat savedChat = chatRepository.save(chat);
        
        // Update conversation's last message time
        conversationRepository.save(conversation);
        
        return convertMessageToDTO(savedChat);
    }
    
    /**
     * Mark messages as read
     */
    public void markMessagesAsRead(Integer conversationId, Integer userId) {
        List<Chat> unreadMessages = chatRepository.findUnreadMessagesByConversationAndReceiver(conversationId, userId);
        unreadMessages.forEach(msg -> {
            msg.setIsRead(true);
            chatRepository.save(msg);
        });
    }
    
    private Map<String, Object> convertMessageToDTO(Chat chat) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", chat.getId());
        dto.put("conversationId", chat.getConversation().getId());
        dto.put("senderId", chat.getSenderId());
        dto.put("messageText", chat.getMessageText());
        dto.put("fileAttach", chat.getFileAttach());
        dto.put("isRead", chat.getIsRead());
        dto.put("sentAt", chat.getSentAt());
        
        // Get sender info
        userRepository.findById(chat.getSenderId()).ifPresent(user -> {
            dto.put("senderName", user.getFirstName() + " " + user.getLastName());
        });
        
        return dto;
    }
}
