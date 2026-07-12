package com.flow.payflow.service.impl;

import com.flow.payflow.entity.BlockadeAssistent;
import com.flow.payflow.repository.BlockadeAssistentRepository;
import com.flow.payflow.service.BlockadeAssistentService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlockadeAssistentServiceImpl implements BlockadeAssistentService {

    private BlockadeAssistentRepository blockadeAssistentRepository;

    public BlockadeAssistentServiceImpl(BlockadeAssistentRepository blockadeAssistentRepository) {
        this.blockadeAssistentRepository = blockadeAssistentRepository;
    }

    @Override
    public BlockadeAssistent save(BlockadeAssistent blockadeAssistent) {
        return blockadeAssistentRepository.save(blockadeAssistent);
    }

    @Override
    public BlockadeAssistent getByCard(String card) {
        Optional<BlockadeAssistent> blockadeAssistent = blockadeAssistentRepository.findByCard(card);
        return (blockadeAssistent.isEmpty()) ? null : blockadeAssistent.get();
    }
}
