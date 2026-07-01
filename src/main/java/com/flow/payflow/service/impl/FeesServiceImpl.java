package com.flow.payflow.service.impl;

import com.flow.payflow.entity.Fees;
import com.flow.payflow.repository.FeesRepository;
import com.flow.payflow.service.FeesService;
import org.springframework.stereotype.Service;

@Service
public class FeesServiceImpl implements FeesService {

    private final FeesRepository feesRepository;

    public FeesServiceImpl(FeesRepository feesRepository) {
        this.feesRepository = feesRepository;
    }

    @Override
    public Fees save(Fees fees) {
        return feesRepository.save(fees);
    }
}
