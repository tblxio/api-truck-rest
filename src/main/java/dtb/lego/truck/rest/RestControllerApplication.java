package dtb.lego.truck.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class RestControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestControllerApplication.class, args);
    }

    @Bean("AsyncWebsocketExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(280);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("AsyncWebsocketExecutor");
        executor.initialize();
        return executor;
    }

}

