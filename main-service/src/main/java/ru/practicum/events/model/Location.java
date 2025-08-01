package ru.practicum.events.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @Column(name = "loc_lat", nullable = false) private Double lat;
    @Column(name = "loc_lon", nullable = false) private Double lon;
}