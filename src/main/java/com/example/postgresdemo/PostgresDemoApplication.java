package com.example.postgresdemo;

import com.example.postgresdemo.scheduled.SourcesMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
public class PostgresDemoApplication {

	private static final Logger log = LoggerFactory.getLogger(PostgresDemoApplication.class);
	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		context = SpringApplication.run(PostgresDemoApplication.class, args);
		log.debug("Initializing sources monitoring");
		SourcesMonitor srcmon = context.getBean(SourcesMonitor.class);
		srcmon.initSourcesMonitoring();
		try {
			log.debug("Starting monitoring thread");
			srcmon.startMonitoringThread();
			log.debug("Monitoring thread running");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Bean
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(4);
		executor.setMaxPoolSize(4);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("fsmonitor-");
		executor.initialize();
		return executor;
	}
}
