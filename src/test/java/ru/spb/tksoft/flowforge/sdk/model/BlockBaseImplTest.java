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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.spb.tksoft.common.exceptions.ConfigurationMismatchException;
import ru.spb.tksoft.common.exceptions.NullArgumentException;
import ru.spb.tksoft.flowforge.sdk.contract.Block;
import ru.spb.tksoft.flowforge.sdk.contract.Line;
import ru.spb.tksoft.flowforge.sdk.contract.LineJunction;
import ru.spb.tksoft.flowforge.sdk.contract.RunnableStateChangeListener;
import ru.spb.tksoft.flowforge.sdk.enumeration.LineState;
import ru.spb.tksoft.flowforge.sdk.enumeration.RunnableState;

/**
 * Tests for BlockBaseImpl.
 *
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
class BlockBaseImplTest {

    private static final String BLOCK_ID = "block1";
    private static final String BLOCK_TYPE_ID = "type1";
    private static final String DEFAULT_INPUT_TEXT = "default input";

    private TestBlock block;

    /**
     * Test implementation of BlockBaseImpl.
     */
    private static class TestBlock extends BlockBaseImpl {

        TestBlock(String blockTypeId, String internalBlockId, String defaultInputText) {
            super(blockTypeId, internalBlockId, defaultInputText);
        }
    }

    @BeforeEach
    void setUp() {
        block = new TestBlock(BLOCK_TYPE_ID, BLOCK_ID, DEFAULT_INPUT_TEXT);
    }

    @Test
    void testConstructor() {
        assertThat(block.getBlockTypeId()).isEqualTo(BLOCK_TYPE_ID);
        assertThat(block.getInternalBlockId()).isEqualTo(BLOCK_ID);
        assertThat(block.getDefaultInputText()).isEqualTo(DEFAULT_INPUT_TEXT);
        assertThat(block.getState()).isEqualTo(RunnableState.READY);
        assertThat(block.isModified()).isTrue();
        assertThat(block.hasError()).isFalse();
        assertThat(block.getErrorMessage()).isEmpty();
    }

    @Test
    void testConstructorWithNullInternalBlockId() {
        assertThatThrownBy(() -> new TestBlock(BLOCK_TYPE_ID, null, DEFAULT_INPUT_TEXT))
                .isInstanceOf(NullArgumentException.class)
                .hasMessageContaining(
                        "blockTypeId, internalBlockId, defaultInputText must not be null");
    }

    @Test
    void testConstructorWithBlankInternalBlockId() {
        assertThatThrownBy(() -> new TestBlock(BLOCK_TYPE_ID, "", DEFAULT_INPUT_TEXT))
                .isInstanceOf(NullArgumentException.class)
                .hasMessageContaining(
                        "blockTypeId, internalBlockId, defaultInputText must not be blank");
    }

    @Test
    void testConstructorWithNullBlockTypeId() {
        assertThatThrownBy(() -> new TestBlock(null, BLOCK_ID, DEFAULT_INPUT_TEXT))
                .isInstanceOf(NullArgumentException.class)
                .hasMessageContaining(
                        "blockTypeId, internalBlockId, defaultInputText must not be null");
    }

    @Test
    void testConstructorWithNullDefaultInputText() {
        assertThatThrownBy(() -> new TestBlock(BLOCK_TYPE_ID, BLOCK_ID, null))
                .isInstanceOf(NullArgumentException.class)
                .hasMessageContaining(
                        "blockTypeId, internalBlockId, defaultInputText must not be null");
    }

    @Test
    void testGetInputText() {
        assertThat(block.getInputText()).isEqualTo(DEFAULT_INPUT_TEXT);
    }

    @Test
    void testSetInputText() {
        String newInput = "new input";
        block.resetModified();
        block.setInputText(newInput);

        assertThat(block.getInputText()).isEqualTo(newInput);
        assertThat(block.isModified()).isTrue();
    }

    @Test
    void testSetInputTextWithNull() {
        assertThatThrownBy(() -> block.setInputText(null))
                .isInstanceOf(NullArgumentException.class);
        assertThat(block.hasError()).isTrue();
    }

    @Test
    void testSetInputTextWithSameValue() {
        block.setInputText("test");
        block.resetModified();
        block.setInputText("test");

        assertThat(block.isModified()).isFalse();
    }

    @Test
    void testGetInputTextWithBlank() {
        block.setInputText("   ");
        assertThat(block.getInputText()).isEqualTo(DEFAULT_INPUT_TEXT);
    }

    @Test
    void testGetResultText() {
        assertThat(block.getResultText()).isEmpty();
    }

    @Test
    void testGetInputJunction() {
        LineJunction junction = block.getInputJunction();
        assertThat(junction)
                .isNotNull()
                .satisfies(j -> assertThat(j.hasLines()).isFalse());
    }

    @Test
    void testGetOutputJunction() {
        LineJunction junction = block.getOutputJunction();
        assertThat(junction)
                .isNotNull()
                .satisfies(j -> assertThat(j.hasLines()).isFalse());
    }

    @Test
    void testResolveLines() {
        Block block2 = new TestBlock(BLOCK_TYPE_ID, "block2", DEFAULT_INPUT_TEXT);
        Line line1 = mock(Line.class);
        Line line2 = mock(Line.class);

        when(line1.getBlockFrom()).thenReturn(block);
        when(line1.getBlockTo()).thenReturn(block2);
        when(line2.getBlockFrom()).thenReturn(block2);
        when(line2.getBlockTo()).thenReturn(block);

        List<Line> lines = List.of(line1, line2);
        block.resolveLines(lines);

        assertThat(block.getOutputJunction().hasLines()).isTrue();
        assertThat(block.getInputJunction().hasLines()).isTrue();
    }

    @Test
    void testResolveLinesTwice() {
        Line line = mock(Line.class);
        when(line.getBlockFrom()).thenReturn(block);
        when(line.getBlockTo()).thenReturn(block);

        List<Line> lines = List.of(line);
        block.resolveLines(lines);

        assertThatThrownBy(() -> block.resolveLines(lines))
                .isInstanceOf(ConfigurationMismatchException.class)
                .hasMessageContaining("inputJunction or outputJunction must not have lines");
    }

    @Test
    void testResolveLinesWithNullLines() {
        List<Line> lines = new ArrayList<>();
        lines.add(null);
        lines.add(mock(Line.class));

        block.resolveLines(lines);
        // Should not throw
        assertThat(block).isNotNull();
    }

    @Test
    void testSetState() {
        RunnableStateChangeListener listener = mock(RunnableStateChangeListener.class);
        block.addStateChangeListener(listener);

        block.setState(RunnableState.RUNNING);

        assertThat(block.getState()).isEqualTo(RunnableState.RUNNING);
        assertThat(block.isModified()).isTrue();
        verify(listener).onStateChanged(any());
    }

    @Test
    void testSetStateWithNull() {
        assertThatThrownBy(() -> block.setState(null))
                .isInstanceOf(NullArgumentException.class);
        assertThat(block.hasError()).isTrue();
    }

    @Test
    void testSetStateDoesNotModifyIfSameState() {
        block.resetModified();
        block.setState(RunnableState.READY);

        assertThat(block.isModified()).isFalse();
    }

    @Test
    void testStop() {
        block.stop();

        assertThat(block.getState()).isEqualTo(RunnableState.STOPPED);
        assertThat(block.isModified()).isTrue();
    }

    @Test
    void testAbort() {
        block.abort();

        assertThat(block.getState()).isEqualTo(RunnableState.ABORTED);
        assertThat(block.isModified()).isTrue();
    }

    @Test
    void testSetReady() {
        block.setState(RunnableState.DONE);
        block.setReady();

        assertThat(block.getState()).isEqualTo(RunnableState.READY);
    }

    @Test
    void testSetReadyWithError() {
        block.setState(RunnableState.DONE);
        block.setError(true, "error message");

        assertThatThrownBy(() -> block.setReady())
                .isInstanceOf(ConfigurationMismatchException.class);
    }

    @Test
    void testSetReadyFromInvalidState() {
        block.setState(RunnableState.RUNNING);
        block.setReady();

        // Should not change state
        assertThat(block.getState()).isEqualTo(RunnableState.RUNNING);
    }

    @Test
    void testReset() {
        block.setInputText("input");
        block.setState(RunnableState.DONE);
        block.getOutputJunction().setState(LineState.ON);
        block.reset();

        assertThat(block.getState()).isEqualTo(RunnableState.READY);
        // getInputText() returns defaultInputText if inputText is blank
        assertThat(block.getInputText()).isEqualTo(DEFAULT_INPUT_TEXT);
        assertThat(block.getResultText()).isEmpty();
        assertThat(block.hasError()).isFalse();
        assertThat(block.getOutputJunction().getState()).isEqualTo(LineState.OFF);
        assertThat(block.isModified()).isTrue();
    }

    @Test
    void testRunFromNotConfigured() {
        TestBlock notConfiguredBlock = new TestBlock(BLOCK_TYPE_ID, "block2", DEFAULT_INPUT_TEXT) {
            @Override
            protected void setState(RunnableState state) {
                if (state == RunnableState.READY) {
                    super.setState(RunnableState.NOT_CONFIGURED);
                } else {
                    super.setState(state);
                }
            }
        };
        notConfiguredBlock.setState(RunnableState.NOT_CONFIGURED);

        assertThatThrownBy(notConfiguredBlock::run)
                .isInstanceOf(ConfigurationMismatchException.class);
    }

    @Test
    void testRunFromReady() {
        block.run();

        assertThat(block.getState()).isEqualTo(RunnableState.RUNNING);
    }

    @Test
    void testGoFurtherNormal() {
        block.goFurtherNormal();

        assertThat(block.getInputJunction().getState()).isEqualTo(LineState.OFF);
        assertThat(block.getOutputJunction().getState()).isEqualTo(LineState.ON);
    }

    @Test
    void testSetModified() {
        block.resetModified();
        block.setModified();

        assertThat(block.isModified()).isTrue();
    }

    @Test
    void testResetModified() {
        block.resetModified();

        assertThat(block.isModified()).isFalse();
    }

    @Test
    void testAddAndRemoveStateChangeListener() {
        RunnableStateChangeListener listener1 = mock(RunnableStateChangeListener.class);
        RunnableStateChangeListener listener2 = mock(RunnableStateChangeListener.class);

        block.addStateChangeListener(listener1);
        block.addStateChangeListener(listener2);
        block.setState(RunnableState.RUNNING);
        verify(listener1).onStateChanged(any());
        verify(listener2).onStateChanged(any());

        block.removeStateChangeListener(listener1);
        // Create a new block to test removal properly
        TestBlock block2 = new TestBlock(BLOCK_TYPE_ID, "block2", DEFAULT_INPUT_TEXT);
        RunnableStateChangeListener listener3 = mock(RunnableStateChangeListener.class);
        block2.addStateChangeListener(listener3);
        block2.setState(RunnableState.READY);
        verify(listener3).onStateChanged(any());
        block2.removeStateChangeListener(listener3);
        block2.setState(RunnableState.DONE);
        // Listener should not be called again after removal
        verify(listener3).onStateChanged(any()); // Only once from before removal
    }

    @Test
    void testGetPrintableState() {
        String printableState = block.getPrintableState();

        assertThat(printableState)
                .contains("Block Type ID: " + BLOCK_TYPE_ID)
                .contains("Internal Block ID: " + BLOCK_ID)
                .contains("Default Input Text: " + DEFAULT_INPUT_TEXT)
                .contains("Input Text: " + DEFAULT_INPUT_TEXT) // getInputText() returns
                                                               // defaultInputText if inputText is
                                                               // blank
                .contains("Result Text: ")
                .contains("State: " + RunnableState.READY)
                .contains("Has Error: false")
                .contains("Error Message: ")
                .contains("Modified: true");
    }

    @Test
    void testGetPrintableStateWithModifiedFields() {
        block.setInputText("custom input");
        block.setState(RunnableState.RUNNING);
        block.setError(true, "test error");
        block.resetModified();

        String printableState = block.getPrintableState();

        assertThat(printableState)
                .contains("Internal Block ID: " + BLOCK_ID)
                .contains("Block Type ID: " + BLOCK_TYPE_ID)
                .contains("Default Input Text: " + DEFAULT_INPUT_TEXT)
                .contains("Input Text: custom input")
                .contains("State: " + RunnableState.RUNNING)
                .contains("Has Error: true")
                .contains("Error Message: test error")
                .contains("Modified: false");
    }

    @Test
    void testGetPrintableStateWithResultText() throws Exception {
        // Use reflection to call protected setResultText method
        Method setResultTextMethod =
                BlockBaseImpl.class.getDeclaredMethod("setResultText", String.class);
        setResultTextMethod.setAccessible(true);
        setResultTextMethod.invoke(block, "test result");

        block.setState(RunnableState.DONE);
        block.resetModified();

        String printableState = block.getPrintableState();

        assertThat(printableState)
                .contains("Result Text: test result")
                .contains("State: " + RunnableState.DONE)
                .contains("Modified: false");
    }

    @Test
    void testGetPrintableStateWithBlankInputText() {
        // Set inputText to blank - getInputText() should return defaultInputText
        block.setInputText("   ");

        String printableState = block.getPrintableState();

        // getPrintableState() should use getInputText(), which returns defaultInputText
        // when inputText is blank, not the blank string itself
        assertThat(printableState)
                .contains("Input Text: " + DEFAULT_INPUT_TEXT)
                .doesNotContain("Input Text:    "); // Should not show blank spaces
    }
}

