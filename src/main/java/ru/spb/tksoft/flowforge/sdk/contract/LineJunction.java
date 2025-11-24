/*
 * Copyright 2025 Konstantin Terskikh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.spb.tksoft.flowforge.sdk.contract;

import ru.spb.tksoft.flowforge.sdk.enumeration.LineState;

/**
 * LineJunction interface.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public interface LineJunction {

    /**
     * Add a line to the junction.
     * 
     * @param line - the line to add.
     */
    void addLine(Line line);

    /**
     * Check if the junction has lines.
     * 
     * @return true if the junction has lines, false otherwise.
     */
    boolean hasLines();

    /**
     * Set the state for each line in the junction.
     * 
     * @param state - the state.
     */
    void setState(LineState state);

    /**
     * Get the state of the junction.
     * 
     * @return the state of the junction.
     */
    LineState getState();

    /**
     * Get the result string from previous blocks.
     * 
     * @return the result string.
     */
    String getResultString();
}
