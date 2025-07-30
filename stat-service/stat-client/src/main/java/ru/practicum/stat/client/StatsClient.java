package ru.practicum.stat.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
public class StatsClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String serverUrl;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void saveHit(EndpointHitDto hitDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EndpointHitDto> request = new HttpEntity<>(hitDto, headers);

        restTemplate.postForEntity(serverUrl + "/hit", request, Void.class);
    }

    public List<ViewStatsDto> getStats(LocalDateTime start,
                                       LocalDateTime end,
                                       List<String> uris,
                                       boolean unique) {
        String startEncoded = URLEncoder.encode(start.format(FORMATTER), StandardCharsets.UTF_8);
        String endEncoded = URLEncoder.encode(end.format(FORMATTER), StandardCharsets.UTF_8);

        StringBuilder urlBuilder = new StringBuilder(serverUrl + "/stats");
        urlBuilder.append("?start=").append(startEncoded)
                .append("&end=").append(endEncoded)
                .append("&unique=").append(unique);

        if (uris != null && !uris.isEmpty()) {
            for (String uri : uris) {
                urlBuilder.append("&uris=").append(URLEncoder.encode(uri, StandardCharsets.UTF_8));
            }
        }

        URI uri = URI.create(urlBuilder.toString());
        ResponseEntity<ViewStatsDto[]> response = restTemplate.getForEntity(uri, ViewStatsDto[].class);
        return response.getBody() == null ? Collections.emptyList() : List.of(response.getBody());
    }
}
