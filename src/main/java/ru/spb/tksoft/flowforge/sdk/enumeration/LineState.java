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

package ru.spb.tksoft.flowforge.sdk.enumeration;

/**
 * LineState enum.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public enum LineState {

    /** Off. */
    OFF("OFF"),

    /** On. */
    ON("ON");

    /** State value. */
    private final String value;

    /**
     * Constructor.
     * 
     * @param value - state value.
     */
    LineState(String value) {
        this.value = value;
    }

    /**
     * Get the state value.
     * 
     * @return the state value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get LineState by value.
     * 
     * @param value - state value.
     * @return LineState or OFF if not found.
     */
    public static LineState fromValue(String value) {
        for (LineState state : LineState.values()) {
            if (state.value.equals(value)) {
                return state;
            }
        }
        return LineState.OFF;
    }
}
