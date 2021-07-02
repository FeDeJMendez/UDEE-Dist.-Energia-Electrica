package edu.utn.udee.Udee.controller.backoffice;

import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Address;
import edu.utn.udee.Udee.dto.AddressDto;
import edu.utn.udee.Udee.exceptions.*;
import edu.utn.udee.Udee.service.backoffice.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/backoffice/addresses")
public class AddressController {

    private final AddressService addressService;
    private final ModelMapper modelMapper;

    @Autowired
    public AddressController(AddressService addressService, ModelMapper modelMapper){
        this.addressService = addressService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity addAddress(@RequestBody AddressDto addressDto)
            throws AddressExistsException, ClientNotExistsException, RateNotExistsException, ClientIsRequiredException, RateIsRequiredException {
        if (addressDto.getClient() == null)
            throw new ClientIsRequiredException();
        if (addressDto.getRate() == null)
            throw new RateIsRequiredException();
        Address newAddress = addressService.addAddress(modelMapper.map(addressDto, Address.class));
        return ResponseEntity.created(Conf.getLocation(newAddress)).build();
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity editAddress(@RequestBody AddressDto addressDto, @PathVariable Integer id) throws AddressNotExistsException {
        Address editedAddress = addressService.editAddress(modelMapper.map(addressDto, Address.class), id);

        return  ResponseEntity.ok().build();
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<AddressDto>> allAddress(Pageable pageable){
        Page page = addressService.allAddress(pageable);
        return response(page);
    }

    @DeleteMapping(value = "{id}", produces = "application/json")
    public ResponseEntity deleteAddressById(@PathVariable(value = "id") Integer id) throws AddressNotExistsException {
        addressService.deleteAddressById(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private ResponseEntity response(Page page) {
        HttpStatus httpStatus = page.getContent().isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.
                status(httpStatus).
                header("X-Total-Count", Long.toString(page.getTotalElements())).
                header("X-Total-Pages", Long.toString(page.getTotalPages())).
                body(page.getContent());
    }
}
