package com.chat.QueryNest.controller;

import com.chat.QueryNest.entity.ChatMessage;
import com.chat.QueryNest.entity.DocumentFile;
import com.chat.QueryNest.service.DocumentChatService;
import com.chat.QueryNest.service.DocumentService;
import org.apache.tika.exception.TikaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentChatService chatService;

    public DocumentController(
            DocumentService documentService,
            DocumentChatService chatService) {
        this.documentService = documentService;
        this.chatService = chatService;
    }

    @PostMapping(value = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> upload(
            @RequestParam("file") MultipartFile file) {

        try {
            DocumentFile document = documentService.upload(file);

            return Map.of(
                    "id", document.getId(),
                    "fileName", document.getFileName(),
                    "uploadedAt", document.getUploadedAt(),
                    "message", "Document uploaded successfully.");
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (IOException | TikaException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Could not read the uploaded document.");
        }
    }

    @GetMapping("/documents")
    public List<Map<String, Object>> documents() {
        return documentService.findAll().stream()
                .map(doc -> Map.<String, Object>of(
                        "id", doc.getId(),
                        "fileName", doc.getFileName(),
                        "contentType", doc.getContentType(),
                        "uploadedAt", doc.getUploadedAt()))
                .toList();
    }

    public record ChatRequest(String question) {
    }

    @PostMapping("/documents/{id}/chat")
    public Map<String, String> chat(
            @PathVariable Long id,
            @RequestBody ChatRequest request) {

        try {
            String answer = chatService.ask(id, request.question());
            return Map.of("answer", answer);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping("/documents/{id}/messages")
    public List<Map<String, Object>> messages(@PathVariable Long id) {
        try {
            return chatService.getHistory(id).stream()
                    .map(this::toMessageMap)
                    .toList();
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    private Map<String, Object> toMessageMap(ChatMessage message) {
        return Map.of(
                "id", message.getId(),
                "role", message.getRole(),
                "content", message.getContent(),
                "createdAt", message.getCreatedAt());
    }
}
