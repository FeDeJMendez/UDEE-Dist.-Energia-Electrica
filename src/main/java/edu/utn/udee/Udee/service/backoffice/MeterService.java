package edu.utn.udee.Udee.service.backoffice;

import edu.utn.udee.Udee.domain.Address;
import edu.utn.udee.Udee.domain.Measurement;
import edu.utn.udee.Udee.domain.Meter;
import edu.utn.udee.Udee.exceptions.*;
import edu.utn.udee.Udee.repository.MeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeterService {

    private final MeterRepository meterRepository;
    private final MeasurementService measurementService;
    private final AddressService addressService;

    @Autowired
    public MeterService(MeterRepository meterRepository, MeasurementService measurementService, AddressService addressService) {
        this.meterRepository = meterRepository;
        this.measurementService = measurementService;
        this.addressService = addressService;
    }


    public Meter addMeter(Meter meter)
            throws AddressNotExistsException, AddressWithMeterException{
        if (meter.getAddress() != null) {
            Address address = addressService.findAddressById(meter.getAddress().getId());
            if (meterRepository.findByAddress(address) != null) {
                throw new AddressWithMeterException();
            }
        }
        return meterRepository.save(meter);
    }

    public Page<Meter> getAll(Pageable pageable) {
        return meterRepository.findAll(pageable);
    }

    public List<Meter> getAll() {
        return (List<Meter>) meterRepository.findAll();
    }

    public Meter getBySerialNumber(Integer serialNumber)
            throws MeterNotExistsException {
        return meterRepository.findById(serialNumber).
                orElseThrow(MeterNotExistsException::new);
    }

    public Meter getByAddress(Address address) throws MeterNotExistsException {
        Meter meter = meterRepository.findByAddress(address);
        if (meter != null){
            return meter;
        }
        else throw new MeterNotExistsException();
    }

    public void deleteBySerialNumber(Integer serialNumber)
            throws MeterNotExistsException {
        if (meterRepository.existsById(serialNumber))
            meterRepository.deleteById(serialNumber);
        else throw new MeterNotExistsException();
    }

    public Meter editMeter(Meter meter, Integer serialNumber)
            throws MeterNotExistsException {
        Meter editedMeter = this.getBySerialNumber(serialNumber);
        editedMeter.setBrand(meter.getBrand());
        editedMeter.setModel(meter.getModel());
        editedMeter.setMeasurements(meter.getMeasurements());
        editedMeter.setAddress(meter.getAddress());
        meterRepository.save(editedMeter);
        return editedMeter;
    }

    /*public void addMeasurementToMeter(Integer serialNumber, Integer idMeasurement)
            throws MeterNotExistsException, MeasurementNotExistsException {
        if (meterRepository.existsById(serialNumber)) {
            Meter meter = this.getBySerialNumber(serialNumber);
            Measurement measurement = measurementService.getById(idMeasurement);
            measurement.setMeter(meter);
            meter.getMeasurements().add(measurement);
            meterRepository.save(meter);
        }
        else throw new MeterNotExistsException();
    }*/

    public void addAddressToMeter(Integer serialNumber, Integer id)
            throws MeterNotExistsException, AddressNotExistsException {
        if (meterRepository.existsById(serialNumber)) {
            Meter meter = this.getBySerialNumber(serialNumber);
            Address address = addressService.findAddressById(id);
            meter.setAddress(address);
            meterRepository.save(meter);
        }
        else throw new MeterNotExistsException();
    }

    public void setBilledMeasurement(Meter meter)
            throws MeterNotExistsException{
//        Meter editMeter = getBySerialNumber(meter.getSerialNumber());
        if (meterRepository.existsById(meter.getSerialNumber())){
            for (Measurement measurement: meter.getMeasurements()) {
                if(measurement.getBilled() == false) {
                    measurement.setBilled(true);
                }
            }
        }
        else throw new MeterNotExistsException();
        meterRepository.save(meter);
    }
}
