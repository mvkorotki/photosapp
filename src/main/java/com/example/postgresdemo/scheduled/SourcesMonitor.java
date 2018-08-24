package com.example.postgresdemo.scheduled;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.postgresdemo.model.PhotoFileSource;
import com.example.postgresdemo.repository.PhotosRepository;
import com.example.postgresdemo.repository.SourcesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

@Service
public class SourcesMonitor {

    @Autowired
    private SourcesRepository sourcesRepository;
    @Autowired
    private PhotosRepository photosRepository;
    @Autowired
    private PhotosProcessor photosProcessor;

    private static final Logger log = LoggerFactory.getLogger(SourcesMonitor.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private WatchService watcher = FileSystems.getDefault().newWatchService();

    public SourcesMonitor() throws IOException {
    }

    public void initSourcesMonitoring() {
        sourcesRepository.findAll().forEach(this::addMonitoredSource);
    }

    public void addMonitoredSource(PhotoFileSource source) {
        Path dir = Paths.get(source.getPath());
        log.debug("Adding startMonitoringThread key for source {}", source.getPath());
        watchDir(dir);
    }

    @Scheduled(fixedRate = 1000)
    public void startMonitoringThread() throws InterruptedException {
        WatchKey key;
        while ((key = watcher.take()) != null) {
            log.debug("Processing key {}", key);
            for (WatchEvent<?> event : key.pollEvents()) {

                WatchEvent.Kind<?> kind = event.kind();
                if (kind == OVERFLOW) {
                    break;
                }
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path dir = (Path) key.watchable();
                Path filename = dir.resolve(ev.context());

                if (Files.isDirectory(filename)) {

                    if(kind == ENTRY_CREATE) {
                        log.debug("New directory detected, adding watcher: {}, {}", filename, kind);
                        watchDir(filename);
                    }

                } else {
                    log.debug("New file detected: {}, {}", filename, kind);
                    try {
                        Path child = dir.resolve(filename);
                        if (Files.probeContentType(child).equals("image/jpeg")) {
                            photosProcessor.processFile(filename.toString());
                        }
                    } catch (IOException e) {
                        log.error(e.getLocalizedMessage());
                    }
                }
            }
            key.reset();
        }
    }

    private void watchDir(Path dir) {
        try {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    dir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }
}
