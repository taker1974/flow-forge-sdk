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
 * RunnableState enum.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public enum RunnableState {

    /**
     * Not configured. Tnere is no default meaningful state: the object must be initialized with
     * state explicitly.
     */
    NOT_CONFIGURED("NOT_CONFIGURED"),

    /** Ready to run. */
    READY("READY"),

    /** Running. */
    RUNNING("RUNNING"),

    /** Done. */
    DONE("DONE"),

    /** Stopped from outside. */
    STOPPED("STOPPED"),

    /**
     * Aborted from outside.
     */
    ABORTED("ABORTED");

    /** State value. */
    private final String value;

    /**
     * Constructor.
     * 
     * @param value - state value.
     */
    RunnableState(String value) {
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
     * Check if the state is ready to run.
     * 
     * @return true if the state is ready to run, false otherwise.
     */
    public boolean isReadyToRun() {
        return this == READY || this == RUNNING;
    }

    /**
     * Get RunnableState by value.
     * 
     * @param value - state value.
     * @return RunnableState or NOT_CONFIGURED if not found.
     */
    public static RunnableState fromValue(String value) {
        for (RunnableState state : RunnableState.values()) {
            if (state.value.equals(value)) {
                return state;
            }
        }
        return RunnableState.NOT_CONFIGURED;
    }
}
