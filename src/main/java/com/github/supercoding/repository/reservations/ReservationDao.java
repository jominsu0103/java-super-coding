package com.github.supercoding.repository.reservations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;

@Repository
public class ReservationDao implements ReservationRepository{
    private JdbcTemplate template;

    public ReservationDao(@Qualifier("jdbcTemplate2") JdbcTemplate jdbcTemplate) {
        this.template = jdbcTemplate;
    }
    @Override
    public Boolean saveReservation(Reservation reservation) {
        Integer rowNums = template.update("INSERT INTO reservation(passenger_id , airline_ticket_id , reservation_status, reserve_at) " +
                        "VALUES (?,?,?,?)", reservation.getReservationId(), reservation.getAirlineTicketId(), reservation.getReservationStatus(),
                new Date(Timestamp.valueOf(reservation.getReserveAt()).getTime()));

        return rowNums > 0;
    }
}
