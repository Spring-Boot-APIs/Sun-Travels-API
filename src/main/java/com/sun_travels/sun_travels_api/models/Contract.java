package com.sun_travels.sun_travels_api.models;

import com.sun_travels.sun_travels_api.Helper;
import com.sun_travels.sun_travels_api.enums.ContractStatus;
import com.sun_travels.sun_travels_api.enums.ContractType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "contract")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Contract implements Serializable {
    @Id
    @SequenceGenerator( name = "contract_sequence", sequenceName = "contract_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_sequence")
    @Column(name = "contract_id", nullable = false, updatable = false)
    private Long contractId;

    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;    // yyyy-MM-dd
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "markup", precision = 5, scale = 2, nullable = false)
    private double markup;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", length = 10, nullable = false)
    private ContractType contractType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 12, nullable = false)
    private ContractStatus status;

    @OneToMany(targetEntity = Room.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contract_id", referencedColumnName = "contract_id")
    private List<Room> rooms;

    public Contract( Hotel hotel, LocalDate startDate, LocalDate endDate, double markup, ContractType contractType, ContractStatus status ) {
        this.hotel = hotel;
        this.startDate = startDate;
        this.endDate = endDate;
        this.markup = Helper.roundToTwoDecimals(markup);
        this.contractType = contractType;
        this.status = status;
    }
}