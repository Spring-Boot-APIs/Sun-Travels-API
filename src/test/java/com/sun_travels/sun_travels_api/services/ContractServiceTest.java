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
import com.sun_travels.sun_travels_api.repositories.ContractRepository;
import com.sun_travels.sun_travels_api.repositories.HotelRepository;
import com.sun_travels.sun_travels_api.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith( MockitoExtension.class)
class ContractServiceTest {
    @Mock private ContractRepository contractRepository;
    @Mock private HotelRepository hotelRepository;
    @Mock private RoomRepository roomRepository;
    private ContractService contractService;

    @BeforeEach
    void setUp() { contractService = new ContractService(contractRepository, hotelRepository, roomRepository); }

    @Test
    void canAddContract() {
        Hotel hotel = new Hotel(1L, "marino beach", "colombo");
        AddContractRequest request = new AddContractRequest(1L, LocalDate.parse("2021-01-31"), LocalDate.parse("2022-12-31"), Double.parseDouble("12.5"), ContractType.GENERAL, new ArrayList<>(Collections.singletonList(new AddRoomRequest("standard", Double.parseDouble("12.5"), 10, 3))));
        Contract contract = new Contract(hotel, request.getStartDate(), request.getEndDate(), request.getMarkup(), request.getContractType(), ContractStatus.ONGOING);
        Contract savedContract = new Contract(1L, hotel, request.getStartDate(), request.getEndDate(), request.getMarkup(), request.getContractType(), ContractStatus.ONGOING, new ArrayList<>());
        given(hotelRepository.findById(1L)).willReturn(java.util.Optional.of(hotel));
        given(contractRepository.save(contract)).willReturn(savedContract);

        contractService.addContract(request);
        ArgumentCaptor<Contract> contractArgumentCaptor = ArgumentCaptor.forClass(Contract.class);
        verify(contractRepository).save(contractArgumentCaptor.capture());
        Contract capturedContract = contractArgumentCaptor.getValue();
        assertThat(capturedContract).isEqualTo(contract);
    }

    @Test
    void willThrowWhenInvalidHotelId() {
        AddContractRequest request = new AddContractRequest(1L, LocalDate.parse("2021-01-31"), LocalDate.parse("2022-12-31"), Double.parseDouble("12.5"), ContractType.GENERAL, new ArrayList<>(Collections.singletonList(new AddRoomRequest("standard", Double.parseDouble("12.5"), 10, 3))));

        assertThatThrownBy(() -> contractService.addContract(request))
                .isInstanceOfAny( InvalidHotelIdException.class)
                .hasMessageContaining("Invalid hotel ID");
        verify(contractRepository, never()).save(any(Contract.class));
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void willThrowWhenContractOverlaps() {
        Hotel hotel = new Hotel(1L, "marino beach", "colombo");
        AddContractRequest request = new AddContractRequest(1L, LocalDate.parse("2021-01-31"), LocalDate.parse("2022-12-31"), Double.parseDouble("12.5"), ContractType.GENERAL, new ArrayList<>(Collections.singletonList(new AddRoomRequest("standard", Double.parseDouble("12.5"), 10, 3))));
        given(hotelRepository.findById(1L)).willReturn(java.util.Optional.of(hotel));
        given(contractRepository.findSimilarGeneralContractId(hotel.getHotelId(), "standard", request.getStartDate(), request.getEndDate())).willReturn(java.util.Optional.of(1L));

        assertThatThrownBy(() -> contractService.addContract(request))
                .isInstanceOfAny(ContractOverlapsException.class)
                .hasMessageContaining("Marino Beach hotel in Colombo has an existing GENERAL contract (ID = 1) with the " +
                                              "Standard room type which overlaps with the given time period");
        verify(contractRepository, never()).save(any(Contract.class));
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void canGetContracts() {
        contractService.getContracts();
        verify(contractRepository).findAllNotDeletedContracts(ContractStatus.DELETED);
    }

    @Test
    void canDeleteContract() {
        given(contractRepository.existsById(anyLong())).willReturn(true);
        contractService.deleteContract(anyLong());
        verify(contractRepository).updateStatusToDeleted(anyLong(), any(LocalDate.class));
    }

    @Test
    void willThrowWhenInvalidContractId() {
        given(contractRepository.existsById(anyLong())).willReturn(false);
        assertThatThrownBy(() -> contractService.deleteContract(1L))
                .isInstanceOfAny( InvalidContractIdException.class)
                .hasMessageContaining("Invalid contract ID");
        verify(contractRepository, never()).updateStatusToDeleted(anyLong(), any(LocalDate.class));
    }

    @Test
    void canUpdateContractStatus() {
        given(contractRepository.findNotStartedDue(LocalDate.now()))
                .willReturn(new ArrayList<>(Collections.singletonList(1L)));
        given(contractRepository.findOngoingDue(LocalDate.now()))
                .willReturn(new ArrayList<>(Collections.singletonList(1L)));
        contractService.updateContractStatus();
        verify(contractRepository).findNotStartedDue(any(LocalDate.class));
        verify(contractRepository).updateNotStartedToOngoing(anyLong());
        verify(contractRepository).findOngoingDue(any(LocalDate.class));
        verify(contractRepository).updateOngoingToExpired(anyLong());
    }

    @Test
    void canDropDeletedContracts() {
        given(contractRepository.findDeletedDue(LocalDate.now()))
                .willReturn(new ArrayList<>(Collections.singletonList(1L)));
        contractService.dropDeletedContracts();
        verify(roomRepository).deleteRoomsByContractId(anyLong());
        verify(contractRepository).deleteContractByContractId(anyLong());
    }
}