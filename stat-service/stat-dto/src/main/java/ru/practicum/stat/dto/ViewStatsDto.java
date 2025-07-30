package ru.practicum.stat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ViewStatsDto {
    private String app;
    private String uri;
    private long hits;
}