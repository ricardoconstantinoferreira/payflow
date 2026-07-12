package com.flow.payflow.service;

import com.flow.payflow.entity.BlockadeAssistent;

public interface BlockadeAssistentService {
    BlockadeAssistent save(BlockadeAssistent blockadeAssistent);
    BlockadeAssistent getByCard(String card);
}
