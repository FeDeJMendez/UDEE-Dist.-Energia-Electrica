package edu.utn.udee.Udee.service.backoffice;

import edu.utn.udee.Udee.domain.Address;
import edu.utn.udee.Udee.exceptions.AddressExistsException;
import edu.utn.udee.Udee.exceptions.AddressNotExistsException;
import edu.utn.udee.Udee.exceptions.ClientNotExistsException;
import edu.utn.udee.Udee.exceptions.RateNotExistsException;
import edu.utn.udee.Udee.repository.AddressRepository;
import edu.utn.udee.Udee.repository.ClientRepository;
import edu.utn.udee.Udee.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final ClientRepository clientRepository;
    private final RateRepository rateRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository, ClientRepository clientRepository, RateRepository rateRepository){
        this.addressRepository = addressRepository;
        this.clientRepository = clientRepository;
        this.rateRepository = rateRepository;
    }

    public Address addAddress(Address address)
            throws AddressExistsException, ClientNotExistsException, RateNotExistsException {
        if(!addressRepository.existsByAddress(address.getAddress())){
            if(clientRepository.existsById(address.getClient().getId())) {
                if (rateRepository.existsById(address.getRate().getId()))
                    return addressRepository.save(address);
                else throw new RateNotExistsException();
            }
            else throw new ClientNotExistsException();
        } else throw new AddressExistsException();
    }

    public Page<Address> allAddress(Pageable pageable){
        return addressRepository.findAll(pageable);
    }

    public void deleteAddressById(Integer id) throws AddressNotExistsException {
        if (addressRepository.existsById(id))
            addressRepository.deleteById(id);
        else throw new AddressNotExistsException();
    }

    public Address editAddress(Address address, Integer id) throws AddressNotExistsException {
            Address editedAddress = addressRepository.findById(id).orElseThrow(AddressNotExistsException::new);

            editedAddress.setCity(address.getCity());
            editedAddress.setClient(address.getClient());
            editedAddress.setCountry(address.getCountry());
            editedAddress.setAddress(address.getAddress());
            editedAddress.setRate(address.getRate());
            editedAddress.setState(address.getState());

            addressRepository.save(editedAddress);

            return editedAddress;
    }

    public Address findAddressById(Integer id) throws AddressNotExistsException {
        return addressRepository.findById(id).orElseThrow(AddressNotExistsException::new);
    }

    public List<Address> findAddressByClientId(Integer id){
        return addressRepository.findAddressByClientId(id);
    }
}