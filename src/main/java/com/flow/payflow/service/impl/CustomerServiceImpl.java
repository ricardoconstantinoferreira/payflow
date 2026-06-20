package com.flow.payflow.service.impl;

import com.flow.payflow.entity.Customer;
import com.flow.payflow.repository.CustomerRepository;
import com.flow.payflow.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
}
