package com.kidTrack.repositories;

import com.kidTrack.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    
    List<Chat> findByConversationIdOrderBySentAtAsc(Integer conversationId);
    
    @Query("SELECT c FROM Chat c WHERE c.conversation.id = ?1 AND c.isRead = false AND c.senderId != ?2")
    List<Chat> findUnreadMessagesByConversationAndReceiver(Integer conversationId, Integer receiverId);
}
