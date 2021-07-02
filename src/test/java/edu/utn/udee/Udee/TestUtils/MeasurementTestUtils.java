package edu.utn.udee.Udee.TestUtils;

import edu.utn.udee.Udee.domain.Measurement;
import edu.utn.udee.Udee.domain.Meter;
import edu.utn.udee.Udee.dto.MeasurementDto;
import edu.utn.udee.Udee.dto.MeterDto;

import java.time.LocalDateTime;
import java.util.List;

import static edu.utn.udee.Udee.TestUtils.MeterTestUtils.getMeterAdded;
import static edu.utn.udee.Udee.TestUtils.MeterTestUtils.getMeterDtoAdded;
import static java.util.Collections.emptyList;

public class MeasurementTestUtils {

    public static Measurement getNewMeasurement(){
        Meter meter = getMeterAdded();
        return Measurement.builder().
                kwh(1.0).
                dateTime(LocalDateTime.of(2021,01,01,00,00,00)).
                meter(meter).
                billed(false).
                build();
    }

    public static MeasurementDto getNewMeasurementDto(){
        return MeasurementDto.from(getNewMeasurement());
    }

    public static Measurement getUnbilledMeasurement(){
        Meter meter = getMeterAdded();
        return Measurement.builder().
                        idMeasurement(1).
                        kwh(1.0).
                        dateTime(LocalDateTime.of(2021,01,01,00,00,00)).
                        meter(meter).
                        billed(false).
                        build();
    }

    public static Measurement getUnbilledMeasurementNullDateTime(){
        Meter meter = getMeterAdded();
        return Measurement.builder().
                idMeasurement(1).
                kwh(1.0).
                dateTime(null).
                meter(meter).
                billed(false).
                build();
    }

    public static Measurement getBilledMeasurement(){
        Measurement measurement = getUnbilledMeasurement();
        measurement.setBilled(true);
        return measurement;
    }

    public static MeasurementDto getUnbilledMeasurementDto(){
        MeterDto meterDto = getMeterDtoAdded();
        return MeasurementDto.builder().
                idMeasurement(1).
                kwh(1.0).
                dateTime(LocalDateTime.of(2021,01,01,00,00,00)).
                meter(meterDto).
                billed(false).
                build();
    }

    public static MeasurementDto getBilledMeasurementDto(){
        MeasurementDto measurementDto = getUnbilledMeasurementDto();
        measurementDto.setBilled(true);
        return measurementDto;
    }


    public static Measurement getUnbilledMeasurementWithoutMeter(){
        return Measurement.builder().
                kwh(1.0).
                dateTime(LocalDateTime.of(2021,01,01,00,00,00)).
                meter(null).
                billed(false).
                build();
    }

    public static MeasurementDto getUnbilledMeasurementDtoWithoutMeter(){
        return MeasurementDto.builder().
                kwh(1.0).
                dateTime(LocalDateTime.of(2021,01,01,00,00,00)).
                meter(null).
                billed(false).
                build();
    }

    public static List<Measurement> getMeasurementUnbilledList(){
        Meter meter = getMeterAdded();
        return List.of(
                Measurement.builder().
                        idMeasurement(1).
                        kwh(1.0).
                        dateTime(LocalDateTime.of(2021,01,01,00,00,00)).
                        meter(meter).
                        billed(false).
                        build(),
                Measurement.builder().
                        idMeasurement(2).
                        kwh(1.0).
                        dateTime(LocalDateTime.of(2021,01,01,00,05,00)).
                        meter(meter).
                        billed(false).
                        build(),
                Measurement.builder().
                        idMeasurement(3).
                        kwh(1.0).
                        dateTime(LocalDateTime.of(2021,02,01,00,00,00)).
                        meter(meter).
                        billed(false).
                        build(),
                Measurement.builder().
                        idMeasurement(4).
                        kwh(1.0).
                        dateTime(LocalDateTime.of(2021,02,01,00,05,00)).
                        meter(meter).
                        billed(false).
                        build());
    }

    public static List<MeasurementDto> getMeasurementDtoUnbilledList(){
        MeterDto meterDto = getMeterDtoAdded();
        return List.of(
                MeasurementDto.builder().
                        idMeasurement(1).
                        kwh(1.0).
                        dateTime(LocalDateTime.of(2021,01,01,00,00,00)).
                        meter(meterDto).
                        billed(false).
                        build(),
                MeasurementDto.builder().
                        idMeasurement(2).
                        kwh(1.0).
                        dateTime(LocalDateTime.of(2021,01,01,00,05,00)).
                        meter(meterDto).
                        billed(false).
                        build(),
                MeasurementDto.builder().
                        idMeasurement(3).
                        kwh(1.0).
                        dateTime(LocalDateTime.of(2021,02,01,00,00,00)).
                        meter(meterDto).
                        billed(false).
                        build(),
                MeasurementDto.builder().
                        idMeasurement(4).
                        kwh(1.0).
                        dateTime(LocalDateTime.of(2021,02,01,00,05,00)).
                        meter(meterDto).
                        billed(false).
                        build());
    }

    public static List<Measurement> getMeasurementBilledList(){
        List<Measurement> measurementList = getMeasurementUnbilledList();
        for (Measurement measurement : measurementList){
            measurement.setBilled(true);
        }
        return measurementList;

    }

    public static List<Measurement> getEmptyMeasurementList(){
        return emptyList();
    }

}
