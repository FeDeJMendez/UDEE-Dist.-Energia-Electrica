package edu.utn.udee.Udee.TestUtils;

import edu.utn.udee.Udee.domain.Rate;
import edu.utn.udee.Udee.dto.RateDto;

import java.util.Collections;
import java.util.List;

public class RateTestUtils {

    public static Rate getRateWithoutId() {
        return Rate.builder().description("Rate1").amount(1.0).addresses(AddressTestUtils.getAddressList()).build();
    }

    public static Rate getRateWithId(){
        return Rate.builder().id(1).description("Rate1").amount(1.0).addresses(AddressTestUtils.getAddressList()).build();
    }

    public static RateDto getRateDto() {
        return RateDto.builder().description("Rate1").amount(1.0).addresses(null).build();
    }

    public static List<RateDto> getRateDtoList () {
        List<RateDto> rateDtoList = List.of(RateDto.builder().description("Rate1").amount(1.0).addresses(null).build(),
                RateDto.builder().description("Rate2").amount(2.0).addresses(null).build()
                );

        return rateDtoList;
    }

    public static List<Rate> getRateList () {
        List<Rate> rateList = List.of(Rate.builder().description("Rate1").amount(1.0).addresses(null).build(),
                Rate.builder().description("Rate2").amount(2.0).addresses(null).build()
        );

        return rateList;
    }

    public static List<Rate> getEmptyRateList(){
        return Collections.emptyList();
    }

}
