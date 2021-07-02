package edu.utn.udee.Udee.service.client;

import edu.utn.udee.Udee.domain.Address;
import edu.utn.udee.Udee.domain.Measurement;
import edu.utn.udee.Udee.domain.Meter;
import edu.utn.udee.Udee.exceptions.AddressNotExistsException;
import edu.utn.udee.Udee.exceptions.MeterNotExistsException;
import edu.utn.udee.Udee.projections.KwhAndAmount;
import edu.utn.udee.Udee.repository.ClientMeasurementRepository;
import edu.utn.udee.Udee.service.backoffice.AddressService;
import edu.utn.udee.Udee.service.backoffice.MeasurementService;
import edu.utn.udee.Udee.service.backoffice.MeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientMeasurementService {

    private final ClientMeasurementRepository clientMeasurementRepository;
    private final AddressService addressService;
    private final MeterService meterService;
    private final MeasurementService measurementService;

    @Autowired
    public ClientMeasurementService(ClientMeasurementRepository clientMeasurementRepository, AddressService addressService, MeterService meterService, MeasurementService measurementService) {
        this.clientMeasurementRepository = clientMeasurementRepository;
        this.addressService = addressService;
        this.meterService = meterService;
        this.measurementService = measurementService;
    }

    public KwhAndAmount getTotalKwhAndAmountBetweenDates(Integer client_id, LocalDate startDate, LocalDate endDate)
            throws AddressNotExistsException, MeterNotExistsException {
        List<Address> addresses = addressService.findAddressByClientId(client_id);
        if (addresses.size() != 0) {
            for (Address address : addresses) {
                Meter meter = meterService.getByAddress(address);
                if (meter != null){
                    if (meter.getMeasurements().size() == 0){
                            Measurement measurement = Measurement.builder().
                                    kwh(0.0).
                                    dateTime(LocalDateTime.now()).
                                    meter(meter).
                                    billed(true).
                                    build();
                            measurementService.addMeasurement(measurement);
                    }
                }
                else throw new MeterNotExistsException();
            }
            return clientMeasurementRepository.findTotalKwhAndAmountByClient(client_id, startDate, endDate);
        }
        else throw new AddressNotExistsException();
    }

    public List<Measurement> getBetweenDates(Integer client_id, LocalDate startDate, LocalDate endDate) {
            return clientMeasurementRepository.findBetweenDates(client_id, startDate, endDate);
    }

}
