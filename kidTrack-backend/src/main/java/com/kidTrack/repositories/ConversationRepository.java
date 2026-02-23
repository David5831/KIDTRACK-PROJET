package com.kidTrack.repositories;

import com.kidTrack.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    
    @Query("SELECT c FROM Conversation c WHERE (c.user1Id = ?1 AND c.user2Id = ?2) OR (c.user1Id = ?2 AND c.user2Id = ?1)")
    Optional<Conversation> findConversationBetweenUsers(Integer userId1, Integer userId2);
    
    @Query("SELECT c FROM Conversation c WHERE c.user1Id = ?1 OR c.user2Id = ?1 ORDER BY c.lastMessageAt DESC")
    List<Conversation> findConversationsByUserId(Integer userId);
}
