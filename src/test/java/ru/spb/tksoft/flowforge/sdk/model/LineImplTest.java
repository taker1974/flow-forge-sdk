/*
 * Copyright 2025 Konstantin Terskikh
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package ru.spb.tksoft.flowforge.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.spb.tksoft.common.exception.ConfigurationMismatchException;
import ru.spb.tksoft.common.exception.NullArgumentException;
import ru.spb.tksoft.flowforge.sdk.contract.Block;
import ru.spb.tksoft.flowforge.sdk.enumeration.LineState;

/**
 * Tests for LineImpl.
 *
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
class LineImplTest {

    private static final String LINE_ID = "line1";
    private static final String BLOCK_FROM_ID = "block1";
    private static final String BLOCK_TO_ID = "block2";

    private LineImpl line;
    private Block blockFrom;
    private Block blockTo;

    @BeforeEach
    void setUp() {
        line = new LineImpl(LINE_ID, BLOCK_FROM_ID, BLOCK_TO_ID);
        blockFrom = mock(Block.class);
        blockTo = mock(Block.class);

        when(blockFrom.getInternalBlockId()).thenReturn(BLOCK_FROM_ID);
        when(blockTo.getInternalBlockId()).thenReturn(BLOCK_TO_ID);
    }

    @Test
    void testConstructor() {
        assertThat(line.getInternalLineId()).isEqualTo(LINE_ID);
        assertThat(line.isModified()).isTrue();
        assertThat(line.getState()).isEqualTo(LineState.OFF);
    }

    @Test
    void testConstructorWithNullInternalLineId() {
        assertThatThrownBy(() -> new LineImpl(null, BLOCK_FROM_ID, BLOCK_TO_ID))
                .isInstanceOf(NullArgumentException.class)
                .hasMessageContaining(
                        "internalLineId, blockFromInternalId, blockToInternalId must not be null");
    }

    @Test
    void testConstructorWithBlankInternalLineId() {
        assertThatThrownBy(() -> new LineImpl("", BLOCK_FROM_ID, BLOCK_TO_ID))
                .isInstanceOf(NullArgumentException.class)
                .hasMessageContaining(
                        "internalLineId, blockFromInternalId, blockToInternalId must not be blank");

        assertThatThrownBy(() -> new LineImpl("   ", BLOCK_FROM_ID, BLOCK_TO_ID))
                .isInstanceOf(NullArgumentException.class)
                .hasMessageContaining(
                        "internalLineId, blockFromInternalId, blockToInternalId must not be blank");
    }

    @Test
    void testConstructorWithNullBlockFromInternalId() {
        assertThatThrownBy(() -> new LineImpl(LINE_ID, null, BLOCK_TO_ID))
                .isInstanceOf(NullArgumentException.class)
                .hasMessageContaining(
                        "internalLineId, blockFromInternalId, blockToInternalId must not be null");
    }

    @Test
    void testConstructorWithNullBlockToInternalId() {
        assertThatThrownBy(() -> new LineImpl(LINE_ID, BLOCK_FROM_ID, null))
                .isInstanceOf(NullArgumentException.class)
                .hasMessageContaining(
                        "internalLineId, blockFromInternalId, blockToInternalId must not be null");
    }

    @Test
    void testResolveBlocks() {
        List<Block> blocks = Arrays.asList(blockFrom, blockTo);

        line.resolveBlocks(blocks);

        assertThat(line.getBlockFrom()).isEqualTo(blockFrom);
        assertThat(line.getBlockTo()).isEqualTo(blockTo);
    }

    @Test
    void testResolveBlocksWithNullList() {
        assertThatThrownBy(() -> line.resolveBlocks(null))
                .isInstanceOf(NullArgumentException.class)
                .hasMessageContaining("blocks must not be null");
    }

    @Test
    void testResolveBlocksTwice() {
        List<Block> blocks = Arrays.asList(blockFrom, blockTo);
        line.resolveBlocks(blocks);

        assertThatThrownBy(() -> line.resolveBlocks(blocks))
                .isInstanceOf(ConfigurationMismatchException.class)
                .hasMessageContaining("blockFrom or blockTo must not be already resolved");
    }

    @Test
    void testResolveBlocksWithMissingBlockFrom() {
        List<Block> blocks = Collections.singletonList(blockTo);

        assertThatThrownBy(() -> line.resolveBlocks(blocks))
                .isInstanceOf(NullArgumentException.class)
                .hasMessageContaining("blockFrom not found");
    }

    @Test
    void testResolveBlocksWithMissingBlockTo() {
        List<Block> blocks = Collections.singletonList(blockFrom);

        assertThatThrownBy(() -> line.resolveBlocks(blocks))
                .isInstanceOf(NullArgumentException.class)
                .hasMessageContaining("blockTo not found");
    }

    @Test
    void testResolveBlocksWithNullBlocksInList() {
        List<Block> blocks = Arrays.asList(null, blockFrom, null, blockTo);

        line.resolveBlocks(blocks);

        assertThat(line.getBlockFrom()).isEqualTo(blockFrom);
        assertThat(line.getBlockTo()).isEqualTo(blockTo);
    }

    @Test
    void testSetState() {
        line.setState(LineState.ON);

        assertThat(line.getState()).isEqualTo(LineState.ON);
        assertThat(line.isModified()).isTrue();
    }

    @Test
    void testSetStateWithNull() {
        assertThatThrownBy(() -> line.setState(null))
                .isInstanceOf(NullArgumentException.class)
                .hasMessageContaining("state must not be null");
    }

    @Test
    void testSetStateDoesNotModifyIfSameState() {
        line.resetModified();
        line.setState(LineState.OFF);

        assertThat(line.isModified()).isFalse();
    }

    @Test
    void testSetModified() {
        line.resetModified();
        line.setModified();

        assertThat(line.isModified()).isTrue();
    }

    @Test
    void testResetModified() {
        line.resetModified();

        assertThat(line.isModified()).isFalse();
    }

    @Test
    void testReset() {
        line.setState(LineState.ON);
        line.resetModified();
        line.reset();

        assertThat(line.getState()).isEqualTo(LineState.OFF);
        assertThat(line.isModified()).isTrue();
    }

    @Test
    void testGetResultText() {
        List<Block> blocks = Arrays.asList(blockFrom, blockTo);
        line.resolveBlocks(blocks);

        when(blockFrom.getResultText()).thenReturn("result text");

        assertThat(line.getResultText()).isEqualTo("result text");
    }

    @Test
    void testGetResultTextWhenBlockFromIsNull() {
        assertThat(line.getResultText()).isEmpty();
    }
}

