package edu.utn.udee.Udee.controller.backoffice;

import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Rate;
import edu.utn.udee.Udee.dto.RateDto;
import edu.utn.udee.Udee.exceptions.RateExistsException;
import edu.utn.udee.Udee.exceptions.RateNotExistsException;
import edu.utn.udee.Udee.service.backoffice.RateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backoffice/rates")
public class RateController {

    private final RateService rateService;
    private final ModelMapper modelMapper;

    @Autowired
    public RateController(RateService rateService, ModelMapper modelMapper) {
        this.rateService = rateService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity addRate (@RequestBody RateDto rateDto)
            throws RateExistsException {
        Rate newRate = rateService.addRate(modelMapper.map(rateDto, Rate.class));
        return ResponseEntity.created(Conf.getLocation(newRate)).build();
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<RateDto>> getAll(Pageable pageable){
        Page page = rateService.getAll(pageable);
        return ResponseEntity.
                //status(page.getTotalElements() != 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).
                        status(page.getContent().isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK).
                        header("X-Total-Count", Long.toString(page.getTotalElements())).
                        header("X-Total-Pages", Long.toString(page.getTotalPages())).
                        body(page.getContent());
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<RateDto> getById (@PathVariable Integer id)
            throws RateNotExistsException {
        Rate rate = rateService.getById(id);
        return ResponseEntity.ok(RateDto.from(rate));
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity deleteRate(@PathVariable Integer id)
            throws RateNotExistsException {
        rateService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity editRate(@RequestBody RateDto rateDto, @PathVariable Integer id)
            throws RateNotExistsException {
        Rate rate = rateService.editRate(modelMapper.map(rateDto, Rate.class), id);
        return  ResponseEntity.ok().build();
    }

    /*@PutMapping(path = "/{idRate}/addresses/{idAddress}", produces = "application/json")
    public ResponseEntity addAddressToRate (@PathVariable Integer idRate, @PathVariable Integer idAddress)
            throws RateNotExistsException, AddressNotExistsException {
        rateService.addAddressToRate(idRate, idAddress);
        return ResponseEntity.status(HttpStatus.OK).build();
    }*/
}
