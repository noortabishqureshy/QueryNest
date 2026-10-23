package com.chat.QueryNest.repository;

import com.chat.QueryNest.entity.DocumentFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository
        extends JpaRepository<DocumentFile, Long> {
}
