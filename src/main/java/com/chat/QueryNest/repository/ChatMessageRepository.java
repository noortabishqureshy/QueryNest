package com.chat.QueryNest.repository;

import com.chat.QueryNest.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByDocumentIdOrderByCreatedAtAscIdAsc(
            Long documentId);
}
