package epw.minipos_api.Service;

import epw.minipos_api.Entity.Customer;
import epw.minipos_api.dto.CreateCustomerDto;
import epw.minipos_api.dto.UpdateCustomerDto;

import java.util.List;

public interface CustomerService {
    List<Customer> list();

    Customer create(CreateCustomerDto dto);

    Customer update(long id, UpdateCustomerDto dto);

    void remove(long id);
}
