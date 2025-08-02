package ru.practicum.events.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.categories.model.Category;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String annotation;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(length = 7000)
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    private boolean paid;

    private int participantLimit;

    @Column(length = 120)
    private String title;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private int confirmedRequests;

    private long views;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Embedded
    private Location location;
}
