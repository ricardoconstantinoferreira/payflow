package com.flow.payflow.usecase;

import com.flow.payflow.entity.Blockade;
import com.flow.payflow.entity.BlockadeAssistent;
import com.flow.payflow.service.BlockadeAssistentService;
import com.flow.payflow.service.BlockadeService;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class VelocityCheck {

    private final BlockadeAssistentService blockadeAssistentService;
    private final BlockadeService blockadeService;

    public VelocityCheck(
            BlockadeAssistentService blockadeAssistentService,
            BlockadeService blockadeService
    ) {
        this.blockadeAssistentService = blockadeAssistentService;
        this.blockadeService = blockadeService;
    }

    public boolean check(String card, String token) {
        BlockadeAssistent blockadeAssistent = new BlockadeAssistent();
        BlockadeAssistent blockadeAssistent1 = blockadeAssistentService.getByCard(card);

        if (blockadeAssistent1 == null) {
            blockadeAssistent.setCard(card);
            blockadeAssistent.setCounter(1);
            blockadeAssistent.setCreatedAt(OffsetDateTime.now());
            blockadeAssistentService.save(blockadeAssistent);
            return true;
        } else {
            BlockadeAssistent blockadeAssistent2 = setQtyBlockadeAssistent(blockadeAssistent1);

            if (!compareDate(token, blockadeAssistent2)) {
                return !(blockadeAssistent2.getCounter() > blockadeService.getBlockadeConfig(token).getQty());
            } else {
                setCurrentDate(blockadeAssistent2);
                return true;
            }
        }
    }

    private BlockadeAssistent setCurrentDate(BlockadeAssistent blockadeAssistent) {
        blockadeAssistent.setCreatedAt(OffsetDateTime.now());
        return blockadeAssistentService.save(blockadeAssistent);
    }

    private BlockadeAssistent setQtyBlockadeAssistent(BlockadeAssistent blockadeAssistent) {
        blockadeAssistent.setCounter(blockadeAssistent.getCounter() + 1);
        return blockadeAssistentService.save(blockadeAssistent);
    }

    private boolean compareDate(String token, BlockadeAssistent blockadeAssistent) {
        Blockade blockade = blockadeService.getBlockadeConfig(token);
        Long parameter = blockade.getParameter();
        OffsetDateTime newDate = blockadeAssistent.getCreatedAt().plusMinutes(parameter);
        OffsetDateTime today = OffsetDateTime.now();

        return (today.isAfter(newDate));
    }
}