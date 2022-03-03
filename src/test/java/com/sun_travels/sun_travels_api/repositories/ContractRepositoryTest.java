package com.sun_travels.sun_travels_api.repositories;

import com.sun_travels.sun_travels_api.dtos.SearchAvailabilityQuery;
import com.sun_travels.sun_travels_api.enums.ContractStatus;
import com.sun_travels.sun_travels_api.enums.ContractType;
import com.sun_travels.sun_travels_api.models.Contract;
import com.sun_travels.sun_travels_api.models.Hotel;
import com.sun_travels.sun_travels_api.models.Room;
import com.sun_travels.sun_travels_api.models.RoomPK;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ContractRepositoryTest {
    @Autowired private HotelRepository hotelRepository;
    @Autowired private ContractRepository contractRepository;
    @Autowired private RoomRepository roomRepository;
    private Hotel savedHotel;

    @BeforeEach
    void setUp() {
        Hotel hotel = new Hotel("marino beach", "colombo");
        savedHotel = hotelRepository.save(hotel);
    }

    @AfterEach
    void tearDown() { roomRepository.deleteAll(); contractRepository.deleteAll(); hotelRepository.deleteAll(); }

    @Test
    void canFindAllNotDeletedContracts() {
        Contract contract = new Contract(savedHotel, LocalDate.parse("2021-01-31"), LocalDate.parse("2022-12-31"), Double.parseDouble("12.5"), ContractType.GENERAL, ContractStatus.ONGOING);
        contractRepository.save(contract);
        List<Contract> contracts = contractRepository.findAllNotDeletedContracts(ContractStatus.DELETED);
        assertFalse(contracts.isEmpty());
    }

    @Test
    @Disabled("Since 'DATE' function is used this is tested against the local mySQL database")
    void canFindSimilarGeneralContractId() {
        Contract contract = new Contract(savedHotel, LocalDate.parse("2021-01-31"), LocalDate.parse("2022-12-31"), Double.parseDouble("12.5"), ContractType.GENERAL, ContractStatus.ONGOING);
        Contract savedContract = contractRepository.save(contract);
        roomRepository.save(new Room(new RoomPK(savedContract.getContractId(), "standard"), 5000, 10, 3));
        Optional<Long> optionalId = contractRepository.findSimilarGeneralContractId(savedHotel.getHotelId(), "standard", LocalDate.parse("2022-04-01"), LocalDate.parse("2022-04-30"));
        assertThat(optionalId).isPresent();
    }

    @Test
    @Disabled("Since 'DATE' function is used, this is tested against the local mySQL database")
    void canFindSimilarSeasonalContractId() {
        Contract contract = new Contract(savedHotel, LocalDate.parse("2021-01-31"), LocalDate.parse("2022-12-31"), Double.parseDouble("12.5"), ContractType.SEASONAL, ContractStatus.ONGOING);
        Contract savedContract = contractRepository.save(contract);
        roomRepository.save(new Room(new RoomPK(savedContract.getContractId(), "standard"), 5000, 10, 3));
        Optional<Long> optionalId = contractRepository.findSimilarSeasonalContractId(savedHotel.getHotelId(), "standard", LocalDate.parse("2022-04-01"), LocalDate.parse("2022-04-30"));
        assertThat(optionalId).isPresent();
    }

    @Test
    @Disabled("Since 'DATE' function is used, this is tested against the local mySQL database")
    void canFindAvailableRooms() {
        Contract contract = new Contract(savedHotel, LocalDate.parse("2021-01-31"), LocalDate.parse("2022-12-31"), Double.parseDouble("12.5"), ContractType.GENERAL, ContractStatus.ONGOING);
        Contract savedContract = contractRepository.save(contract);
        roomRepository.save(new Room(new RoomPK(savedContract.getContractId(), "standard"), 5000, 10, 3));
        List<SearchAvailabilityQuery> options = contractRepository.findAvailableRooms(savedHotel.getCity(), LocalDate.parse("2022-04-01"), LocalDate.parse("2022-04-03"), 2, 2);
        assertFalse(options.isEmpty());
    }

    @Test
    @Disabled("Since 'DATE' function is used, this is tested against the local mySQL database")
    void canFindNotStartedDue() {
        Contract contract = new Contract(savedHotel, LocalDate.now(), LocalDate.now().plusMonths(3), Double.parseDouble("12.5"), ContractType.GENERAL, ContractStatus.NOT_STARTED);
        Contract savedContract = contractRepository.save(contract);
        roomRepository.save(new Room(new RoomPK(savedContract.getContractId(), "standard"), 5000, 10, 3));
        List<Long> contractIds = contractRepository.findNotStartedDue(LocalDate.now());
        assertFalse(contractIds.isEmpty());
    }

    @Test
    @Disabled("Since 'DATE' function is used, this is tested against the local mySQL database")
    void canFindOngoingDue() {
        Contract contract = new Contract(savedHotel, LocalDate.now().minusMonths(3), LocalDate.now(), Double.parseDouble("12.5"), ContractType.GENERAL, ContractStatus.ONGOING);
        Contract savedContract = contractRepository.save(contract);
        roomRepository.save(new Room(new RoomPK(savedContract.getContractId(), "standard"), 5000, 10, 3));
        List<Long> contractIds = contractRepository.findOngoingDue(LocalDate.now());
        assertFalse(contractIds.isEmpty());
    }

    @Test
    @Disabled("Since 'DATE' function is used, this is tested against the local mySQL database")
    void canFindDeletedDue() {
        Contract contract = new Contract(savedHotel, LocalDate.now().minusMonths(6), LocalDate.now().minusMonths(3), Double.parseDouble("12.5"), ContractType.GENERAL, ContractStatus.DELETED);
        Contract savedContract = contractRepository.save(contract);
        roomRepository.save(new Room(new RoomPK(savedContract.getContractId(), "standard"), 5000, 10, 3));
        List<Long> contractIds = contractRepository.findOngoingDue(LocalDate.now());
        assertFalse(contractIds.isEmpty());
    }

    @Test
    void canDeleteContractByContractId() {
        Contract contract = new Contract(savedHotel, LocalDate.now().minusMonths(6), LocalDate.now().minusMonths(3), Double.parseDouble("12.5"), ContractType.GENERAL, ContractStatus.DELETED);
        Contract savedContract = contractRepository.save(contract);
        Room savedRoom = roomRepository.save(new Room(new RoomPK(savedContract.getContractId(), "standard"), 5000, 10, 3));
        roomRepository.deleteRoomsByContractId(savedRoom.getRoomPK().getContractId());
        contractRepository.deleteContractByContractId(savedContract.getContractId());
        assertFalse(contractRepository.existsById(savedContract.getContractId()));
    }
}