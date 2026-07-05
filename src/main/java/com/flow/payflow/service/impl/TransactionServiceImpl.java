package com.flow.payflow.service.impl;

import com.flow.payflow.dto.TransactionDto;
import com.flow.payflow.dto.TransactionResponse;
import com.flow.payflow.entity.*;
import com.flow.payflow.repository.TransactionRepository;
import com.flow.payflow.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final CustomerService customerService;
    private final BillingAddressService billingAddressService;
    private final FeesService feesService;
    private final StoreService storeService;
    private final InstallmentsService installmentsService;

    @Autowired
    public TransactionServiceImpl(
            TransactionRepository repository,
            CustomerService customerService,
            BillingAddressService billingAddressService,
            FeesService feesService,
            StoreService storeService,
            InstallmentsService installmentsService
    ) {
        this.repository = repository;
        this.customerService = customerService;
        this.billingAddressService = billingAddressService;
        this.feesService = feesService;
        this.storeService = storeService;
        this.installmentsService = installmentsService;
    }

    @Override
    public TransactionResponse create(TransactionDto request) {
        Customer customer = customerService.save(request.getCustomer());
        BillingAddress billingAddress = billingAddressService.save(request.getBillingAddress());

        Transaction t = new Transaction();

        t.setOrderId(request.getOrderId());
        t.setAmount(request.getAmount());
        t.setCurrency(request.getCurrency());
        t.setInstallments(request.getInstallments());
        t.setPaymentMethod(request.getPaymentMethod());
        t.setCardToken(request.getCardToken());
        t.setCustomer(customer);
        t.setBillingAddress(billingAddress);
        t.setStatus(Status.PENDING);
        t.setCreatedAt(OffsetDateTime.now());

        Float amountTotal = installmentsService.getCalcAmountTotal(t, request.getAuthToken());
        t.setAmountTotal(amountTotal);

        Transaction saved = repository.save(t);
        addFeesTransaction(saved, request.getAuthToken());

        return new TransactionResponse(
                saved.getId(),
                saved.getOrderId(),
                saved.getAmount(),
                saved.getCurrency(),
                saved.getInstallments(),
                saved.getPaymentMethod(),
                saved.getCardToken(),
                saved.getAmountTotal()
        );
    }

    @Override
    public void updateStatus(Long id, Status status) {
        Transaction t = repository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found: " + id));
        t.setStatus(status);
        repository.save(t);
    }

    @Override
    public TransactionResponse getById(Long id) {
        Transaction t = repository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found: " + id));
        return new TransactionResponse(t.getId(), t.getOrderId(), t.getAmount(), t.getCurrency(),
                t.getInstallments(), t.getPaymentMethod(), t.getCardToken(), t.getAmountTotal());
    }

    @Override
    public Page<TransactionResponse> list(Pageable pageable) {
        Page<Transaction> page = repository.findAll(pageable);
        List<TransactionResponse> content = page.getContent().stream()
                .map(t -> new TransactionResponse(t.getId(), t.getOrderId(), t.getAmount(),
                        t.getCurrency(), t.getInstallments(), t.getPaymentMethod(), t.getCardToken(),
                        t.getAmountTotal()))
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    @Override
    public Transaction getByCardToken(String token) {
        Transaction transaction = repository.getByCardToken(token).orElseThrow(() -> new RuntimeException("Transaction not found: " + token));
        return transaction;
    }

    private void addFeesTransaction(Transaction transaction, String token) {
        Store store = storeService.getStoreByToken(token);

        Fees fees = new Fees();
        fees.setDescriptionFees(transaction.getInstallments());
        fees.setTransaction(transaction);
        fees.setStore(store);

        feesService.save(fees);
    }
}
