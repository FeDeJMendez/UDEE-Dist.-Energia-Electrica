package edu.utn.udee.Udee.repository;

import edu.utn.udee.Udee.domain.Measurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;

@Repository
public interface MeasurementRepository extends CrudRepository<Measurement, Integer> {

    Page<Measurement> findAll(Pageable pageable);

    @Query(value = "select * from measurements " +
            "where meter_serial_number = ?1 " +
            "and date_time between ?2 and ?3",
            nativeQuery = true)
    List<Measurement> findByMeterAndDateTimeRange(Integer meterSerialNumber, LocalDate beginDateTime, LocalDate endDateTime);

    @Query(value = "select * from measurements " +
            "where billed = false and meter_serial_number = ?1",
            nativeQuery = true)
    List<Measurement> findUnbilledMeasurements(Integer meterSerialNumber);
}
