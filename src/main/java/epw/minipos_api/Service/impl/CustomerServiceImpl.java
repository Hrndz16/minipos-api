package epw.minipos_api.Service.impl;

import epw.minipos_api.Entity.Customer;
import epw.minipos_api.Repository.CustomerRepository;
import epw.minipos_api.Service.CustomerService;
import epw.minipos_api.dto.CreateCustomerDto;
import epw.minipos_api.dto.UpdateCustomerDto;
import epw.minipos_api.exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> list() {
        return customerRepository.findAll()
                .stream()
                .sorted((a, b) -> Long.compare(a.getId(), b.getId()))
                .toList();
    }

    @Override
    public Customer create(CreateCustomerDto dto) {
        Customer customer = new Customer(
                dto.fullName().trim(),
                dto.email().trim().toLowerCase(),
                normalizePhone(dto.phone())
        );
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(long id, UpdateCustomerDto dto) {
        Customer current = findByIdOrThrow(id);

        if (dto.fullName() != null) {
            current.setFullName(dto.fullName().trim());
        }
        if (dto.email() != null) {
            current.setEmail(dto.email().trim().toLowerCase());
        }
        if (dto.phone() != null) {
            current.setPhone(normalizePhone(dto.phone()));
        }

        return customerRepository.save(current);
    }

    @Override
    public void remove(long id) {
        Customer current = findByIdOrThrow(id);
        customerRepository.delete(current);
    }

    private Customer findByIdOrThrow(long id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    private String normalizePhone(String phone) {
        if (phone == null) {
            return null;
        }
        String trimmed = phone.trim();
        return trimmed.isBlank() ? null : trimmed;
    }
}
