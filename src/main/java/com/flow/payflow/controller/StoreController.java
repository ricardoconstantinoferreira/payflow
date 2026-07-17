package com.flow.payflow.controller;

import com.flow.payflow.dto.StoreDto;
import com.flow.payflow.entity.Store;
import com.flow.payflow.mapper.StoreMapper;
import com.flow.payflow.service.StoreService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/payflow/store", produces = MediaType.APPLICATION_JSON_VALUE)
public class StoreController {

    private static final Logger log = LoggerFactory.getLogger(StoreController.class);
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
        log.info("Create store {}", response.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Store> update(@PathVariable(value = "id") Long id, @RequestBody StoreDto storeDto) {
        Store store = storeMapper.toEntity(storeDto);
        store.setId(id);
        Store response = storeService.create(store);
        log.info("Update Store {}", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> getById(@PathVariable(value = "id") Long id) {
        log.info("Get Store by Id");
        return new ResponseEntity<>(storeService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Store>> getAll() {
        log.info("Get Akk Stores");
        return new ResponseEntity<>(storeService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") Long id) {
        storeService.deleteById(id);
    }
}
