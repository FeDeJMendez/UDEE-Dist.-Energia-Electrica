package edu.utn.udee.Udee.service.backoffice;

import edu.utn.udee.Udee.domain.Measurement;
import edu.utn.udee.Udee.exceptions.MeasurementNotExistsException;
import edu.utn.udee.Udee.exceptions.MeterNotExistsException;
import edu.utn.udee.Udee.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public Measurement addMeasurement(Measurement measurement) {
        measurement.setBilled(false);
        measurement.setDateTime(LocalDateTime.now());
        return measurementRepository.save(measurement);
    }

    public Page<Measurement> getAll(Pageable pageable) {
        return measurementRepository.findAll(pageable);
    }

    public Measurement getById(Integer id)
            throws MeasurementNotExistsException {
        return measurementRepository.findById(id).
                orElseThrow(MeasurementNotExistsException::new);
    }

    public List<Measurement> getByMeterAndDateTimeRange(Integer meterSerialNumber, LocalDate beginDate, LocalDate endDate) {
        return measurementRepository.findByMeterAndDateTimeRange(meterSerialNumber, beginDate, endDate);
    }

    public List<Measurement> getUnbilledMeasurements (Integer meterSerialNumber) {
        List<Measurement> unbilledMeasurements = measurementRepository.findUnbilledMeasurements(meterSerialNumber);
        return unbilledMeasurements;
    }

    public void deleteById(Integer id)
            throws MeasurementNotExistsException {
        if (measurementRepository.existsById(id))
            measurementRepository.deleteById(id);
        else throw new MeasurementNotExistsException();
    }
}
