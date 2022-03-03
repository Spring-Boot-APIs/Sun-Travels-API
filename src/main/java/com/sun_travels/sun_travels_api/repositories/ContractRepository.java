package com.sun_travels.sun_travels_api.repositories;

import com.sun_travels.sun_travels_api.dtos.SearchAvailabilityQuery;
import com.sun_travels.sun_travels_api.enums.ContractStatus;
import com.sun_travels.sun_travels_api.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    @Query("SELECT c FROM Contract c WHERE c.status != ?1 ORDER BY c.status DESC")
    List<Contract> findAllNotDeletedContracts( ContractStatus status );

    /**
     * Return an optional contractId if there exists a GENERAL contract with the same hotel and room type with overlapping period as the given values
     */
    @Query(value = "SELECT contract_id FROM hotel NATURAL JOIN contract NATURAL JOIN room " +
                           "WHERE hotel_id = :hotelId AND room_type = :roomType AND " +
                           "((Date(start_date) >= :startDate AND Date(start_date) <= :endDate) OR " +
                           "(Date(start_date) <= :startDate AND Date(end_date) >= :endDate) OR " +
                           "(Date(end_date) >= :startDate AND Date(end_date) <= :endDate) OR " +
                           "(Date(start_date) >= :startDate AND Date(end_date) <= :endDate)) AND " +
                           "status != 'DELETED' AND contract_type = 'GENERAL'",
            nativeQuery = true)
    Optional<Long> findSimilarGeneralContractId(
            @Param("hotelId") Long hotelId,
            @Param("roomType") String roomType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * Return an optional contractId if there exists a SEASONAL contract with the same hotel and room type with overlapping period as the given values
     */
    @Query(value = "SELECT contract_id FROM hotel NATURAL JOIN contract NATURAL JOIN room " +
                           "WHERE hotel_id = :hotelId AND room_type = :roomType AND " +
                           "((Date(start_date) >= :startDate AND Date(start_date) <= :endDate) OR " +
                           "(Date(start_date) <= :startDate AND Date(end_date) >= :endDate) OR " +
                           "(Date(end_date) >= :startDate AND Date(end_date) <= :endDate) OR " +
                           "(Date(start_date) >= :startDate AND Date(end_date) <= :endDate)) AND " +
                           "status != 'DELETED' AND contract_type = 'SEASONAL'",
            nativeQuery = true)
    Optional<Long> findSimilarSeasonalContractId(
            @Param("hotelId") Long hotelId,
            @Param("roomType") String roomType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * Returns available rooms according to the given values
     */
    @Query(value = "SELECT hotel_id AS hotelId, hotel_name AS hotelName, city as city, " +
        "contract_type AS contractType, markup AS markup, " +
        "room_type AS roomType, price AS price " +
        "FROM hotel NATURAL JOIN contract NATURAL JOIN room " +
        "WHERE city = :city AND Date(start_date) < :checkInDate AND Date(end_date) > :checkOutDate AND status != 'DELETED' AND no_of_rooms >= :roomCount AND max_adults >= :maxNoOfAdults ORDER BY hotel_id",
        nativeQuery = true)
   List<SearchAvailabilityQuery> findAvailableRooms(
            @Param("city") String city,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("roomCount") int roomCount,
            @Param("maxNoOfAdults") int maxNoOfAdults
    );

    /**
     * Updates contract status to DELETED and the end date to the current date of the given contracts
     */
    @Modifying
    @Query(value = "UPDATE contract SET status = 'DELETED', end_date = :currentDate WHERE contract_id = :contractId", nativeQuery = true)
    void updateStatusToDeleted( @Param("contractId") Long contractId, @Param("currentDate") LocalDate currentDate );

    /**
     * Returns ids of the NOT_STARTED contracts where start_date is equal to the current date
     */
    @Query(value = "SELECT contract_id FROM contract " +
                           "WHERE Date(start_date) = :currentDate AND status = 'NOT_STARTED'", nativeQuery = true)
    List<Long> findNotStartedDue( @Param("currentDate") LocalDate currentDate );

    /**
     * Updates contract status to ONGOING of the given contracts
     */
    @Modifying
    @Query(value = "UPDATE contract SET status = 'ONGOING' WHERE contract_id = :contractId", nativeQuery = true)
    void updateNotStartedToOngoing( @Param("contractId") Long contractId );

    /**
     * Returns ids of the ONGOING contracts where end_date is equal to the current date
     */
    @Query(value = "SELECT contract_id FROM contract " +
                           "WHERE Date(end_date) = :currentDate AND status = 'ONGOING'", nativeQuery = true)
    List<Long> findOngoingDue( @Param("currentDate") LocalDate currentDate );

    /**
     * Updates contract status to EXPIRED of the given contracts
     */
    @Modifying
    @Query(value = "UPDATE contract SET status = 'EXPIRED' WHERE contract_id =: contractId", nativeQuery = true)
    void updateOngoingToExpired(@Param("contractId") Long contractId );

    /**
     * Returns ids of the DELETED contracts which are deleted 3 months ago
     */
    @Query(value = "SELECT contract_id FROM contract " +
                           "WHERE status = 'DELETED' AND DATE_ADD(Date(end_date), INTERVAL 3 MONTH) = :currentDate", nativeQuery = true)
    List<Long> findDeletedDue( @Param("currentDate") LocalDate currentDate );

    /**
     * Drop the contracts from the "contract" table where the contractId equals to the given id
     * @param contractId Id of the contract
     */
    @Modifying
    @Query(value = "DELETE FROM contract WHERE contract_id = :contractId", nativeQuery = true)
    void deleteContractByContractId( @Param("contractId") Long contractId );
}