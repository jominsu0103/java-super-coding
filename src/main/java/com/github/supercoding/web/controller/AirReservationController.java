package com.github.supercoding.web.controller;

import com.github.supercoding.service.AirReservationService;
import com.github.supercoding.service.exceptions.InvalidValueException;
import com.github.supercoding.service.exceptions.NotAcceptException;
import com.github.supercoding.service.exceptions.NotFoundException;
import com.github.supercoding.web.dto.airline.ReservationRequest;
import com.github.supercoding.web.dto.airline.ReservationResult;
import com.github.supercoding.web.dto.airline.Ticket;
import com.github.supercoding.web.dto.airline.TicketResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.sql.Not;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/air-reservation")
@RequiredArgsConstructor
public class AirReservationController {
    private AirReservationService airReservationService;

    @GetMapping("/tickets")
    public TicketResponse findAirlineTickets(@RequestParam("user-Id") Integer userId,
                                             @RequestParam("airline-ticket-type") String ticketType){
            List<Ticket> tickets = airReservationService.findUserFavoritePlaceTickets(userId , ticketType);
            return new TicketResponse(tickets);
    }
    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResult makeReservation(@RequestBody ReservationRequest reservationRequest){
        return airReservationService.makeReservation(reservationRequest);
    }
    @GetMapping("/users-sum-price")
    public Double findUserFlightSumPrice(@RequestParam("user-id") Integer userId) {
        Double sum = airReservationService.findUserFlightSumPrice(userId);
        return sum;
    }
}
