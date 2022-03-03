package com.sun_travels.sun_travels_api.configurations;

import com.sun_travels.sun_travels_api.services.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfiguration {
    private final ContractService contractService;

    @Autowired
    public SchedulingConfiguration( ContractService contractService ) {
        this.contractService = contractService;
    }

    @Scheduled(fixedRateString = "PT24H")   // Execute this task every 24 hrs (once a day)
    public void updateContractStatus() {
        this.contractService.updateContractStatus();
        this.contractService.dropDeletedContracts();
    }
}
