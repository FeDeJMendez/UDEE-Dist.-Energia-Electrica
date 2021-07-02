package edu.utn.udee.Udee.controller.backoffice;

import edu.utn.udee.Udee.TestUtils.RateTestUtils;
import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Rate;
import edu.utn.udee.Udee.dto.RateDto;
import edu.utn.udee.Udee.exceptions.RateExistsException;
import edu.utn.udee.Udee.exceptions.RateNotExistsException;
import edu.utn.udee.Udee.service.backoffice.RateService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
public class RateControllerTest {

    private RateController rateController;

    private RateService rateService;
    private ModelMapper modelMapper;

    @Before
    public void setUp() {
        this.rateService = mock(RateService.class);
        this.modelMapper = mock(ModelMapper.class);
        this.rateController = new RateController(rateService, modelMapper);
    }

    @Test
    public void testAddRateOk() throws RateExistsException {
        PowerMockito.mockStatic(Conf.class);
        when(rateService.addRate(RateTestUtils.getRateWithoutId())).thenReturn(RateTestUtils.getRateWithId());

        try {
            ResponseEntity responseEntity = rateController.addRate(RateTestUtils.getRateDto());
            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        } catch (RateExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = RateExistsException.class)
    public void testAddRateException()  throws RateExistsException{
        when(rateService.addRate(any())).thenThrow(new RateExistsException());
        ResponseEntity responseEntity = rateController.addRate(RateTestUtils.getRateDto());
    }

    @Test
    public void testGetAllHttpStatus200(){
        PageRequest pageable = PageRequest.of(1, 10);
        Page<Rate> mockedPage = mock(Page.class);
        PowerMockito.when(mockedPage.getTotalElements()).thenReturn(100L);
        PowerMockito.when(mockedPage.getTotalPages()).thenReturn(10);
        PowerMockito.when(mockedPage.getContent()).thenReturn(RateTestUtils.getRateList());
        PowerMockito.when(rateService.getAll(pageable)).thenReturn(mockedPage);

        ResponseEntity<List<RateDto>> responseEntity = rateController.getAll(pageable);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllHttpStatusNonContent(){
        PageRequest pageable = PageRequest.of(50, 10);
        Page<Rate> mockedPage = mock(Page.class);
        PowerMockito.when(mockedPage.getTotalElements()).thenReturn(100L);
        PowerMockito.when(mockedPage.getTotalPages()).thenReturn(10);
        PowerMockito.when(mockedPage.getContent()).thenReturn(RateTestUtils.getEmptyRateList());
        PowerMockito.when(rateService.getAll(pageable)).thenReturn(mockedPage);

        ResponseEntity<List<RateDto>> responseEntity = rateController.getAll(pageable);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().size());
    }

    @Test
    public void testGetByIdOk() throws RateNotExistsException {
        when(rateService.getById(any())).thenReturn(RateTestUtils.getRateWithId());

        try {
            ResponseEntity<RateDto> responseEntity = rateController.getById(any());
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        } catch (RateNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = RateNotExistsException.class)
    public void testGetByIdException () throws RateNotExistsException {
        when(rateService.getById(any())).thenThrow(RateNotExistsException.class);
        ResponseEntity<RateDto> responseEntity = rateController.getById(any());
    }

    @Test
    public void testDeleteRateOk() throws RateNotExistsException {
        doNothing().when(rateService).deleteById(any());

        try {
            ResponseEntity responseEntity = rateController.deleteRate(1);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            verify(rateService,times(1)).deleteById(1);
        } catch (RateNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = RateNotExistsException.class)
    public void testDeleteRateException() throws RateNotExistsException{
        doThrow(new RateNotExistsException()).when(rateService).deleteById(any());
        ResponseEntity responseEntity = rateController.deleteRate(1);
    }

    @Test
    public void testEditRateOk() throws RateNotExistsException {
        when(rateService.editRate(RateTestUtils.getRateWithoutId(), 1)).thenReturn(RateTestUtils.getRateWithId());

        try {
            ResponseEntity responseEntity = rateController.editRate(RateTestUtils.getRateDto(),1);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        } catch (RateNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = RateNotExistsException.class)
    public void testEditRateException() throws RateNotExistsException{
        when(rateService.editRate(any(), any())).thenThrow(RateNotExistsException.class);
        ResponseEntity responseEntity = rateController.editRate(RateTestUtils.getRateDto(),1);
    }
}
