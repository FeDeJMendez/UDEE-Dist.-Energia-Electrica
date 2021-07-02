package edu.utn.udee.Udee.controller.backoffice;

import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Address;
import edu.utn.udee.Udee.domain.Measurement;
import edu.utn.udee.Udee.domain.Meter;
import edu.utn.udee.Udee.dto.MeasurementDto;
import edu.utn.udee.Udee.exceptions.AddressNotExistsException;
import edu.utn.udee.Udee.exceptions.MeasurementNotExistsException;
import edu.utn.udee.Udee.exceptions.MeterIsRequiredException;
import edu.utn.udee.Udee.exceptions.MeterNotExistsException;
import edu.utn.udee.Udee.service.backoffice.AddressService;
import edu.utn.udee.Udee.service.backoffice.MeasurementService;
import edu.utn.udee.Udee.service.backoffice.MeterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/backoffice/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final MeterService meterService;
    private final AddressService addressService;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementController(MeasurementService measurementService, MeterService meterService, AddressService addressService, ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.meterService = meterService;
        this.addressService = addressService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity addMeasurement (@RequestBody MeasurementDto measurementDto)
            throws MeterNotExistsException, MeterIsRequiredException {
        if (measurementDto.getMeter() == null)
            throw new MeterIsRequiredException();
        Measurement newMeasurement = measurementService.addMeasurement(Measurement.builder().
                idMeasurement(measurementDto.getIdMeasurement()).
                kwh(measurementDto.getKwh()).
                dateTime(measurementDto.getDateTime()).
                meter(meterService.getBySerialNumber(measurementDto.getMeter().getSerialNumber())).
                build());
        return ResponseEntity.created(Conf.getLocation(newMeasurement)).build();
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<MeasurementDto>> getAll(Pageable pageable){
        Page page = measurementService.getAll(pageable);
        return ResponseEntity.
                status(page.getTotalElements() != 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).
                header("X-Total-Count", Long.toString(page.getTotalElements())).
                header("X-Total-Pages", Long.toString(page.getTotalPages())).
                body(page.getContent());
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<MeasurementDto> getById (@PathVariable Integer id)
            throws MeasurementNotExistsException {
        Measurement measurement = measurementService.getById(id);
        return ResponseEntity.ok(MeasurementDto.from(measurement));
    }


    //***GET BY ADDRESS AND DATETIME RANGE***//
    @GetMapping(value = "/addresses/{idAddress}/{start}/{end}", produces = "application/json")
    public  ResponseEntity<List<MeasurementDto>> getByAddressAndDateTimeRange(@PathVariable Integer idAddress,
                                                                              @PathVariable(value = "start") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
                                                                              @PathVariable(value = "end") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate)
            throws AddressNotExistsException, MeterNotExistsException {
        Address address = addressService.findAddressById(idAddress);
        Meter meter = meterService.getByAddress(address);
        List<Measurement> filteredMeasurements = measurementService.getByMeterAndDateTimeRange(meter.getSerialNumber(), startDate, endDate);
        List<MeasurementDto> filteredMeasurementsDto = listMeasurementsToDto(filteredMeasurements);
        return ResponseEntity.
                status(filteredMeasurementsDto.size() != 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).
                body(filteredMeasurementsDto);
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity deleteMeasurement(@PathVariable Integer id)
            throws MeasurementNotExistsException {
        measurementService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private List<MeasurementDto> listMeasurementsToDto (List<Measurement> list){
        return list.stream().
                map(x -> MeasurementDto.from(x)).
                collect(Collectors.toList());
    }

}
