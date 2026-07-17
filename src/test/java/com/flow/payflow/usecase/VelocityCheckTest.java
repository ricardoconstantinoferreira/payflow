package com.flow.payflow.usecase;

import com.flow.payflow.entity.Blockade;
import com.flow.payflow.entity.BlockadeAssistent;
import com.flow.payflow.service.BlockadeAssistentService;
import com.flow.payflow.service.BlockadeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VelocityCheckTest {

    @Mock
    private BlockadeAssistentService blockadeAssistentService;

    @Mock
    private BlockadeService blockadeService;

    @InjectMocks
    private VelocityCheck velocityCheck;

    private final String card = "1111222233334444";
    private final String token = "token123";

    @Test
    void check_whenNoAssist_thenCreateAndReturnTrue() {
        when(blockadeAssistentService.getByCard(card)).thenReturn(null);
        when(blockadeAssistentService.save(any(BlockadeAssistent.class))).thenAnswer(inv -> inv.getArgument(0));

        boolean result = velocityCheck.check(card, token);

        assertTrue(result);
        verify(blockadeAssistentService).save(any(BlockadeAssistent.class));
    }

    @Test
    void check_whenCompareDateFalse_andCounterExceedsQty_returnsFalse() {
        BlockadeAssistent assist = new BlockadeAssistent();
        assist.setCard(card);
        assist.setCounter(5);
        assist.setCreatedAt(OffsetDateTime.now());

        when(blockadeAssistentService.getByCard(card)).thenReturn(assist);
        when(blockadeAssistentService.save(any(BlockadeAssistent.class))).thenAnswer(inv -> inv.getArgument(0));

        Blockade blockade = new Blockade();
        blockade.setParameter(60L); // 60 minutes window
        blockade.setQty(1L); // allowed qty
        when(blockadeService.getBlockadeConfig(token)).thenReturn(blockade);

        boolean result = velocityCheck.check(card, token);

        assertFalse(result);
    }

    @Test
    void check_whenCompareDateFalse_andCounterWithinQty_returnsTrue() {
        BlockadeAssistent assist = new BlockadeAssistent();
        assist.setCard(card);
        assist.setCounter(1);
        assist.setCreatedAt(OffsetDateTime.now());

        when(blockadeAssistentService.getByCard(card)).thenReturn(assist);
        when(blockadeAssistentService.save(any(BlockadeAssistent.class))).thenAnswer(inv -> inv.getArgument(0));

        Blockade blockade = new Blockade();
        blockade.setParameter(60L); // 60 minutes window
        blockade.setQty(5L); // allowed qty
        when(blockadeService.getBlockadeConfig(token)).thenReturn(blockade);

        boolean result = velocityCheck.check(card, token);

        assertTrue(result);
    }
}
