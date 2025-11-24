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

import ru.spb.tksoft.flowforge.sdk.enumeration.RunnableState;

/**
 * Runnable interface.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public interface Runnable {

    /**
     * Get the runnable state.
     * 
     * @return the runnable state.
     */
    RunnableState getState();

    /**
     * Set the runnable to READY state.
     * 
     * This method is used to set the runnable to READY state without resetting it. It is used to
     * set the runnable to READY state when the runnable is in DONE state for example. Use with
     * caution.
     */
    void setReady();

    /**
     * Run the runnable.
     */
    void run();

    /**
     * Stop the runnable.
     */
    void stop();

    /**
     * Forced stop the runnable.
     */
    void abort();

    /**
     * Reset the runnable.
     */
    void reset();

    /**
     * Check if the runnable has an error.
     * 
     * @return true if the runnable has an error, false otherwise.
     */
    boolean hasError();

    /**
     * Get the error message of the runnable.
     * 
     * @return the error message of the runnable.
     */
    String getErrorMessage();
}
