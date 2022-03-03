package com.sun_travels.sun_travels_api.contollers;

import com.sun_travels.sun_travels_api.dtos.AddContractRequest;
import com.sun_travels.sun_travels_api.models.Contract;
import com.sun_travels.sun_travels_api.services.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api.sun-travels.com/contract")
public class ContractController {
    private final ContractService contractService;

    @Autowired
    public ContractController( ContractService contractService ) { this.contractService = contractService; }

     @PostMapping()
    public ResponseEntity<Contract> addContract( @RequestBody AddContractRequest request ) {
        Contract newContract = contractService.addContract( request );
        return new ResponseEntity<>( newContract, HttpStatus.CREATED);
    }

    @GetMapping("/contracts")
    public ResponseEntity<List<Contract>> getContracts() {
        List<Contract> contracts = contractService.getContracts();
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    @DeleteMapping("/{contractId}")
    public ResponseEntity<?> deleteContract( @PathVariable("contractId") Long contractId ) {
        contractService.deleteContract( contractId );
        return new ResponseEntity<>(HttpStatus.OK);
    }
}