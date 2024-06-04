package com.github.supercoding.service;

import com.github.supercoding.repository.airlineTicket.AirlineTicket;
import com.github.supercoding.repository.airlineTicket.AirlineTicketRepository;
import com.github.supercoding.repository.users.UserEntity;
import com.github.supercoding.repository.users.UserRepository;
import com.github.supercoding.web.dto.airline.Ticket;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirReservationService {
    private UserRepository userRepository;
    private AirlineTicketRepository airlineTicketRepository;

    public AirReservationService(UserRepository userRepository, AirlineTicketRepository airlineTicketRepository) {
        this.userRepository = userRepository;
        this.airlineTicketRepository = airlineTicketRepository;
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
}
