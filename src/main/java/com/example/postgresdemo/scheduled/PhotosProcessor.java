package com.example.postgresdemo.scheduled;

import com.example.postgresdemo.model.PhotoFile;
import com.example.postgresdemo.repository.PhotosRepository;
import com.example.postgresdemo.repository.SourcesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Component
public class PhotosProcessor {

    private static final Logger log = LoggerFactory.getLogger(PhotosProcessor.class);

    @Autowired
    private SourcesRepository sourcesRepository;
    @Autowired
    private PhotosRepository photosRepository;

    // Index one file
    @Async
    public CompletableFuture<PhotoFile> processFile(String filePath) throws InterruptedException {
        File f = new File(filePath);
        log.debug("Processing file {}", f);
        // calculate md5

        return CompletableFuture.completedFuture(null);
    }

}
