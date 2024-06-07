package com.github.supercoding.repository.airlineTicket;

import com.github.supercoding.repository.flight.Flight;
import jakarta.persistence.*;
import lombok.*;



import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode(of = "ticketId")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "airline_ticket")
public class AirlineTicket {
    @Id @Column(name = "ticket_id")
    private Integer ticketId;
    @Column(name = "ticket_type", length = 5, columnDefinition = "CHECK (ticket_type in ('편도', '왕복')) ")
    private String ticketType;
    @Column(name = "departure_loc", length = 20)
    private String departureLocation;
    @Column(name = "arrival_loc", length = 20)
    private String arrivalLocation;
    @Column(name = "departure_at", nullable = false)
    private LocalDateTime departureAt;
    @Column(name = "return_at", nullable = false)
    private LocalDateTime returnAt;
    @Column(name = "tax")
    private Double tax;
    @Column(name = "total_price")
    private Double totalPrice;
    @OneToMany(mappedBy = "airlineTicket")
    private List<Flight> flightList;

}
