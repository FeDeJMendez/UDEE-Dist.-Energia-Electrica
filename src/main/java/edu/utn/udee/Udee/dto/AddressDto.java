package edu.utn.udee.Udee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.utn.udee.Udee.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {
    Integer id;
    String address;
    String city;
    String state;
    String country;
    ClientDto client;
    RateDto rate;
//    List<Bill> bills;


    public static AddressDto from (Address address) {
        return AddressDto.builder().
                id(address.getId()).
                address(address.getAddress()).
                city(address.getCity()).
                state(address.getState()).
                country(address.getCountry()).
                client(ClientDto.from(address.getClient())).
                rate(RateDto.from(address.getRate())).
                build();
    }
}
