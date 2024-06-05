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
import com.github.supercoding.service.exceptions.InvalidValueException;
import com.github.supercoding.service.exceptions.NotAcceptException;
import com.github.supercoding.service.exceptions.NotFoundException;
import com.github.supercoding.service.mapper.TicketMapper;
import com.github.supercoding.web.dto.airline.ReservationRequest;
import com.github.supercoding.web.dto.airline.ReservationResult;
import com.github.supercoding.web.dto.airline.Ticket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
        Set<String> ticketTypeSet = new HashSet<>(Arrays.asList("편도" , "왕복"));

        if(!ticketTypeSet.contains(ticketType)){
            throw new InvalidValueException("해당 TicketType "+ticketType + "은 지원하지 않습니다.");
        }
        UserEntity userEntity = userRepository.findUserById(userId).orElseThrow(()->new NotFoundException("해당 유저를 찾을 수 없습니다. 해당 ID : "+ userId));
        String likePlace = userEntity.getLikeTravelPlace();

        List<AirlineTicket> airlineTickets = airlineTicketRepository.findAllAirlineTicketsWithPlaceAndTicketType(likePlace,ticketType);

        if (airlineTickets.isEmpty()){
            throw new NotFoundException("해당 likePlace: "+likePlace+"와 TicketType: "+ ticketType + "에 해당하는 항공권 찾을 수 없습니다.");
        }

        List<Ticket> tickets = airlineTickets.stream().map(TicketMapper.INSTANCE::airlineTicketToTicket).collect(Collectors.toList());
        return tickets;
    }
    @Transactional(transactionManager = "tm2")
    public ReservationResult makeReservation(ReservationRequest reservationRequest) {
        // 필요한 repo : Reservation Repo ,  Passenger Repo , join table(flight / airline_ticket

        Integer userId = reservationRequest.getUserId();
        Integer airlineTicketId = reservationRequest.getAirlineTicketId();
        // Passenger
        Passenger passenger = passengerRepository.findPassengerByUserId(userId).orElseThrow(()-> new NotFoundException("요청하신 유저 아이디 : "+userId+"에 해당하는 Passenger를 찾을 수 없습니다."));
        Integer passengerId = passenger.getPassengerId();

        // price 등의 정보 불러오기

       List<AirlineTicketFlightInfo> airlineTicketFlightInfos = airlineTicketRepository.findAllAirLineTicketAndFlightInfo(airlineTicketId);
        if (airlineTicketFlightInfos.isEmpty()){
            throw new NotFoundException("AirlineTicket Id "+airlineTicketId+" 에 해당하는 항공편과 항공권을 찾을 수 없습니다.");
        }
        Boolean isSuccess = false;
       // reservation 생성
        Reservation reservation = new Reservation(passengerId,airlineTicketId);
        try {
            isSuccess = reservationRepository.saveReservation(reservation);
        }catch (RuntimeException e){
            throw new NotAcceptException("Reservation이 등록되는 과정이 거부되었습니다.");
        }

        // ReservationResult DTO 만들기
        List<Integer> prices= airlineTicketFlightInfos.stream().map(AirlineTicketFlightInfo::getPrice).collect(Collectors.toList());
        List<Integer> charges = airlineTicketFlightInfos.stream().map(AirlineTicketFlightInfo::getCharge).collect(Collectors.toList());
        Integer tax = airlineTicketFlightInfos.stream().map(AirlineTicketFlightInfo::getTax).findFirst().get();
        Integer total_price = airlineTicketFlightInfos.stream().map(AirlineTicketFlightInfo::getTotalPrice).findFirst().get();

        return new ReservationResult(prices,charges,tax,total_price,isSuccess);
    }
}
