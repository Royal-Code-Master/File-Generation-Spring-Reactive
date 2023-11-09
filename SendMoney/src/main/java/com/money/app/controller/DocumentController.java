package com.money.app.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.money.app.service.DocumentService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/doc")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/data")
    public Mono<String> exportDocumentsToCSV() {
    	
        return documentService.exportDocumentsToCSV();
        
    }
}
