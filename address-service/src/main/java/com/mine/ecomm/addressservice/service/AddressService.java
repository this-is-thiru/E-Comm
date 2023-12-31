package com.mine.ecomm.addressservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mine.ecomm.addressservice.dto.AddressDTO;
import com.mine.ecomm.addressservice.entity.Address;
import com.mine.ecomm.addressservice.repository.AddressRepository;

/**
 * The type Address service.
 */
@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    /**
     * Add address string.
     *
     * @param addressDTO the address dto
     *
     * @return the string
     */
    public String addAddress(final AddressDTO addressDTO) {
	final Address address = new Address();
	updateAddress(addressDTO, address);
	return "new address added successfully.";
    }

    /**
     * Update address boolean.
     *
     * @param addressDTO the address dto
     *
     * @return the boolean
     */
    public Boolean updateAddress(final AddressDTO addressDTO) {
	final Optional<Address> addressOptional = addressRepository.findById(addressDTO.getId());
	final Address address;
	if (addressOptional.isPresent()) {
	    address = addressOptional.get();
	    updateAddress(addressDTO, address);
	    return true;
	}
	return false;
    }

    private void updateAddress(AddressDTO addressDTO, Address address) {
	address.setDno(addressDTO.getDno());
	address.setStreet(addressDTO.getStreet());
	address.setCity(addressDTO.getCity());
	address.setState(getStateWithId(addressDTO.getStateId()));
	address.setPinCode(addressDTO.getPinCode());
	address.setPhoneNumber(addressDTO.getPhoneNumber());
	address.setId();
	addressRepository.saveAndFlush(address);
    }

    /**
     * Gets state with id.
     *
     * @param stateId the state id
     *
     * @return the state with id
     */
    public String getStateWithId(final int stateId) {
	final ArrayList<String> states = AddressDTO.getStates();
	return states.get(stateId);
    }

    /**
     * Gets all addresses.
     *
     * @return the all addresses
     */
    public List<Address> getAllAddresses() {
	return addressRepository.findAll();
    }

    /**
     * Delete address with id boolean.
     *
     * @param addressId the address id
     *
     * @return the boolean
     */
    public Boolean deleteAddressWithId(final String addressId) {
	final Optional<Address> optionalAddress = addressRepository.findById(addressId);
	if (optionalAddress.isPresent()) {
	    addressRepository.deleteById(addressId);
	    return true;
	}
	return false;
    }

    /**
     * Gets address with id.
     *
     * @param addressId the address id
     *
     * @return the address with id
     */
    public Address getAddressWithId(final String addressId) {
	final List<Address> addresses = getAllAddresses();
	for (Address address : addresses) {
	    if (address.getId().equals(addressId)) {
		return address;
	    }
	}
	return null;
    }
}
