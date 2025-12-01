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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.common.exceptions.NullArgumentException;
import ru.spb.tksoft.common.exceptions.ObjectAlreadyExistsException;

/**
 * Context for the block.
 * 
 * Used to store context data for the evaluation of the expressions.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@RequiredArgsConstructor
public final class Context {

    private final Map<String, Object> data = new ConcurrentHashMap<>();

    /**
     * Checks if the given string is a valid context key.
     * 
     * @param key - the key to check.
     * @return true if the key is valid, false otherwise.
     */
    public static boolean isValidContextKey(final String key) {

        return key != null && !key.isBlank() && key.length() <= 255
                && key.matches("^[a-zA-Z][a-zA-Z0-9_#]*$");
    }

    /**
     * Puts the value into the context.
     * 
     * @param key - the key.
     * @param value - the value.
     * @throws NullArgumentException - if the key is null or blank.
     * @throws ObjectAlreadyExistsException - if the key already exists.
     */
    public void put(final String key, final Object value) {

        if (!isValidContextKey(key)) {
            throw new IllegalArgumentException("key is not valid");
        }

        if (data.putIfAbsent(key, value) != null) {
            throw new ObjectAlreadyExistsException("key already exists");
        }
    }

    /**
     * Gets the value from the context.
     * 
     * @param key - the key.
     * @return the value or null if the key does not exist.
     */
    public Object get(final String key) {
        return data.getOrDefault(key, null);
    }

    /**
     * Updates the value in the context.
     * 
     * @param key - the key.
     * @param newValue - the new value.
     * @return the new value or null if the key does not exist.
     * @throws IllegalArgumentException - if the key is not valid.
     */
    public Object update(final String key, final Object newValue) {
        if (!isValidContextKey(key)) {
            throw new IllegalArgumentException("key is not valid");
        }

        return data.computeIfPresent(key, (k, v) -> newValue);
    }
}
