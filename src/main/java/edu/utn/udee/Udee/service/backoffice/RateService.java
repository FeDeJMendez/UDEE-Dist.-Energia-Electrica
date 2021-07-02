package edu.utn.udee.Udee.service.backoffice;

import edu.utn.udee.Udee.domain.Address;
import edu.utn.udee.Udee.domain.Rate;
import edu.utn.udee.Udee.exceptions.AddressNotExistsException;
import edu.utn.udee.Udee.exceptions.RateExistsException;
import edu.utn.udee.Udee.exceptions.RateNotExistsException;
import edu.utn.udee.Udee.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RateService {

    private final RateRepository rateRepository;
    private final AddressService addressService;

    @Autowired
    public RateService(RateRepository rateRepository, AddressService addressService) {
        this.rateRepository = rateRepository;
        this.addressService = addressService;
    }

    public Rate addRate(Rate rate)
            throws RateExistsException{
        if (!rateRepository.existsByDescription(rate.getDescription()))
            return rateRepository.save(rate);
        else throw new RateExistsException();
    }

    public Page<Rate> getAll(Pageable pageable) {
        return rateRepository.findAll(pageable);
    }

    public Rate getById(Integer id)
            throws RateNotExistsException {
        return rateRepository.findById(id).
                orElseThrow(RateNotExistsException::new);
    }

    public void deleteById(Integer id)
            throws RateNotExistsException{
        if (rateRepository.existsById(id))
            rateRepository.deleteById(id);
        else throw new RateNotExistsException();
    }

    public Rate editRate(Rate rate, Integer id)
            throws  RateNotExistsException {
        Rate editedRate = this.getById(id);
        editedRate.setDescription(rate.getDescription());
        editedRate.setAmount(rate.getAmount());
        rateRepository.save(editedRate);
        return editedRate;
    }

    /*public void addAddressToRate(Integer idRate, Integer idAddress)
            throws RateNotExistsException, AddressNotExistsException {
        if (rateRepository.existsById(idRate)) {
            Rate rate = this.getById(idRate);
            Address address = addressService.findAddressById(idAddress);
            address.setRate(rate);
            rate.getAddresses().add(address);
            rateRepository.save(rate);
        }
        else throw new RateNotExistsException();
    }*/
}
