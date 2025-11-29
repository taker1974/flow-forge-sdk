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

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.common.exception.ConfigurationMismatchException;
import ru.spb.tksoft.common.exception.NullArgumentException;
import ru.spb.tksoft.flowforge.sdk.contract.Block;
import ru.spb.tksoft.flowforge.sdk.contract.BlockPlugin;
import ru.spb.tksoft.flowforge.sdk.contract.Line;
import ru.spb.tksoft.flowforge.sdk.contract.LineJunction;
import ru.spb.tksoft.flowforge.sdk.contract.RunnableStateChangeListener;
import ru.spb.tksoft.flowforge.sdk.enumeration.LineState;
import ru.spb.tksoft.flowforge.sdk.enumeration.RunnableState;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Block implementation. Base class for all blocks.
 * 
 * Open for subclassing.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public abstract class BlockBaseImpl implements Block {

    private static final Logger log = LoggerFactory.getLogger(BlockBaseImpl.class);

    protected static final String NL = System.lineSeparator();

    @NotNull
    private final String internalBlockId;

    @NotNull
    private final String blockTypeId;

    @NotNull
    private final String defaultInputText;

    @NotNull
    private String inputText = "";

    @NotNull
    private String resultText = "";

    @NotNull
    private final LineJunction inputJunction = new LineJunctionImpl();

    @NotNull
    private final LineJunction outputJunction = new LineJunctionImpl();

    @NotNull
    private volatile RunnableState state = RunnableState.NOT_CONFIGURED;

    private volatile boolean hasError = false;

    @NotNull
    private String errorMessage = "";

    private volatile boolean modified;

    /**
     * Get the block type id from the @BlockPlugin annotation.
     * 
     * @param clazz - the class to get the annotation from.
     * @return the block type id from the annotation.
     * @throws ConfigurationMismatchException if the annotation is not present.
     */
    protected static String getBlockTypeIdFromAnnotation(Class<?> clazz) {
        BlockPlugin annotation = clazz.getAnnotation(BlockPlugin.class);
        if (annotation == null) {
            throw new ConfigurationMismatchException(
                    "Class " + clazz.getName() + " must be annotated with @BlockPlugin");
        }
        return annotation.blockTypeId();
    }

    @SuppressWarnings("java:S125")
    // CHECKSTYLE:OFF
    // @formatter:off
    /*
     * Put this code in descendants.
     * 
     * // Define the block type id for caching. 
     * private static final String BLOCK_TYPE_ID =
     *     getBlockTypeIdFromAnnotation(ChatGPT.class);
     * 
     * // Implement the getBlockTypeId() method.
     * @Override 
     * public String getBlockTypeId() { 
     *     return BLOCK_TYPE_ID; 
     * }
     */
    // @formatter:on
    // CHECKSTYLE:ON

    /**
     * Set the error.
     * 
     * @param hasError - the error flag.
     * @param errorMessage - the error message.
     */
    protected synchronized void setError(final boolean hasError, final String errorMessage) {

        if (this.hasError != hasError) {
            setModified();
        }

        this.hasError = hasError;
        this.errorMessage = errorMessage;
    }

    /**
     * Get the log text.
     * 
     * @param message - the message.
     * @return the log text.
     */
    protected String getLogText(final @NotNull String message) {

        return String.format("%s [%s]: %s",
                getClass().getSimpleName(), internalBlockId, message);
    }

    /**
     * Constructor.
     * 
     * @param internalBlockId - the internal block id.
     * @param blockTypeId - the block type id.
     * @param defaultInputText - the default input text.
     */
    protected BlockBaseImpl(final String internalBlockId,
            final String blockTypeId, final String defaultInputText) {

        if (internalBlockId == null || blockTypeId == null || defaultInputText == null) {
            throw new NullArgumentException(
                    "internalBlockId, blockTypeId, defaultInputText must not be null");
        }

        if (internalBlockId.isBlank() || blockTypeId.isBlank() || defaultInputText.isBlank()) {
            throw new NullArgumentException(
                    "internalBlockId, blockTypeId, defaultInputText must not be blank");
        }

        this.internalBlockId = internalBlockId;
        this.blockTypeId = blockTypeId;
        this.defaultInputText = defaultInputText;

        // Set the modified flag to true to force the initial state to be modified.
        this.modified = true;

        this.state = RunnableState.READY;
    }

    /**
     * Resolve the lines.
     * 
     * Call it only once. Call it after resolving blocks.
     * 
     * @param lines - the lines.
     */
    public synchronized void resolveLines(final @NotNull List<Line> lines) {

        if (inputJunction.hasLines() || outputJunction.hasLines()) {
            throw new ConfigurationMismatchException(
                    getLogText("inputJunction or outputJunction must not have lines"));
        }

        // Line diagram:
        // blockFrom --------> blockTo
        // equivalent to block diagram:
        // outputJunction ---> inputJunction
        lines.stream().filter(Objects::nonNull)
                .filter(line -> line.getBlockFrom() != null && line.getBlockTo() != null)
                .forEach(line -> {
                    // If "me" is the 'block from' for line,
                    // then add the line to the output junction.
                    if (line.getBlockFrom().getInternalBlockId().equals(internalBlockId)) {
                        outputJunction.addLine(line);
                    }
                    // If "me" is the 'block to' for line,
                    // then add the line to the input junction.
                    if (line.getBlockTo().getInternalBlockId().equals(internalBlockId)) {
                        inputJunction.addLine(line);
                    }
                });
    }

    /**
     * Get the internal block id.
     * 
     * @return the internal block id.
     */
    @Override
    public String getInternalBlockId() {
        return internalBlockId;
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
     * Get the block type id.
     * 
     * @return the block type id.
     */
    @Override
    @NotNull
    public String getBlockTypeId() {
        return blockTypeId;
    }

    /**
     * Get the default input text.
     * 
     * @return the default input text.
     */
    @Override
    @NotNull
    public String getDefaultInputText() {
        return defaultInputText;
    }

    /**
     * Set the input text.
     * 
     * @param inputText - the input text.
     */
    @Override
    public synchronized void setInputText(final String inputText) {

        if (inputText == null) {
            setError(true, "Input text is null");
            throw new NullArgumentException(getLogText(getErrorMessage()));
        }

        if (!this.inputText.equals(inputText)) {
            setModified();
        }

        this.inputText = inputText;
        LogEx.info(log, LogEx.me(), getLogText("inputText changed"));
    }

    /**
     * Get the input text.
     * 
     * @return the input text.
     */
    @Override
    @NotNull
    public synchronized String getInputText() {
        return inputText == null || inputText.isBlank() ? getDefaultInputText() : inputText;
    }

    /**
     * Set the result text.
     * 
     * @param resultText - the result text.
     */
    protected synchronized void setResultText(final String resultText) {

        if (resultText == null) {
            setError(true, "Result text is null");
            throw new NullArgumentException(getLogText(getErrorMessage()));
        }

        if (!this.resultText.equals(resultText)) {
            setModified();
        }

        this.resultText = resultText;
        LogEx.info(log, LogEx.me(), getLogText("resultText changed"));
    }

    /**
     * Get the result text.
     * 
     * @return the result text.
     */
    @Override
    @NotNull
    public synchronized String getResultText() {
        return resultText;
    }

    /**
     * Get the input junction.
     * 
     * @return the input junction.
     */
    @Override
    @NotNull
    public LineJunction getInputJunction() {
        return inputJunction;
    }

    /**
     * Get the output junction.
     * 
     * @return the output junction.
     */
    @Override
    @NotNull
    public LineJunction getOutputJunction() {
        return outputJunction;
    }

    // Successors might be interested in the state changes, so we provide a way to add and remove
    // listeners. We use a CopyOnWriteArrayList to ensure thread safety.
    private final List<RunnableStateChangeListener> stateChangeListeners =
            new CopyOnWriteArrayList<>();

    protected void addStateChangeListener(RunnableStateChangeListener listener) {
        stateChangeListeners.add(listener);
    }

    protected void removeStateChangeListener(RunnableStateChangeListener listener) {
        stateChangeListeners.remove(listener);
    }

    /**
     * Fire the state change event to all listeners on every state change. @see setState().
     * 
     * @param newState - the new state to fire the event for.
     */
    protected void fireStateChanged(RunnableState newState) {
        RunnableStateChangedEvent event = new RunnableStateChangedEvent(newState);
        stateChangeListeners.stream().forEach(listener -> listener.onStateChanged(event));
    }

    /**
     * Get the state.
     * 
     * Synchronized to match the synchronization on "setState". sonarqube(java:S2886)
     * 
     * @return current state.
     */
    @Override
    @NotNull
    public synchronized RunnableState getState() {
        return state;
    }

    /**
     * Set the state.
     * 
     * @param state - the state.
     */
    protected synchronized void setState(final RunnableState state) {

        if (state == null) {
            setError(true, "state is null");
            throw new NullArgumentException(getLogText(getErrorMessage()));
        }

        if (this.state != state) {
            setModified();
        }

        this.state = state;
        fireStateChanged(state);
        LogEx.info(log, LogEx.me(), getLogText("state changed to " + state));
    }

    /**
     * Check if the block has an error.
     * 
     * @return true if the block has an error, false otherwise.
     */
    @Override
    public boolean hasError() {
        return hasError;
    }

    /**
     * Get the error message of the block.
     * 
     * @return the error message of the block.
     */
    @Override
    @NotNull
    public synchronized String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Stop the block.
     */
    @Override
    public synchronized void stop() {

        setState(RunnableState.STOPPED);

        // Do nothing with the junctions. Just stop.

        // Ensure the block is marked as modified.
        setModified();

        LogEx.info(log, LogEx.me(), getLogText("stop completed"));
    }

    /**
     * Forced stop the block.
     */
    @Override
    public synchronized void abort() {

        setState(RunnableState.ABORTED);

        // Do nothing with the junctions. Abort as needed.

        // Ensure the block is marked as modified.
        setModified();

        LogEx.info(log, LogEx.me(), getLogText("abort completed"));
    }

    /**
     * Set the block to READY state without resetting it.
     * 
     * This method is used to set the block to READY state without resetting it. It is used to set
     * the block to READY state when the block is in DONE state for example. Use with caution.
     */
    @Override
    public synchronized void setReady() {

        if (state == RunnableState.DONE || state == RunnableState.ABORTED
                || state == RunnableState.STOPPED) {

            if (hasError) {
                throw new ConfigurationMismatchException(getLogText(getErrorMessage()));
            }

            setState(RunnableState.READY);
        }
    }

    /**
     * Reset the block.
     */
    @Override
    public synchronized void reset() {

        setState(RunnableState.READY);

        // Yes, reset this fields here.
        setError(false, "");
        setInputText("");
        setResultText("");

        // Set the output junction to off.
        getOutputJunction().setState(LineState.OFF);

        // Ensure the block is marked as modified.
        setModified();

        LogEx.info(log, LogEx.me(), getLogText("reset completed"));
    }

    /**
     * Run the block.
     * 
     * Base state machine which runs from [NOT_CONFIGURED||READY] to [DONE]. Main working state is
     * [RUNNING] and it's processing in the subclass.
     * 
     * Subclasses must implement the [RUNNING] state and then put the block to [DONE] state.
     * 
     * @throws ConfigurationMismatchException - if the block is not configured.
     */
    @Override
    public synchronized void run() {

        if (getState() == RunnableState.NOT_CONFIGURED) {
            setError(true, "Block is not configured");
            throw new ConfigurationMismatchException(getLogText(getErrorMessage()));
        }

        if (getState() == RunnableState.READY) {
            setState(RunnableState.RUNNING);
        }

        // CHECKSTYLE:OFF
        // @formatter:off
        /* 
         * Do the job in the subclass as follows:
         *
         * if getState() is RunnableState.RUNNING then
         *     do the job and then
         *     setState() to RunnableState.DONE.
         * That's all.
         */
        // @formatter:on
        // CHECKSTYLE:ON

        // CHECKSTYLE:OFF
        // @formatter:off
        /*
        * Process DONE state in the subclass as follows:
        * 
        * if getState() is RunnableState.DONE then
        *   Normal flow: 
        *     turn off the input junction and 
        *     turn on the output junction.
        *   Special flow:
        *     do something special.
        */
        // @formatter:on
        // CHECKSTYLE:ON
    }

    /**
     * Go further in the normal flow: turn off the input junction and turn on the output junction.
     */
    protected void goFurtherNormal() {

        getInputJunction().setState(LineState.OFF);
        getOutputJunction().setState(LineState.ON);
    }
}
