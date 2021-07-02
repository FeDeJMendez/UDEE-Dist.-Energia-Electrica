package edu.utn.udee.Udee.controller.client;

import edu.utn.udee.Udee.domain.Measurement;
import edu.utn.udee.Udee.dto.MeasurementDto;
import edu.utn.udee.Udee.dto.UserDto;
import edu.utn.udee.Udee.exceptions.AddressNotExistsException;
import edu.utn.udee.Udee.exceptions.MeterNotExistsException;
import edu.utn.udee.Udee.projections.KwhAndAmount;
import edu.utn.udee.Udee.service.backoffice.AddressService;
import edu.utn.udee.Udee.service.client.ClientMeasurementService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/client/measurements")
public class ClientMeasurementController {

    private final ClientMeasurementService clientMeasurementService;
    private final AddressService addressService;
    private final ModelMapper modelMapper;

    @Autowired
    public ClientMeasurementController(ClientMeasurementService clientMeasurementService, ModelMapper modelMapper, AddressService addressService){
        this.clientMeasurementService = clientMeasurementService;
        this.modelMapper = modelMapper;
        this.addressService = addressService;
    }

    @GetMapping(value = "consumption/{start}/{end}", produces = "application/json")
    public ResponseEntity<KwhAndAmount> totalKwhAndAmountBetweenDates(@PathVariable(value = "start") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
                                                                      @PathVariable(value = "end") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate,
                                                                      Authentication auth)
            throws AddressNotExistsException, MeterNotExistsException {
        UserDto userDto = (UserDto) auth.getPrincipal();
        KwhAndAmount kwhAndAmount = clientMeasurementService.getTotalKwhAndAmountBetweenDates(userDto.getClient_id(), startDate, endDate);
        return ResponseEntity.ok(kwhAndAmount);

    }

    @GetMapping(value = "{start}/{end}", produces = "application/json")
    public ResponseEntity<List<MeasurementDto>> getBetweenDates (@PathVariable(value = "start") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
                                                                 @PathVariable(value = "end") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate,
                                                                 Authentication auth){
        UserDto userDto = (UserDto) auth.getPrincipal();
        List<Measurement> measurements = clientMeasurementService.getBetweenDates(userDto.getClient_id(), startDate, endDate);
        List<MeasurementDto> measurementsDto = listMeasurementsToDtoIgnoreMeter(measurements);
        return ResponseEntity.
                status(measurementsDto.size() != 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).
                body(measurementsDto);
    }

    private List<MeasurementDto> listMeasurementsToDtoIgnoreMeter (List<Measurement> list){
        return list.stream().
                map(x -> MeasurementDto.builder().
                        idMeasurement(x.getIdMeasurement()).
                        kwh(x.getKwh()).
                        dateTime(x.getDateTime()).
                        billed(x.getBilled()).
                        build()).
                collect(Collectors.toList());
    }
}
