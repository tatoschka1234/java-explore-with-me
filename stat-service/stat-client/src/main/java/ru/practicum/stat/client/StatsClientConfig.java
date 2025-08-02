package ru.practicum.stat.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatsClientConfig {

    @Value("${stat.server.url}")
    private String serverUrl;

    @Bean
    public StatsClient statsClient() {
        return new StatsClient(serverUrl);
    }
}
