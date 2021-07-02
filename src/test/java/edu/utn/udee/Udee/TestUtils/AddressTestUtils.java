package edu.utn.udee.Udee.TestUtils;

import edu.utn.udee.Udee.domain.Address;
import edu.utn.udee.Udee.domain.Rate;
import edu.utn.udee.Udee.dto.AddressDto;
import edu.utn.udee.Udee.dto.RateDto;

import java.util.Collections;
import java.util.List;

import static edu.utn.udee.Udee.TestUtils.ClientTestUtils.getClientDtoWithId;
import static edu.utn.udee.Udee.TestUtils.ClientTestUtils.getClientWithId;
import static edu.utn.udee.Udee.TestUtils.RateTestUtils.getRateWithId;
import static java.util.Collections.emptyList;

public class AddressTestUtils {

    public static AddressDto getAddressDtoReceived (){
        return AddressDto.builder().
                address("TestStreet 1234").
                city("TestCity").
                state("TestState").
                country("TestCountry").
                client(getClientDtoWithId()).
                rate(RateDto.builder().
                        id(1).
                        description("TestRateDescription").
                        amount(1.0).
                        addresses(emptyList()).
                        build()).
                build();
    }

    public static AddressDto getAddressDtoReceivedWithoutClient (){
        return AddressDto.builder().
                address("TestStreet 1234").
                city("TestCity").
                state("TestState").
                country("TestCountry").
                client(null).
                rate(RateDto.builder().
                        id(1).
                        description("TestRateDescription").
                        amount(1.0).
                        addresses(emptyList()).
                        build()).
                build();
    }

    public static AddressDto getAddressDtoReceivedWithoutRate (){
        return AddressDto.builder().
                address("TestStreet 1234").
                city("TestCity").
                state("TestState").
                country("TestCountry").
                client(getClientDtoWithId()).
                rate(null).
                build();
    }

    public static Address getAddressReceived (){
        return Address.builder().
                address("TestStreet 1234").
                city("TestCity").
                state("TestState").
                country("TestCountry").
                client(getClientWithId()).
                rate(Rate.builder().
                        id(1).
                        description("TestRateDescription").
                        amount(1.0).
                        addresses(Collections.emptyList()).
                        build()).
                build();
    }

    public static Address getAddressAdded (){
        return Address.builder().
                id(1).
                address("TestStreet 1234").
                city("TestCity").
                state("TestState").
                country("TestCountry").
                client(getClientWithId()).
                rate(Rate.builder().
                        id(1).
                        description("TestRateDescription").
                        amount(1.0).
                        addresses(Collections.emptyList()).
                        build()).
                build();
    }

    public static AddressDto getAddressDtoAdded (){
        return AddressDto.builder().
                id(1).
                address("TestStreet 1234").
                city("TestCity").
                state("TestState").
                country("TestCountry").
                client(getClientDtoWithId()).
                rate(RateDto.builder().
                        id(1).
                        description("TestRateDescription").
                        amount(1.0).
                        addresses(Collections.emptyList()).
                        build()).
                build();
    }

    public static List<Address> getAddressList(){
        return List.of(
                Address.builder().id(1).
                        address("TestStreet 1234").
                        city("TestCity").
                        state("TestState").
                        country("TestCountry").
                        client(getClientWithId()).
                        rate(Rate.builder().
                                id(1).
                                description("TestRateDescription").
                                amount(1.0).
                                addresses(Collections.emptyList()).
                                build()).
                        build(),
                Address.builder().id(2).
                        address("TestStreetTwo 1234").
                        city("TestCityTwo").
                        state("TestStateTwo").
                        country("TestCountryTwo").
                        client(getClientWithId()).
                        rate(Rate.builder().
                                id(1).
                                description("TestRateDescription").
                                amount(1.0).
                                addresses(Collections.emptyList()).
                                build()).
                        build());
    }

    /*public static List<AddressDto> getAddressDtoList() {
        return List.of(
                AddressDto.builder().id(1).
                        address("TestStreet 1234").
                        city("TestCity").
                        state("TestState").
                        country("TestCountry").
                        client(ClientTestUtils.getClientDto()).
                        rate(RateDto.builder().
                                id(1).
                                description("TestRateDescription").
                                amount(1.0).
                                addresses(Collections.emptyList()).
                                build()).
                        build(),
                AddressDto.builder().id(2).
                        address("TestStreetTwo 1234").
                        city("TestCityTwo").
                        state("TestStateTwo").
                        country("TestCountryTwo").
                        client(ClientTestUtils.getClientDto()).
                        rate(RateDto.builder().
                                id(1).
                                description("TestRateDescription").
                                amount(1.0).
                                addresses(Collections.emptyList()).
                                build()).
                        build());
    }*/
}