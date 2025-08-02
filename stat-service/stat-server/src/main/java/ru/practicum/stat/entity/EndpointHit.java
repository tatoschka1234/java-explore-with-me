package ru.practicum.stat.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String app;

    @Column(length = 1024)
    private String uri;

    @Column(length = 45)
    private String ip;

    private LocalDateTime timestamp;
}
