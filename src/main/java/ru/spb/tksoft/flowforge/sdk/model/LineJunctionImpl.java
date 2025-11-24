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

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import ru.spb.tksoft.common.exception.NullArgumentException;
import ru.spb.tksoft.flowforge.sdk.contract.Line;
import ru.spb.tksoft.flowforge.sdk.contract.LineJunction;
import ru.spb.tksoft.flowforge.sdk.enumeration.LineState;

/**
 * LineJunction implementation.
 * 
 * Subclassing is not allowed.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@NoArgsConstructor
public final class LineJunctionImpl implements LineJunction {

    /** Line separator. */
    protected static final String NL = System.lineSeparator();

    @NotNull
    private final List<Line> lines = new ArrayList<>();

    @NotNull
    private volatile LineState state = LineState.OFF;

    /**
     * Add a line to the junction.
     * 
     * @param line - the line to add.
     */
    @Override
    public synchronized void addLine(final Line line) {

        if (line == null) {
            throw new NullArgumentException("line must not be null");
        }

        lines.add(line);
    }

    /**
     * Check if the junction has lines.
     * 
     * @return true if the junction has lines, false otherwise.
     */
    @Override
    public synchronized boolean hasLines() {
        return !lines.isEmpty();
    }

    /**
     * Set the state for each line in the junction.
     * 
     * @param state the state.
     */
    @Override
    public synchronized void setState(final LineState state) {

        if (state == null) {
            throw new NullArgumentException("state must not be null");
        }

        lines.forEach(line -> line.setState(state));
        this.state = state;
    }

    /**
     * Get the state of the junction.
     * 
     * Synchronized to match the synchronization on "setState". sonarqube(java:S2886)
     * 
     * @return the state.
     */
    @Override
    public synchronized LineState getState() {
        return state;
    }

    /**
     * Get the result string.
     */
    @Override
    @NotNull
    public String getResultString() {

        final var sb = new StringBuilder();
        lines.forEach(line -> {
            String result = line.getResultText();
            if (result == null || result.isBlank()) {
                return;
            }
            sb.append(result).append(NL);
        });
        return sb.toString();
    }
}
