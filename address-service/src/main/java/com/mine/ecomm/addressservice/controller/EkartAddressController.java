package com.mine.ecomm.addressservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mine.ecomm.addressservice.dto.AddressDTO;
import com.mine.ecomm.addressservice.entity.Address;
import com.mine.ecomm.addressservice.exception.MetadataException;
import com.mine.ecomm.addressservice.service.AddressService;

import jakarta.validation.Valid;


/**
 * The type Ekart address controller.
 */
@CrossOrigin
@RestController
@RequestMapping("/address")
public class EkartAddressController {

    @Autowired
    private AddressService addressService;

    /**
     * Add address response entity.
     *
     * @param addressDTO the account dto
     *
     * @return the response entity
     */
    @PostMapping("/add")
    public ResponseEntity<String> addAddress(final @Valid @RequestBody AddressDTO addressDTO) {
        final String successMessage = addressService.addAddress(addressDTO);
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }

    /**
     * Add address response entity.
     *
     * @param addressDTO the account dto
     *
     * @return the response entity
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateAddress(final @Valid @RequestBody AddressDTO addressDTO) {
        final Boolean response = addressService.updateAddress(addressDTO);
        if (response) {
            final String successMessage = "Address updated successfully.";
            return new ResponseEntity<>(successMessage, HttpStatus.OK);
        } else {
            throw new MetadataException("Address not updated.");
        }
    }

    /**
     * Delete address response entity.
     *
     * @param addressId the address id
     *
     * @return the response entity
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAddress(final @PathVariable("id") String addressId) {
        final Boolean response = addressService.deleteAddressWithId(addressId);
        if (response) {
            final String successMessage = "Address deleted successfully.";
            return new ResponseEntity<>(successMessage, HttpStatus.OK);
        } else {
            throw new MetadataException("Address not updated.");
        }
    }

    /**
     * Gets all states.
     *
     * @return the all states
     */
    @GetMapping("/states")
    public ResponseEntity<List<String>> getAllStates() {
        final List<String> states = AddressDTO.getStates();
        return new ResponseEntity<>(states, HttpStatus.OK);
    }

    /**
     * Gets state with id.
     *
     * @param id the id
     *
     * @return the state with id
     */
    @GetMapping("/state/{id}")
    public ResponseEntity<String> getStateWithId(final @PathVariable("id") int id) {
        final String selectedState = addressService.getStateWithId(id);
        return new ResponseEntity<>(selectedState, HttpStatus.OK);
    }

    /**
     * Gets all addresses.
     *
     * @return the all addresses
     */
    @GetMapping("/addresses")
    public ResponseEntity<List<Address>> getAllAddresses() {
        final List<Address> addresses = addressService.getAllAddresses();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    /**
     * Gets address with id.
     *
     * @param addressId the address id
     *
     * @return the address with id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressWithId(final @PathVariable("id") String addressId) {
        final Address address = addressService.getAddressWithId(addressId);

        if (address == null) {
            throw new MetadataException("Address not found with given id.");
        }
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

}
