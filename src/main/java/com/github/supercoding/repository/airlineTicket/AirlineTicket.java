package com.github.supercoding.repository.airlineTicket;

import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Objects;
@Setter
@Getter
@EqualsAndHashCode(of = "ticketId")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AirlineTicket {
    private Integer ticketId;
    private String ticketType;
    private String departureLocation;
    private String arrivalLocation;
    private LocalDateTime departureAt;
    private LocalDateTime returnAt;
    private Double tax;
    private Double totalPrice;

    @Builder
    public AirlineTicket(Integer ticketId, String ticketType, String departureLocation, String arrivalLocation, Date departureAt, Date returnAt, Double tax, Double totalPrice) {
        this.ticketId = ticketId;
        this.ticketType = ticketType;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.departureAt = (departureAt != null) ? departureAt.toLocalDate().atStartOfDay() : null;
        this.returnAt =(returnAt != null) ? returnAt.toLocalDate().atStartOfDay() : null;
        this.tax = tax;
        this.totalPrice = totalPrice;
    }
}
