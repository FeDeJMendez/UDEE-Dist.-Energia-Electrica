package edu.utn.udee.Udee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.utn.udee.Udee.domain.Measurement;
import edu.utn.udee.Udee.domain.Meter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasurementDto {

    private Integer idMeasurement;
    private Double kwh;
    private LocalDateTime dateTime;
    private MeterDto meter;
//    private Bill bill;
    private Boolean billed;

    public static MeasurementDto from(Measurement measurement) {
        Meter meter = measurement.getMeter();
        return MeasurementDto.builder().
                idMeasurement(measurement.getIdMeasurement()).
                kwh(measurement.getKwh()).
                dateTime(measurement.getDateTime()).
                //meter(MeterDto.from(measurement.getMeter())).
                meter(MeterDto.builder().
                    serialNumber(meter.getSerialNumber()).
                    brand(meter.getBrand()).
                    model(meter.getModel()).
                    build()).
                billed(measurement.getBilled()).
                build();
    }
}