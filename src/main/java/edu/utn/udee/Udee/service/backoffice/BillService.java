package edu.utn.udee.Udee.service.backoffice;

import edu.utn.udee.Udee.domain.*;
import edu.utn.udee.Udee.exceptions.BillNotExistsException;
import edu.utn.udee.Udee.exceptions.MeterNotExistsException;
import edu.utn.udee.Udee.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final MeasurementService measurementService;
    private final MeterService meterService;

    @Autowired
    public BillService(BillRepository billRepository, MeasurementService measurementService, MeterService meterService) {
        this.billRepository = billRepository;
        this.measurementService = measurementService;
        this.meterService = meterService;
    }

    public List<Bill> createAllBills(List<Meter> meterList) throws MeterNotExistsException {
        List<Bill> newBillList = new ArrayList<>();

        for (Meter meter: meterList) {
            List<Measurement> measurementList = measurementService.getUnbilledMeasurements(meter.getSerialNumber());
                if (measurementList.size() != 0){
                meterService.setBilledMeasurement(meter);
                Double totalMeasurement = measurementList.stream().mapToDouble(x -> x.getKwh()).sum();
                Bill newBill = Bill.builder().
                        dni(meter.getAddress().getClient().getDni()).
                        date(LocalDate.now()).
                        fullName(meter.getAddress().getClient().getName() + " " + meter.getAddress().getClient().getSurname()).
                        address(meter.getAddress().getAddress()).
                        city(meter.getAddress().getCity()).
                        meterSerialNumber(meter.getSerialNumber()).
                        firstMeasurement(measurementList.get(0).getKwh()).
                        lastMeasurement(measurementList.get(measurementList.size() - 1).getKwh()).
                        firstMeasurementDateTime(measurementList.get(0).getDateTime()).
                        lastMeasurementDateTime(measurementList.get(measurementList.size() - 1).getDateTime()).
                        totalMeasurementKwh(totalMeasurement).
                        rate(meter.getAddress().getRate().getDescription()).
                        totalAmount(totalMeasurement * meter.getAddress().getRate().getAmount()).
                        paid(false).
                        build();
                newBillList.add(newBill);
                billRepository.save(newBill);
            }
        }
        return newBillList;
    }

    public Page getAll(Pageable pageable) {
        return billRepository.findAll(pageable);
    }

    public Bill getById(Integer id)
            throws BillNotExistsException {
        return billRepository.findById(id).
                orElseThrow(BillNotExistsException::new);
    }

    public List<Bill> addressDebt(Address address) {
        List<Bill> unpaidBills = billRepository.findUnpaidByAddress(address.getAddress());
        return unpaidBills;
    }

    public List<Bill> clientDebt(Client client) {
        List<Address> addresses = client.getAddress();
        List<Bill> unpaidBills = new ArrayList<>();
        for (Address address : addresses){
            List<Bill> unpaidAddressBills = addressDebt(address);
            for (Bill bill : unpaidAddressBills){
                unpaidBills.add(bill);
            }
        }
        return unpaidBills;
    }

    public void deleteById(Integer id)
            throws BillNotExistsException {
        if (billRepository.existsById(id))
            billRepository.deleteById(id);
        else throw new BillNotExistsException();
    }
}
