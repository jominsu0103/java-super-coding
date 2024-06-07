package com.github.supercoding.web.dto.airline;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.supercoding.repository.airlineTicket.AirlineTicket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Ticket {
    private String depart;
    private String arrival;
    private String departureTime;
    private String returnTime;
    private Integer ticketId;
}
