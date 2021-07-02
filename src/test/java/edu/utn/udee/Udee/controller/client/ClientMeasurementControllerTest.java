package edu.utn.udee.Udee.controller.client;

import edu.utn.udee.Udee.TestUtils.MeasurementTestUtils;
import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.enums.Rol;
import edu.utn.udee.Udee.dto.MeasurementDto;
import edu.utn.udee.Udee.dto.UserDto;
import edu.utn.udee.Udee.exceptions.AddressNotExistsException;
import edu.utn.udee.Udee.exceptions.MeterNotExistsException;
import edu.utn.udee.Udee.projections.KwhAndAmount;
import edu.utn.udee.Udee.service.backoffice.AddressService;
import edu.utn.udee.Udee.service.client.ClientMeasurementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
@PowerMockIgnore({
        "javax.security.*"})
public class ClientMeasurementControllerTest {

    private ClientMeasurementController clientMeasurementController;

    private ClientMeasurementService clientMeasurementService;
    private AddressService addressService;
    private ModelMapper modelMapper;
    private Authentication auth;

    @Before
    public void setUp(){
        this.clientMeasurementService = mock(ClientMeasurementService.class);
        this.addressService = mock(AddressService.class);
        this.auth = mock(Authentication.class);

        this.clientMeasurementController = new ClientMeasurementController(clientMeasurementService,modelMapper,addressService);
    }

    @Test
    public void getTotalKwhAndAmountBetweenDatesOk() throws AddressNotExistsException, MeterNotExistsException {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusMonths(1);
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        KwhAndAmount kwhAndAmount = factory.createProjection(KwhAndAmount.class);
        kwhAndAmount.setAmount(2.0);
        kwhAndAmount.setKwh(5.0);
        when(auth.getPrincipal()).thenReturn(UserDto.builder().id(1).username("Amoruso").rol(Rol.ROLE_CLIENT).client_id(1).build());
        when(clientMeasurementService.getTotalKwhAndAmountBetweenDates(1,startDate,endDate)).thenReturn(kwhAndAmount);

        ResponseEntity responseEntity = clientMeasurementController.totalKwhAndAmountBetweenDates(startDate,endDate,auth);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test(expected = AddressNotExistsException.class)
    public void testGetTotalKwhAndAmountAddressNotExistsException() throws AddressNotExistsException, MeterNotExistsException {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusMonths(1);
        when(auth.getPrincipal()).thenReturn(UserDto.builder().id(1).username("Amoruso").rol(Rol.ROLE_CLIENT).client_id(1).build());
        when(clientMeasurementService.getTotalKwhAndAmountBetweenDates(1,startDate,endDate)).thenThrow(new AddressNotExistsException());
        ResponseEntity<KwhAndAmount> responseEntity = clientMeasurementController.totalKwhAndAmountBetweenDates(startDate, endDate, auth);
    }

    @Test(expected = MeterNotExistsException.class)
    public void testGetTotalKwhAndAmountMeterNotExistsException() throws AddressNotExistsException, MeterNotExistsException {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusMonths(1);
        when(auth.getPrincipal()).thenReturn(UserDto.builder().id(1).username("Amoruso").rol(Rol.ROLE_CLIENT).client_id(1).build());
        when(clientMeasurementService.getTotalKwhAndAmountBetweenDates(1,startDate,endDate)).thenThrow(new MeterNotExistsException());
        ResponseEntity<KwhAndAmount> responseEntity = clientMeasurementController.totalKwhAndAmountBetweenDates(startDate, endDate, auth);
    }

    @Test
    public void testGetBetweenDatesOk() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusMonths(1);
        when(auth.getPrincipal()).thenReturn(UserDto.builder().id(1).username("Amoruso").rol(Rol.ROLE_CLIENT).client_id(1).build());
        when(clientMeasurementService.getBetweenDates(1,startDate,endDate)).thenReturn(MeasurementTestUtils.getMeasurementUnbilledList());

        ResponseEntity<List<MeasurementDto>> responseEntity = clientMeasurementController.getBetweenDates(startDate, endDate, auth);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetBetweenDatesNonContent() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusMonths(1);
        when(auth.getPrincipal()).thenReturn(UserDto.builder().id(1).username("Amoruso").rol(Rol.ROLE_CLIENT).client_id(1).build());
        when(clientMeasurementService.getBetweenDates(1,startDate,endDate)).thenReturn(MeasurementTestUtils.getEmptyMeasurementList());

        ResponseEntity<List<MeasurementDto>> responseEntity = clientMeasurementController.getBetweenDates(startDate, endDate, auth);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

}
