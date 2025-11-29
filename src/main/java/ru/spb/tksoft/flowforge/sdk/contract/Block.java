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

package ru.spb.tksoft.flowforge.sdk.contract;

/**
 * Block interface.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public interface Block extends Runnable, Modifiable {

    /**
     * Get the internal block id.
     * 
     * @return the internal block id.
     */
    String getInternalBlockId();

    /**
     * Get the block type id.
     * 
     * @return the block type id.
     */
    String getBlockTypeId();

    /**
     * Get the default input text.
     * 
     * @return the default input text.
     */
    String getDefaultInputText();

    /**
     * Set the input text.
     * 
     * @param inputText - the input text.
     */
    void setInputText(String inputText);

    /**
     * Get the input text.
     * 
     * @return the input text.
     */
    String getInputText();

    /**
     * Get the result text.
     * 
     * @return the result text.
     */
    String getResultText();

    /**
     * Get the input junction.
     * 
     * @return the input junction.
     */
    LineJunction getInputJunction();

    /**
     * Get the output junction.
     * 
     * @return the output junction.
     */
    LineJunction getOutputJunction();
}
