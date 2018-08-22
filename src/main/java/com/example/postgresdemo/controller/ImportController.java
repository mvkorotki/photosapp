package com.example.postgresdemo.controller;

import com.example.postgresdemo.model.PhotoFileSource;
import com.example.postgresdemo.repository.SourcesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class ImportController {

    private static Logger log = LoggerFactory.getLogger(ImportController.class);
    @Autowired
    private SourcesRepository sourcesRepository;

    @GetMapping("/sources")
    public Page<PhotoFileSource> getSources(Pageable pageable) {
        return sourcesRepository.findAll(pageable);
    }


    @PostMapping("/sources")
    public PhotoFileSource addSource(@Valid @RequestBody PhotoFileSource source) {
        return sourcesRepository.save(source);
    }

    @PutMapping("/sources/{sourceId}")
    public PhotoFileSource updateSource(@PathVariable Long sourceId,
                                   @Valid @RequestBody PhotoFileSource sourceRequest) {
        return sourcesRepository.findById(sourceId)
                .map(source -> {
                    source.setPath(sourceRequest.getPath());
                    return sourcesRepository.save(source);
                }).orElseThrow(() -> new ResourceNotFoundException("Source not found with id " + sourceId));
    }


    @DeleteMapping("/sources/{sourceId}")
    public ResponseEntity<?> deleteSource(@PathVariable Long sourceId) {
        return sourcesRepository.findById(sourceId)
                .map(source -> {
                    sourcesRepository.delete(source);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Source not found with id " + sourceId));
    }
}
