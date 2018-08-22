package com.example.postgresdemo.source;

import com.example.postgresdemo.model.PhotoFileSource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SourceProcessor implements Runnable {

    private static Logger log = LoggerFactory.getLogger(SourceProcessor.class);
    private PhotoFileSource source;

    public SourceProcessor(PhotoFileSource source) {
        this.source = source;
    }

    @Override
    public void run() {
        try (Stream<Path> paths = Files.walk(Paths.get(source.getPath()))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(file -> log.debug(file.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PhotoFileSource getSource() {
        return source;
    }

    public void setSource(PhotoFileSource source) {
        this.source = source;
    }
}
