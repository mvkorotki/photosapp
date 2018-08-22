package com.example.postgresdemo.scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.postgresdemo.repository.SourcesRepository;
import com.example.postgresdemo.source.SourceProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SourcesMonitor {

    @Autowired
    private SourcesRepository sourcesRepository;

    private static final Logger log = LoggerFactory.getLogger(SourcesMonitor.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 10000)
    public void reportCurrentTime() {
        sourcesRepository.findAll().forEach(source -> {
            log.info("Reading files in source {}", source.getPath());
            Thread t = new Thread(new SourceProcessor(source));
            t.run();
            try {
                t.join();
            } catch (InterruptedException e) {
                log.warn("Reading source {} interrupted", source.getPath());
            }
            log.info("Timer end");
        });
    }
}
