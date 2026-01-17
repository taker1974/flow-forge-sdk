/*
 * Copyright 2026 Konstantin Terskikh
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

import java.util.Map;
import java.util.Optional;

/**
 * Context interface.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2026
 */
public interface Context {

    /**
     * Checks if the given string is a valid context key.
     * 
     * @param key - the key to check.
     * @return true if the key is valid, false otherwise.
     */
    boolean isValidContextKey(String key);

    /**
     * Puts the value into the context.
     * 
     * @param key - the key.
     * @param value - the value.
     */
    void put(String key, Object value);

    /**
     * Gets the value from the context.
     * 
     * @param key - the key.
     * @return the value or empty if the key does not exist.
     */
    Optional<Object> get(String key);

    /**
     * Updates the value in the context.
     * 
     * @param key - the key.
     * @param newValue - the new value.
     * @return the new value or null if the key does not exist.
     */
    Optional<Object> update(String key, Object newValue);

    /**
     * Removes the value from the context.
     * 
     * @param key - the key.
     */
    void remove(String key);

    /**
     * Clears the context.
     */
    void clear();

    /**
     * Puts all the values from the map into the context.
     * 
     * @param map - the map.
     */
    void putAll(Map<String, Object> map);

    /**
     * Converts the context to an immutable map.
     * 
     * @return the immutable map.
     */
    Map<String, Object> toImmutableMap();
}
