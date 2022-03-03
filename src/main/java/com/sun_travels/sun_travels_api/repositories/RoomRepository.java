package com.sun_travels.sun_travels_api.repositories;

import com.sun_travels.sun_travels_api.models.Room;
import com.sun_travels.sun_travels_api.models.RoomPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room,RoomPK> {

    /**
     * Drop the rooms from the "room" table where the contractId equals to the given id
     * @param contractId Id of the contract where the rooms belong to
     */
    @Modifying
    @Query(value = "DELETE FROM room WHERE contract_id = :contractId", nativeQuery = true)
    void deleteRoomsByContractId( @Param("contractId") Long contractId );
}
