package com.flow.payflow.service.impl;

import com.flow.payflow.entity.BillingAddress;
import com.flow.payflow.repository.BillingAddressRepository;
import com.flow.payflow.service.BillingAddressService;
import org.springframework.stereotype.Service;

@Service
public class BillingAddressServiceImpl implements BillingAddressService {

    private final BillingAddressRepository billingAddressRepository;

    public BillingAddressServiceImpl(BillingAddressRepository billingAddressRepository) {
        this.billingAddressRepository = billingAddressRepository;
    }

    @Override
    public BillingAddress save(BillingAddress billingAddress) {
        return billingAddressRepository.save(billingAddress);
    }
}
