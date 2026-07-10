package com.flow.payflow.controller;

import com.flow.payflow.dto.BlockadeDto;
import com.flow.payflow.entity.Blockade;
import com.flow.payflow.mapper.BlockadeMapper;
import com.flow.payflow.service.BlockadeService;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/transaction/blockade", produces = MediaType.APPLICATION_JSON_VALUE)
public class BlockadeController {

    private final BlockadeService blockadeService;
    private final BlockadeMapper blockadeMapper;

    public BlockadeController(
            BlockadeService blockadeService,
            BlockadeMapper blockadeMapper
    ) {
        this.blockadeService = blockadeService;
        this.blockadeMapper = blockadeMapper;
    }

    @PostMapping
    public ResponseEntity<Blockade> save(
            @RequestHeader("Authorization") String authorization,
            @RequestBody BlockadeDto blockadeDto
    ) {
        String token = null;
        if (authorization != null) {
            token = authorization.replace("Bearer ", "");
        }

        Blockade blockade = blockadeMapper.toEntity(blockadeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                blockadeService.save(blockade, token)
        );
    }
}
