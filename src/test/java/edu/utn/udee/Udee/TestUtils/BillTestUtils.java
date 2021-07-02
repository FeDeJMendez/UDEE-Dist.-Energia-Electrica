package edu.utn.udee.Udee.TestUtils;

import edu.utn.udee.Udee.domain.Address;
import edu.utn.udee.Udee.domain.Bill;
import edu.utn.udee.Udee.domain.Client;
import edu.utn.udee.Udee.domain.Meter;
import edu.utn.udee.Udee.dto.BillDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static edu.utn.udee.Udee.TestUtils.AddressTestUtils.*;
import static edu.utn.udee.Udee.TestUtils.ClientTestUtils.*;
import static edu.utn.udee.Udee.TestUtils.MeterTestUtils.*;
import static java.util.Collections.emptyList;

public class BillTestUtils {

    public static Bill getBill(){
        List<Client> clientList = getClientsList();
        List<Address> addressList = getAddressList();
        List<Meter> meterList = getMeterNoMeasurementList();
        return Bill.builder().
                id(1).
                dni(clientList.get(0).getDni()).
                date(LocalDate.of(2021,01,01)).
                fullName(clientList.get(0).getName() + " " + clientList.get(0).getSurname()).
                address(addressList.get(0).getAddress()).
                city(addressList.get(0).getCity()).
                meterSerialNumber(meterList.get(0).getSerialNumber()).
                firstMeasurement(1.0).
                lastMeasurement(1.0).
                firstMeasurementDateTime(LocalDateTime.of(2021,01,01,00,00,00)).
                lastMeasurementDateTime(LocalDateTime.of(2021,01,31,00,00,00)).
                rate("Rate1").
                totalAmount(100.00).
                paid(false).
                build();
    }

    public static BillDto getBillDto(){
        return BillDto.from(getBill());
    }

    public static List<Bill> getBillList(){
        List<Client> clientList = getClientsList();
        List<Address> addressList = getAddressList();
        List<Meter> meterList = getMeterNoMeasurementList();
        return List.of(
                Bill.builder().
                        id(1).
                        dni(clientList.get(0).getDni()).
                        date(LocalDate.of(2021,01,01)).
                        fullName(clientList.get(0).getName() + " " + clientList.get(0).getSurname()).
                        address(addressList.get(0).getAddress()).
                        city(addressList.get(0).getCity()).
                        meterSerialNumber(meterList.get(0).getSerialNumber()).
                        firstMeasurement(1.0).
                        lastMeasurement(1.0).
                        firstMeasurementDateTime(LocalDateTime.of(2021,01,01,00,00,00)).
                        lastMeasurementDateTime(LocalDateTime.of(2021,01,31,00,00,00)).
                        rate("Rate1").
                        totalAmount(100.00).
                        paid(false).
                        build(),
                Bill.builder().
                        id(2).
                        dni(clientList.get(1).getDni()).
                        date(LocalDate.of(2021,01,01)).
                        fullName(clientList.get(1).getName() + " " + clientList.get(1).getSurname()).
                        address(addressList.get(1).getAddress()).
                        city(addressList.get(1).getCity()).
                        meterSerialNumber(meterList.get(1).getSerialNumber()).
                        firstMeasurement(2.0).
                        lastMeasurement(2.0).
                        firstMeasurementDateTime(LocalDateTime.of(2021,01,01,00,00,00)).
                        lastMeasurementDateTime(LocalDateTime.of(2021,01,31,00,00,00)).
                        rate("Rate1").
                        totalAmount(200.00).
                        paid(false).
                        build());
    }

    public static List<BillDto> getBillDtoList(){
        return getBillList().stream().
                map(x -> BillDto.from(x)).
                collect(Collectors.toList());
    }

    public static List<Bill> getBillListByAddressOrClient(){
        Client client = getClientWithId();
        Address address = getAddressAdded();
        Meter meter = getMeterAdded();
        return List.of(
                Bill.builder().
                        id(1).
                        dni(client.getDni()).
                        date(LocalDate.of(2021,01,01)).
                        fullName(client.getName() + " " + client.getSurname()).
                        address(address.getAddress()).
                        city(address.getCity()).
                        meterSerialNumber(meter.getSerialNumber()).
                        firstMeasurement(1.0).
                        lastMeasurement(1.0).
                        firstMeasurementDateTime(LocalDateTime.of(2021,01,01,00,00,00)).
                        lastMeasurementDateTime(LocalDateTime.of(2021,01,31,00,00,00)).
                        rate("Rate1").
                        totalAmount(100.00).
                        paid(false).
                        build(),
                Bill.builder().
                        id(2).
                        dni(client.getDni()).
                        date(LocalDate.of(2021,01,01)).
                        fullName(client.getName() + " " + client.getSurname()).
                        address(address.getAddress()).
                        city(address.getCity()).
                        meterSerialNumber(meter.getSerialNumber()).
                        firstMeasurement(2.0).
                        lastMeasurement(2.0).
                        firstMeasurementDateTime(LocalDateTime.of(2021,01,01,00,00,00)).
                        lastMeasurementDateTime(LocalDateTime.of(2021,01,31,00,00,00)).
                        rate("Rate1").
                        totalAmount(200.00).
                        paid(false).
                        build());
    }

    public static List<BillDto> getBillDtoListByAddressOrClient(){
        return getBillListByAddressOrClient().stream().
                map(x -> BillDto.from(x)).
                collect(Collectors.toList());
    }

    public static List<Bill> getEmptyBillList(){
        return emptyList();
    }
}