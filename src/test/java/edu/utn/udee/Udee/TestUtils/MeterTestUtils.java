package edu.utn.udee.Udee.TestUtils;

import edu.utn.udee.Udee.domain.Address;
import edu.utn.udee.Udee.domain.Meter;
import edu.utn.udee.Udee.dto.MeterDto;

import java.util.Collections;
import java.util.List;

import static edu.utn.udee.Udee.TestUtils.AddressTestUtils.*;
import static edu.utn.udee.Udee.TestUtils.MeasurementTestUtils.*;

public class MeterTestUtils {

    public static MeterDto getMeterDtoReceived (){
        return MeterDto.builder().
                brand("TestBrand").
                model("TestModel").
                measurements(null).
                address(getAddressDtoReceived()).
                build();
    }

    public static Meter getMeterReceived (){
        return Meter.builder().
                brand("TestBrand").
                model("TestModel").
                measurements(null).
                address(getAddressAdded()).
                build();
    }

    public static Meter getMeterAdded (){
        return Meter.builder().
                serialNumber(1).
                brand("TestBrand").
                model("TestModel").
                measurements(Collections.emptyList()).
                address(getAddressAdded()).
                build();
    }

    public static MeterDto getMeterDtoAdded (){
        return MeterDto.builder().
                serialNumber(1).
                brand("TestBrand").
                model("TestModel").
                measurements(Collections.emptyList()).
                address(getAddressDtoAdded()).
                build();
    }

    public static Meter getMeterWithoutAddress(){
        return Meter.builder().
                serialNumber(1).
                brand("TestBrand").
                model("TestModel").
                measurements(Collections.emptyList()).
                address(null).
                build();
    }

    public static List<Meter> getMeterNoMeasurementList(){
        List<Address> addresses = getAddressList();
        return List.of(
                Meter.builder().
                        serialNumber(1).
                        brand("TestBrand").
                        model("TestModel").
                        measurements(Collections.emptyList()).
                        address(/*addresses.get(0)*/null).
                        build(),
                Meter.builder().
                        serialNumber(2).
                        brand("TestBrandTwo").
                        model("TestModelTwo").
                        measurements(Collections.emptyList()).
                        address(/*addresses.get(1)*/null).
                        build());
    }

    public static List<Meter> getMeterWithMeasurementList(){
        List<Address> addresses = getAddressList();
        return List.of(
                Meter.builder().
                        serialNumber(1).
                        brand("TestBrand").
                        model("TestModel").
                        measurements(getMeasurementUnbilledList()).
                        address(addresses.get(0)).
                        build(),
                Meter.builder().
                        serialNumber(2).
                        brand("TestBrandTwo").
                        model("TestModelTwo").
                        measurements(getMeasurementUnbilledList()).
                        address(addresses.get(1)).
                        build());
    }

    public static Meter getMeterWithMeasurement(){
        return Meter.builder().
                serialNumber(1).
                brand("TestBrand").
                model("TestModel").
                measurements(getMeasurementUnbilledList()).
                address(getAddressAdded()).
                build();
    }

    public static Meter getMeterWithMeasurementsBilled(){
        return Meter.builder().
                serialNumber(1).
                brand("TestBrand").
                model("TestModel").
                measurements(getMeasurementBilledList()).
                address(getAddressAdded()).
                build();
    }
}
