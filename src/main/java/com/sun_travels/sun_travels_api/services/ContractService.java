package com.sun_travels.sun_travels_api.services;

import com.sun_travels.sun_travels_api.dtos.AddContractRequest;
import com.sun_travels.sun_travels_api.dtos.AddRoomRequest;
import com.sun_travels.sun_travels_api.enums.ContractStatus;
import com.sun_travels.sun_travels_api.enums.ContractType;
import com.sun_travels.sun_travels_api.exceptions.ContractOverlapsException;
import com.sun_travels.sun_travels_api.exceptions.InvalidContractIdException;
import com.sun_travels.sun_travels_api.exceptions.InvalidHotelIdException;
import com.sun_travels.sun_travels_api.models.Contract;
import com.sun_travels.sun_travels_api.models.Hotel;
import com.sun_travels.sun_travels_api.models.Room;
import com.sun_travels.sun_travels_api.models.RoomPK;
import com.sun_travels.sun_travels_api.repositories.ContractRepository;
import com.sun_travels.sun_travels_api.repositories.HotelRepository;
import com.sun_travels.sun_travels_api.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {
    private final ContractRepository contractRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public ContractService( ContractRepository contractRepository, HotelRepository hotelRepository, RoomRepository roomRepository ) {
        this.contractRepository = contractRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    private Hotel validateHotelId( Long hotelId ) {
        Optional<Hotel> hotelOptional = hotelRepository.findById( hotelId );
        if (!hotelOptional.isPresent()) {   // Throw if hotel with the given id doesn't exist
            throw new InvalidHotelIdException();
        }
        return hotelOptional.get();
    }

    private void validateRoomType( Hotel hotel, String roomType, LocalDate startDate, LocalDate endDate, ContractType type ) {
        Optional<Long> contractOptional;
        if (type.equals(ContractType.GENERAL)) {
            contractOptional = contractRepository.findSimilarGeneralContractId( hotel.getHotelId(), roomType, startDate, endDate );
        } else {
            contractOptional = contractRepository.findSimilarSeasonalContractId( hotel.getHotelId(), roomType, startDate, endDate );
        }
        if(contractOptional.isPresent()) {     // Throw if the room type of the same hotel exists with overlapping periods
            throw new ContractOverlapsException(hotel, contractOptional.get(), roomType, type);
        }
    }

    /**
     * Get the contract status depending on the start and end dates
     * @param request Contract details
     * @return Contract Status
     */
    private ContractStatus decideContractStatus( AddContractRequest request ) {
        LocalDate today = LocalDate.now();
        if (request.getStartDate().compareTo(today) > 0) {
            return ContractStatus.NOT_STARTED;
        } else if (request.getEndDate().compareTo(today) < 0) {
            return ContractStatus.EXPIRED;
        } else {
            return ContractStatus.ONGOING;
        }
    }

    @Transactional
    public Contract addContract( AddContractRequest request ) {
        Hotel hotel = validateHotelId( request.getHotelId() );
        for( AddRoomRequest room: request.getRooms() ) {
            room.setRoomType( room.getRoomType().trim().toLowerCase() );
            validateRoomType( hotel, room.getRoomType(), request.getStartDate(), request.getEndDate(), request.getContractType() );
        }
        ContractStatus status = decideContractStatus( request );
        Contract contract = contractRepository.save(new Contract(hotel, request.getStartDate(), request.getEndDate(), request.getMarkup(), request.getContractType(), status));
        for( AddRoomRequest room: request.getRooms() ) {    // Add room details to the database
            roomRepository.save(new Room(new RoomPK(contract.getContractId(), room.getRoomType()), room.getPrice(), room.getNoOfRooms(), room.getMaxAdults()) );
        }
        return contract;
    }

    public List<Contract> getContracts() { return contractRepository.findAllNotDeletedContracts(ContractStatus.DELETED); }

    @Transactional
    public void deleteContract( Long contractId ) {
        if (!contractRepository.existsById(contractId)) { throw new InvalidContractIdException(); }
        contractRepository.updateStatusToDeleted(contractId, LocalDate.now());
    }

    /**
     * Contract status is updated depending on the contract validity period and the current status
     */
    @Transactional
    public void updateContractStatus() {
        LocalDate currentDate = LocalDate.now();
        for( Long id: contractRepository.findNotStartedDue( currentDate ) )  {
            contractRepository.updateNotStartedToOngoing( id );
        }
        for( Long id: contractRepository.findOngoingDue( currentDate ))  {
            contractRepository.updateOngoingToExpired( id );
        }
    }

    @Transactional
    public void dropDeletedContracts() {
        for( Long id: contractRepository.findDeletedDue( LocalDate.now() ))  {
            roomRepository.deleteRoomsByContractId( id );   // Drop all the rooms with the given contract id
            contractRepository.deleteContractByContractId( id );    // Drop contract with the given contract id
        }
    }
}