package com.sun_travels.sun_travels_api.repositories;

import com.sun_travels.sun_travels_api.enums.ContractStatus;
import com.sun_travels.sun_travels_api.enums.ContractType;
import com.sun_travels.sun_travels_api.models.Contract;
import com.sun_travels.sun_travels_api.models.Hotel;
import com.sun_travels.sun_travels_api.models.Room;
import com.sun_travels.sun_travels_api.models.RoomPK;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class RoomRepositoryTest {
    @Autowired private RoomRepository roomRepository;
    @Autowired private ContractRepository contractRepository;
    @Autowired private HotelRepository hotelRepository;
    private Room savedRoom;

    @BeforeEach
    void setUp() {
        Hotel hotel = new Hotel("marino beach", "colombo");
        Hotel savedHotel = hotelRepository.save(hotel);
        Contract contract = new Contract(savedHotel, LocalDate.parse("2021-01-31"), LocalDate.parse("2022-12-31"), Double.parseDouble("12.5"), ContractType.GENERAL, ContractStatus.ONGOING);
        Contract savedContract = contractRepository.save(contract);
        savedRoom = new Room(new RoomPK(savedContract.getContractId(), "standard"), 5000, 10, 3);
    }

    @AfterEach
    void tearDown() { roomRepository.deleteAll(); }

    @Test
    void canDeleteRoomsByContractId() {
        roomRepository.save( savedRoom );
        roomRepository.deleteRoomsByContractId( savedRoom.getRoomPK().getContractId());
        assertFalse(roomRepository.existsById(new RoomPK( savedRoom.getRoomPK().getContractId(), savedRoom.getRoomPK().getRoomType())));
    }
}