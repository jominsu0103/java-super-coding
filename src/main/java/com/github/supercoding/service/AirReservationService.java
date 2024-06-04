package com.github.supercoding.service;

import com.github.supercoding.repository.airlineTicket.AirlineTicket;
import com.github.supercoding.repository.airlineTicket.AirlineTicketFlightInfo;
import com.github.supercoding.repository.airlineTicket.AirlineTicketRepository;
import com.github.supercoding.repository.passenger.Passenger;
import com.github.supercoding.repository.passenger.PassengerRepository;
import com.github.supercoding.repository.reservations.Reservation;
import com.github.supercoding.repository.reservations.ReservationRepository;
import com.github.supercoding.repository.users.UserEntity;
import com.github.supercoding.repository.users.UserRepository;
import com.github.supercoding.web.dto.airline.ReservationRequest;
import com.github.supercoding.web.dto.airline.ReservationResult;
import com.github.supercoding.web.dto.airline.Ticket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirReservationService {
    private UserRepository userRepository;
    private AirlineTicketRepository airlineTicketRepository;

    private PassengerRepository passengerRepository;
    private ReservationRepository reservationRepository;

    public AirReservationService(UserRepository userRepository, AirlineTicketRepository airlineTicketRepository, PassengerRepository passengerRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.airlineTicketRepository = airlineTicketRepository;
        this.passengerRepository = passengerRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Ticket> findUserFavoritePlaceTickets(Integer userId, String ticketType) {
        // 필요한 repo : user Repo , airlineTicket Repo
        // 유저를 userId로 가져와서 ,선호하는 여행지 도출
        // 선호하는 여행지와 ticketType으로 airlineTicket table로 질의해서 필요한 AirlineTicket
        // 이 둘의 정보를 조합해서 Ticket Dto를 만든다.
        UserEntity userEntity = userRepository.findUserById(userId);
        String likePlace = userEntity.getLikeTravelPlace();
        List<AirlineTicket> airlineTickets = airlineTicketRepository.findAllAirlineTicketsWithPlaceAndTicketType(likePlace,ticketType);

        List<Ticket> tickets = airlineTickets.stream().map(Ticket::new).collect(Collectors.toList());
        return tickets;
    }
    @Transactional(transactionManager = "tm2")
    public ReservationResult makeReservation(ReservationRequest reservationRequest) {
        // 필요한 repo : Reservation Repo ,  Passenger Repo , join table(flight / airline_ticket

        Integer userId = reservationRequest.getUserId();
        Integer airlineTicketId = reservationRequest.getAirlineTicketId();
        // Passenger
        Passenger passenger = passengerRepository.findPassengerByUserId(userId);
        Integer passengerId = passenger.getPassengerId();

        // price 등의 정보 불러오기

       List<AirlineTicketFlightInfo> airlineTicketFlightInfos = airlineTicketRepository.findAllAirLineTicketAndFlightInfo(airlineTicketId);

       // reservation 생성
        Reservation reservation = new Reservation(passengerId,airlineTicketId);
        Boolean isSuccess = reservationRepository.saveReservation(reservation);

        // TODO: ReservationResult DTO 만들기
        List<Integer> prices= airlineTicketFlightInfos.stream().map(AirlineTicketFlightInfo::getPrice).collect(Collectors.toList());
        List<Integer> charges = airlineTicketFlightInfos.stream().map(AirlineTicketFlightInfo::getCharge).collect(Collectors.toList());
        Integer tax = airlineTicketFlightInfos.stream().map(AirlineTicketFlightInfo::getTax).findFirst().get();
        Integer total_price = airlineTicketFlightInfos.stream().map(AirlineTicketFlightInfo::getTotalPrice).findFirst().get();

        return new ReservationResult(prices,charges,tax,total_price,isSuccess);
    }
}
