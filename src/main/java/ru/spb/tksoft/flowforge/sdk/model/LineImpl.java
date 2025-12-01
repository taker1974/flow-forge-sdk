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

import ru.spb.tksoft.utils.log.LogEx;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spb.tksoft.common.exceptions.NullArgumentException;
import ru.spb.tksoft.flowforge.sdk.contract.Block;
import ru.spb.tksoft.flowforge.sdk.contract.Line;
import ru.spb.tksoft.flowforge.sdk.enumeration.LineState;
import ru.spb.tksoft.common.exceptions.ConfigurationMismatchException;

/**
 * Line implementation.
 * 
 * Subclassing is not allowed.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public final class LineImpl implements Line {

    private static final Logger log = LoggerFactory.getLogger(LineImpl.class);

    @NotNull
    private final String internalLineId;

    @NotNull
    private final String blockFromInternalId;

    @NotNull
    private final String blockToInternalId;

    @NotNull
    private Block blockFrom;

    @NotNull
    private Block blockTo;

    @NotNull
    private volatile LineState state = LineState.OFF;

    /**
     * Get the log text.
     * 
     * @param message - the message.
     * @return the log text.
     */
    protected String getLogText(final @NotNull String message) {

        return String.format("%s [%s]: %s",
                getClass().getSimpleName(), internalLineId, message);
    }

    private volatile boolean modified;

    /**
     * Constructor.
     * 
     * @param internalLineId - the internal line id.
     * @param blockFromInternalId - the block from internal id.
     * @param blockToInternalId - the block to internal id.
     * @throws NullArgumentException if internalLineId, blockFromInternalId, blockToInternalId is
     *         null or blank.
     */
    public LineImpl(final String internalLineId,
            final String blockFromInternalId, final String blockToInternalId) {

        if (internalLineId == null || blockFromInternalId == null || blockToInternalId == null) {
            throw new NullArgumentException(
                    "internalLineId, blockFromInternalId, blockToInternalId must not be null");
        }

        if (internalLineId.isBlank() || blockFromInternalId.isBlank()
                || blockToInternalId.isBlank()) {
            throw new NullArgumentException(
                    "internalLineId, blockFromInternalId, blockToInternalId must not be blank");
        }

        this.internalLineId = internalLineId;

        this.blockFromInternalId = blockFromInternalId;
        this.blockToInternalId = blockToInternalId;

        this.state = LineState.OFF;

        // Set the modified flag to true to force the initial state to be modified.
        this.modified = true;
    }

    /**
     * Resolve the blocks.
     * 
     * Call it only once. Call it before resolving lines.
     * 
     * @param blocks - the blocks.
     */
    public synchronized void resolveBlocks(final List<Block> blocks) {

        if (blocks == null) {
            throw new NullArgumentException("blocks must not be null");
        }

        if (blockFrom != null || blockTo != null) {
            throw new ConfigurationMismatchException(
                    getLogText("blockFrom or blockTo must not be already resolved"));
        }

        this.blockFrom = blocks.stream()
                .filter(Objects::nonNull)
                .filter(block -> block.getInternalBlockId().equals(blockFromInternalId))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NullArgumentException(
                        getLogText("blockFrom not found")));

        this.blockTo = blocks.stream()
                .filter(Objects::nonNull)
                .filter(block -> block.getInternalBlockId().equals(blockToInternalId))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NullArgumentException(
                        getLogText("blockTo not found")));
    }

    /**
     * Set the modified flag.
     */
    @Override
    public void setModified() {
        this.modified = true;
    }

    /**
     * Check if the modified flag is set.
     * 
     * @return true if the modified flag is set, false otherwise.
     */
    @Override
    public boolean isModified() {
        return modified;
    }

    /**
     * Reset the modified flag.
     */
    @Override
    public void resetModified() {
        this.modified = false;
    }

    /**
     * Get the internal line id.
     * 
     * @return the internal line id.
     */
    @Override
    @NotNull
    public String getInternalLineId() {
        return internalLineId;
    }

    /**
     * Get the block from.
     * 
     * Reference to the block from set during the resolving of blocks and then not changed. So it's
     * safe to return it without synchronization.
     * 
     * @return the block from.
     */
    @Override
    @NotNull
    public Block getBlockFrom() {
        return blockFrom;
    }

    /**
     * Get the block to.
     * 
     * Reference to the block to set during the resolving of blocks and then not changed. So it's
     * safe to return it without synchronization.
     * 
     * @return the block to.
     */
    @Override
    @NotNull
    public Block getBlockTo() {
        return blockTo;
    }

    /**
     * Get the line state.
     * 
     * Synchronized to match the synchronization on "setState". sonarqube(java:S2886)
     * 
     * @return the line state.
     */
    @Override
    @NotNull
    public synchronized LineState getState() {
        return state;
    }

    /**
     * Set the line state.
     * 
     * @param state - the line state.
     */
    @Override
    public synchronized void setState(final LineState state) {

        if (state == null) {
            throw new NullArgumentException(getLogText("state must not be null"));
        }

        if (this.state != state) {
            setModified();
        }

        this.state = state;
        LogEx.info(log, LogEx.me(), getLogText("state changed to " + state));
    }

    /**
     * Reset the line.
     */
    @Override
    public synchronized void reset() {

        setState(LineState.OFF);

        // Ensure the line is marked as modified.
        setModified();

        LogEx.info(log, LogEx.me(), getLogText("reset completed"));
    }

    /**
     * Get the result text from previous block.
     */
    @Override
    @NotNull
    public String getResultText() {

        if (blockFrom != null) {
            return blockFrom.getResultText();
        }
        return "";
    }
}
