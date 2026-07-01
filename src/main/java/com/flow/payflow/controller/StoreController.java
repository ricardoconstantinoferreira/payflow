package com.flow.payflow.controller;

import com.flow.payflow.dto.StoreDto;
import com.flow.payflow.entity.Store;
import com.flow.payflow.mapper.StoreMapper;
import com.flow.payflow.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/transaction/store", produces = MediaType.APPLICATION_JSON_VALUE)
public class StoreController {

    private final StoreService storeService;
    private final StoreMapper storeMapper;

    public StoreController(StoreService storeService, StoreMapper storeMapper) {
        this.storeService = storeService;
        this.storeMapper = storeMapper;
    }

    @PostMapping
    public ResponseEntity<Store> create(@Valid @RequestBody StoreDto storeDto) {
        Store store = storeMapper.toEntity(storeDto);
        Store response = storeService.create(store);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
