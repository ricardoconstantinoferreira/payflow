package com.flow.payflow.controller;

import com.flow.payflow.dto.FeesConfigDto;
import com.flow.payflow.entity.FeesConfig;
import com.flow.payflow.mapper.FeesConfigMapper;
import com.flow.payflow.service.FeesConfigService;
import com.flow.payflow.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/transaction/store/config", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeesConfigController {

    private final FeesConfigService feesConfigService;
    private final FeesConfigMapper feesConfigMapper;

    public FeesConfigController(
            FeesConfigService feesConfigService,
            FeesConfigMapper feesConfigMapper
    ) {
        this.feesConfigService = feesConfigService;
        this.feesConfigMapper = feesConfigMapper;
    }

    @PostMapping
    public ResponseEntity<FeesConfig> save(@Valid @RequestBody FeesConfigDto dto) {
        FeesConfig feesConfig = feesConfigMapper.toEntity(dto);
        FeesConfig result = feesConfigService.save(feesConfig, dto.getStoreId());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeesConfig> update(@Valid @RequestBody FeesConfigDto dto, @PathVariable(value = "id") Long id) {
        FeesConfig feesConfig = feesConfigMapper.toEntity(dto);
        feesConfig.setId(id);

        FeesConfig result = feesConfigService.save(feesConfig, dto.getStoreId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    public ResponseEntity<FeesConfig> getByStoreId(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        return new ResponseEntity<>(feesConfigService.getByStoreByToken(token), HttpStatus.OK);
    }
}
