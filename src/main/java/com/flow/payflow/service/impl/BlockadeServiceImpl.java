package com.flow.payflow.service.impl;

import com.flow.payflow.entity.Blockade;
import com.flow.payflow.entity.Store;
import com.flow.payflow.entity.TypeParameter;
import com.flow.payflow.repository.BlockadeRepository;
import com.flow.payflow.service.BlockadeService;
import com.flow.payflow.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
public class BlockadeServiceImpl implements BlockadeService {

    private final BlockadeRepository blockadeRepository;
    private final StoreService storeService;

    public BlockadeServiceImpl(
            BlockadeRepository blockadeRepository,
            StoreService storeService
    ) {
        this.blockadeRepository = blockadeRepository;
        this.storeService = storeService;
    }

    @Override
    public Blockade save(Blockade blockade, String token) {
        Store store = storeService.getStoreByToken(token);
        blockade.setStore(store);

        Blockade blockade1 = getByStoreId(store.getId());
        if (blockade1 != null) {
            blockade.setId(blockade1.getId());
        }

        if (blockade.getTypeParameter().equals(TypeParameter.HOURS)) {
            Long parameterHours = Duration.ofHours(blockade.getParameter()).toMinutes();
            blockade.setParameter(parameterHours);
        }

        if (blockade.getTypeParameter().equals(TypeParameter.DAYS)) {
            Long parameterDays = Duration.ofDays(blockade.getParameter()).toMinutes();
            blockade.setParameter(parameterDays);
        }

        log.info("Salvando blockade classe blockade.");
        return blockadeRepository.save(blockade);
    }

    @Override
    public Blockade getByStoreId(Long storeId) {
        log.info("Pegando a loja pelo id  classe blockade.");
        Optional<Blockade> blockade = blockadeRepository.findByStoreId(storeId);
        return (blockade.isEmpty()) ? null : blockade.get();
    }

    @Override
    public Blockade getBlockadeConfig(String token) {
        log.info("Pegando a loja pelo token classe blockade.");
        Store store = storeService.getStoreByToken(token);
        return getByStoreId(store.getId());
    }
}
