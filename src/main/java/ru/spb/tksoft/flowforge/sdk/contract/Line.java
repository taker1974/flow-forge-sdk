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
 * Line interface.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public interface Line extends Modifiable {

    /**
     * Get the internal line id.
     * 
     * @return the internal line id.
     */
    String getInternalLineId();

    /**
     * Get the block from.
     * 
     * @return the block from.
     */
    Block getBlockFrom();

    /**
     * Get the block to.
     * 
     * @return the block to.
     */
    Block getBlockTo();

    /**
     * Get the line state.
     * 
     * @return the line state.
     */
    LineState getState();

    /**
     * Set the line state.
     * 
     * @param state - the line state.
     */
    void setState(LineState state);

    /**
     * Reset the line.
     */
    void reset();

    /**
     * Get results from previous block.
     * 
     * @return the results from previous block.
     */
    String getResultText();
}
