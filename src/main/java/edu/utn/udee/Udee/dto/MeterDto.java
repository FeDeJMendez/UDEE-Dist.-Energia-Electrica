package edu.utn.udee.Udee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.utn.udee.Udee.domain.Measurement;
import edu.utn.udee.Udee.domain.Meter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeterDto {

    private Integer serialNumber;
    private String brand;
    private String model;
    private List<MeasurementDto> measurements;
    private AddressDto address;



    public static MeterDto from(Meter meter){
        List<Measurement> measurements = meter.getMeasurements();
        List<MeasurementDto> measurementDto = null;
        if (measurements!= null) {
            measurementDto = measurements.stream().map(x -> MeasurementDto.from(x)).collect(Collectors.toList());
        }
        return MeterDto.builder().
                serialNumber(meter.getSerialNumber()).
                brand(meter.getBrand()).
                model(meter.getModel()).
                measurements(measurementDto).
                address(AddressDto.from(meter.getAddress())).
                build();
    }

}
