package com.chat.QueryNest.service;

import com.chat.QueryNest.entity.DocumentFile;
import com.chat.QueryNest.repository.DocumentRepository;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

@Service
public class DocumentService {

    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024;
    private final DocumentRepository documentRepository;
    private final Tika tika = new Tika();

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Transactional
    public DocumentFile upload(MultipartFile file)
            throws IOException, TikaException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please select a non-empty file.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "Maximum file size is 10 MB.");
        }

        String filename = file.getOriginalFilename();

        if (filename == null || filename.isBlank()
                || filename.contains("/")
                || filename.contains("\\")
                || filename.contains("..")) {
            throw new IllegalArgumentException("Invalid file name.");
        }

        String lowerName = filename.toLowerCase(Locale.ROOT);
        List<String> allowedExtensions = List.of(
                ".pdf", ".txt", ".docx", ".doc", ".rtf",
                ".html", ".htm", ".md", ".csv");

        boolean allowed = allowedExtensions.stream()
                .anyMatch(lowerName::endsWith);

        if (!allowed) {
            throw new IllegalArgumentException(
                    "Unsupported format. Upload PDF, TXT, DOCX, DOC, "
                            + "RTF, HTML, Markdown or CSV.");
        }

        String text;

        try (InputStream input = file.getInputStream()) {
            text = tika.parseToString(input);
        }

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                    "No readable text was found in this file. "
                            + "Scanned PDFs may require OCR.");
        }

        if (text.length() > 500_000) {
            throw new IllegalArgumentException(
                    "Extracted text is too large for this demo. "
                            + "Please upload a smaller document.");
        }

        DocumentFile document = new DocumentFile();
        document.setFileName(filename);
        document.setContentType(
                file.getContentType() == null
                        ? "application/octet-stream"
                        : file.getContentType());
        document.setExtractedText(text);

        return documentRepository.save(document);
    }

    public List<DocumentFile> findAll() {
        return documentRepository.findAll();
    }

    public DocumentFile findById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Document not found."));
    }
}
