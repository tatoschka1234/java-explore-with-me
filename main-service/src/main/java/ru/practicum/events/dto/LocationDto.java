package ru.practicum.events.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    @NotNull
    @DecimalMin("-90.0")  @DecimalMax("90.0")   private Double lat;
    @NotNull @DecimalMin("-180.0") @DecimalMax("180.0")  private Double lon;
}

