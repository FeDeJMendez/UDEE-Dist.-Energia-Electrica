package edu.utn.udee.Udee.service.backoffice;

import edu.utn.udee.Udee.TestUtils.ClientTestUtils;
import edu.utn.udee.Udee.TestUtils.MeasurementTestUtils;
import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Client;
import edu.utn.udee.Udee.domain.Measurement;
import edu.utn.udee.Udee.exceptions.ClientNotExistsException;
import edu.utn.udee.Udee.exceptions.MeasurementNotExistsException;
import edu.utn.udee.Udee.repository.MeasurementRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
public class MeasurementServiceTest {

    private MeasurementService measurementService;

    private MeasurementRepository measurementRepository;

    @Before
    public void setUp(){
        this.measurementRepository = mock(MeasurementRepository.class);

        this.measurementService = new MeasurementService(measurementRepository);
    }

    @Test
    public void testAddMeasurementOk(){
        when(measurementRepository.save(any())).thenReturn(MeasurementTestUtils.getUnbilledMeasurement());
        Measurement measurement = measurementService.addMeasurement(MeasurementTestUtils.getNewMeasurement());
        assertEquals(MeasurementTestUtils.getUnbilledMeasurement(), measurement);
    }

    @Test
    public void testGetAll(){
        PageRequest pageable = PageRequest.of(1, 10);
        Page<Measurement> mockedPage = mock(Page.class);
        PowerMockito.when(mockedPage.getContent()).thenReturn(MeasurementTestUtils.getMeasurementUnbilledList());
        PowerMockito.when(measurementRepository.findAll(pageable)).thenReturn(mockedPage);

        Page<Measurement> measurements = measurementService.getAll(pageable);

        verify(measurementRepository, times(1)).findAll(pageable);
        assertEquals(4, measurements.getContent().size());
    }

    @Test
    public void testGetByIdOk() throws MeasurementNotExistsException  {
        when(measurementRepository.findById(any())).thenReturn(Optional.of(MeasurementTestUtils.getUnbilledMeasurement()));

        try {
            Measurement measurement = measurementService.getById(any());
            assertEquals(MeasurementTestUtils.getUnbilledMeasurement(), measurement);
        } catch (MeasurementNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = MeasurementNotExistsException.class)
    public void testGetByIdException() throws MeasurementNotExistsException {
        Measurement measurement = measurementService.getById(any());
    }

    @Test
    public void testGetByMeterAndDateTimeRangeOk() {
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        when(measurementRepository.findByMeterAndDateTimeRange(1, begin, end)).thenReturn(MeasurementTestUtils.getMeasurementUnbilledList());
        List<Measurement> measurementList = measurementService.getByMeterAndDateTimeRange(1, begin, end);
        assertEquals(MeasurementTestUtils.getMeasurementUnbilledList(), measurementList);
    }

    @Test
    public void testGetUnbilledMeasurements() {
        when(measurementRepository.findUnbilledMeasurements(1)).thenReturn(MeasurementTestUtils.getMeasurementUnbilledList());
        List<Measurement> measurementList = measurementService.getUnbilledMeasurements(1);
        assertEquals(MeasurementTestUtils.getMeasurementUnbilledList(), measurementList);
    }

    @Test
    public void testDeleteByIdOk() throws MeasurementNotExistsException {
        when(measurementRepository.existsById(any())).thenReturn(true);

        try {
            measurementService.deleteById(1);
            verify(measurementRepository, times(1)).deleteById(1);
        } catch (MeasurementNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = MeasurementNotExistsException.class)
    public void testDeleteByIdException() throws MeasurementNotExistsException {
        when(measurementRepository.existsById(any())).thenReturn(false);
        measurementService.deleteById(1);
    }
}
