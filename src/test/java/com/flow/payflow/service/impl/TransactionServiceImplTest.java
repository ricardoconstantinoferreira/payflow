package com.flow.payflow.service.impl;

import com.flow.payflow.dto.TransactionDto;
import com.flow.payflow.dto.TransactionResponse;
import com.flow.payflow.entity.*;
import com.flow.payflow.exception.MessageException;
import com.flow.payflow.repository.TransactionRepository;
import com.flow.payflow.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private CustomerService customerService;

    @Mock
    private BillingAddressService billingAddressService;

    @Mock
    private FeesService feesService;

    @Mock
    private StoreService storeService;

    @Mock
    private InstallmentsService installmentsService;

    @InjectMocks
    private TransactionServiceImpl service;

    @Test
    void shouldPersistTransactionAddFees() {
        // Arrange
        TransactionDto dto = new TransactionDto();
        dto.setOrderId("ORD-1");
        dto.setAmount(100f);
        dto.setCurrency("BRL");
        dto.setInstallments(1);
        dto.setPaymentMethod("CARD");
        dto.setCardToken("card-token");
        dto.setAuthToken("store-token");

        Customer customer = new Customer();
        customer.setDocument("423143214312");
        customer.setEmail("customer@gmail.com");
        customer.setName("teste");
        customer.setExternalId("r2rewq3241432");

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setCity("Taboao");
        billingAddress.setCountry("BR");
        billingAddress.setNumber(12);
        billingAddress.setStreet("TESTE TESTE");
        billingAddress.setState("SP");
        billingAddress.setZipCode("90328099");

        dto.setCustomer(customer);
        dto.setBillingAddress(billingAddress);

        Customer savedCustomer = new Customer();
        BillingAddress savedAddress = new BillingAddress();

        // CORREÇÃO 2: Uso de any() para isolar o comportamento do mock
        when(customerService.save(any())).thenReturn(savedCustomer);
        when(billingAddressService.save(any())).thenReturn(savedAddress);

        Store mockStore = new Store();
        mockStore.setId(1L);
        mockStore.setDescription("Test Store");
        when(storeService.getStoreByToken("store-token")).thenReturn(mockStore);

        Transaction savedTransaction = new Transaction();
        savedTransaction.setId(123L);
        savedTransaction.setOrderId(dto.getOrderId());
        savedTransaction.setAmount(dto.getAmount());
        savedTransaction.setCurrency(dto.getCurrency());
        savedTransaction.setInstallments(dto.getInstallments());
        savedTransaction.setPaymentMethod(dto.getPaymentMethod());
        savedTransaction.setCardToken(dto.getCardToken());
        savedTransaction.setAmountTotal(100f);

        when(installmentsService.getCalcAmountTotal(any(Transaction.class), eq("store-token"))).thenReturn(100f);
        when(repository.save(any(Transaction.class))).thenReturn(savedTransaction);

        // CORREÇÃO 3: Garante que o feesService não quebre o fluxo caso precise retornar algo
        when(feesService.save(any(Fees.class))).thenReturn(new Fees());

        // Act
        TransactionResponse response = service.create(dto);

        // Assert
        assertNotNull(response);
        assertEquals(123L, response.id());
        assertEquals(dto.getOrderId(), response.orderId());
        assertEquals(dto.getAmount(), response.amount());

        ArgumentCaptor<Fees> feesCaptor = ArgumentCaptor.forClass(Fees.class);
        verify(feesService, times(1)).save(feesCaptor.capture());

        Fees passedFees = feesCaptor.getValue();
        assertNotNull(passedFees.getTransaction());
        assertEquals(123L, passedFees.getTransaction().getId());
    }


    @Test
    void getById_whenNotFound_shouldThrowMessageException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        MessageException ex = assertThrows(MessageException.class, () -> service.getById(1L));
        // MessageException stores the short code as the exception message and the human message in status
        assertEquals("Not_Found", ex.getMessage());
        assertTrue(ex.getStatus().contains("Transaction not found"));
    }

    @Test
    void updateStatus_shouldUpdateEntityAndSave() {
        Transaction t = new Transaction();
        t.setId(5L);
        t.setStatus(Status.PENDING);

        when(repository.findById(5L)).thenReturn(Optional.of(t));
        when(repository.save(any(Transaction.class))).thenReturn(t);

        service.updateStatus(5L, Status.PAID);

        ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(repository).save(txCaptor.capture());
        assertEquals(Status.PAID, txCaptor.getValue().getStatus());
    }
}
