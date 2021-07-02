package edu.utn.udee.Udee.controller.backoffice;

import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Meter;
import edu.utn.udee.Udee.dto.MeterDto;
import edu.utn.udee.Udee.exceptions.AddressNotExistsException;
import edu.utn.udee.Udee.exceptions.AddressWithMeterException;
import edu.utn.udee.Udee.exceptions.MeterNotExistsException;
import edu.utn.udee.Udee.service.backoffice.MeterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backoffice/meters")
public class MeterController {

    private final MeterService meterService;
    private final ModelMapper modelMapper;

    @Autowired
    public MeterController(MeterService meterService, ModelMapper modelMapper) {
        this.meterService = meterService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity addMeter (@RequestBody MeterDto meterDto)
            throws AddressNotExistsException, AddressWithMeterException {
        Meter newMeter = meterService.addMeter(modelMapper.map(meterDto, Meter.class));
        return ResponseEntity.created(Conf.getLocation(newMeter)).build();
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<MeterDto>> getAll(Pageable pageable){
            Page page = meterService.getAll(pageable);
            return ResponseEntity.
                    status(page.getTotalElements() != 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).
                    header("X-Total-Count", Long.toString(page.getTotalElements())).
                    header("X-Total-Pages", Long.toString(page.getTotalPages())).
                    body(page.getContent());
    }

    @GetMapping(path = "/{serialNumber}", produces = "application/json")
    public ResponseEntity<MeterDto> getBySerialNumber (@PathVariable Integer serialNumber)
            throws MeterNotExistsException {
        Meter meter = meterService.getBySerialNumber(serialNumber);
        return ResponseEntity.ok(MeterDto.from(meter));
    }

    @DeleteMapping(path = "/{serialNumber}", produces = "application/json")
    public ResponseEntity deleteMeter(@PathVariable Integer serialNumber)
            throws MeterNotExistsException {
        meterService.deleteBySerialNumber(serialNumber);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping(path = "/{serialNumber}", consumes = "application/json")
    public ResponseEntity editMeter(@RequestBody MeterDto meterDto, @PathVariable Integer serialNumber)
            throws MeterNotExistsException {
        Meter meter = meterService.editMeter(modelMapper.map(meterDto, Meter.class), serialNumber);
        return  ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{serialNumber}/addresses/{id}", produces = "application/json")
    public ResponseEntity addAddressToMeter(@PathVariable Integer serialNumber, @PathVariable Integer id)
            throws MeterNotExistsException, AddressNotExistsException {
        meterService.addAddressToMeter(serialNumber, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
