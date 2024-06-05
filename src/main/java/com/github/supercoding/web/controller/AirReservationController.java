package com.github.supercoding.web.controller;

import com.github.supercoding.service.AirReservationService;
import com.github.supercoding.service.exceptions.InvalidValueException;
import com.github.supercoding.service.exceptions.NotAcceptException;
import com.github.supercoding.service.exceptions.NotFoundException;
import com.github.supercoding.web.dto.airline.ReservationRequest;
import com.github.supercoding.web.dto.airline.ReservationResult;
import com.github.supercoding.web.dto.airline.Ticket;
import com.github.supercoding.web.dto.airline.TicketResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.sql.Not;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/air-reservation")
public class AirReservationController {
    private AirReservationService airReservationService;

    public AirReservationController(AirReservationService airReservationService) {
        this.airReservationService = airReservationService;
    }

    @GetMapping("/tickets")
    public ResponseEntity findAirlineTickets(@RequestParam("user-Id") Integer userId,
                                             @RequestParam("airline-ticket-type") String ticketType){
        try {
            List<Ticket> tickets = airReservationService.findUserFavoritePlaceTickets(userId , ticketType);
            TicketResponse ticketResponse = new TicketResponse(tickets);
            return new ResponseEntity(ticketResponse, HttpStatus.OK);
        }catch (InvalidValueException ive){
            log.error("Client 요청에 문제가 있어 다음처럼 출력 "+ive.getMessage());
            return new ResponseEntity(ive.getMessage(), HttpStatus.BAD_REQUEST);
        }catch(NotFoundException nfe){
            log.error("Client 요청 이후 DB 검색 중 에러로 다음처럼 출력 "+nfe.getMessage());
            return new ResponseEntity(nfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/reservations")
    public ResponseEntity makeReservation(@RequestBody ReservationRequest reservationRequest){
        try {
           ReservationResult reservationResult = airReservationService.makeReservation(reservationRequest);
           return new ResponseEntity(reservationResult, HttpStatus.CREATED);
        }catch (NotFoundException nfe){
            log.error("Client 요청 이후 DB 검색 중 에러로 다음처럼 출력 "+nfe.getMessage());
            return new ResponseEntity(nfe.getMessage(), HttpStatus.NOT_FOUND);
        }catch (NotAcceptException nae){
            log.error("Client 요청이 모종의 이유로 거부됩니다."+nae.getMessage());
            return new ResponseEntity(nae.getMessage(),HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
