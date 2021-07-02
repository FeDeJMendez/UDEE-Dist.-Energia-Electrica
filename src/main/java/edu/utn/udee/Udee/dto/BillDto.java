package edu.utn.udee.Udee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.utn.udee.Udee.domain.Bill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillDto {

    Integer id;
    Integer dni;
    LocalDate date;
    String fullName;
    String address;
    String city;
    Integer meterSerialNumber;
    Double firstMeasurement;
    Double lastMeasurement;
    LocalDateTime firstMeasurementDateTime;
    LocalDateTime lastMeasurementDateTime;
    Double totalMeasurementKwh;
    String rate; //rate type
    Double totalAmount;
    Boolean paid;

        public static BillDto from(Bill bill) {
            return BillDto.builder().
                    id(bill.getId()).
                    dni(bill.getDni()).
                    date(bill.getDate()).
                    fullName(bill.getFullName()).
                    address(bill.getAddress()).
                    city(bill.getCity()).
                    meterSerialNumber(bill.getMeterSerialNumber()).
                    firstMeasurement(bill.getFirstMeasurement()).
                    lastMeasurement(bill.getLastMeasurement()).
                    firstMeasurementDateTime(bill.getFirstMeasurementDateTime()).
                    lastMeasurementDateTime(bill.getLastMeasurementDateTime()).
                    totalMeasurementKwh(bill.getTotalMeasurementKwh()).
                    rate(bill.getRate()).
                    totalAmount(bill.getTotalAmount()).
                    paid(bill.getPaid()).
                    build();

        }
}
