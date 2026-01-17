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
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.common.exceptions.NullArgumentException;
import ru.spb.tksoft.common.exceptions.ObjectAlreadyExistsException;
import ru.spb.tksoft.flowforge.sdk.contract.Context;

/**
 * Context implementation.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@RequiredArgsConstructor
public final class ContextImpl implements Context {

    private final Map<String, Object> data = new ConcurrentHashMap<>();

    /** Context key match pattern. */
    public static final String CONTEXT_KEY_MATCH_PATTERN = "^[a-zA-Z][a-zA-Z0-9_#]*$";

    /** Minimum context key length. */
    public static final int MIN_CONTEXT_KEY_LENGTH = 1;

    /** Maximum context key length. */
    public static final int MAX_CONTEXT_KEY_LENGTH = 255;

    /**
     * Checks if the given string is a valid context key.
     * 
     * @param key - the key to check.
     * @return true if the key is valid, false otherwise.
     */
    @Override
    public boolean isValidContextKey(final String key) {

        return !Objects.isNull(key) && !key.isBlank() &&
                key.length() >= MIN_CONTEXT_KEY_LENGTH && key.length() <= MAX_CONTEXT_KEY_LENGTH
                && key.matches(CONTEXT_KEY_MATCH_PATTERN);
    }

    /**
     * Puts the value into the context.
     * 
     * @param key - the key.
     * @param value - the value.
     * @throws NullArgumentException - if the key is null or blank.
     * @throws ObjectAlreadyExistsException - if the key already exists.
     */
    @Override
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
     * @return the value or empty if the key does not exist.
     */
    @Override
    public Optional<Object> get(final String key) {
        return Optional.ofNullable(data.get(key));
    }

    /**
     * Updates the value in the context.
     * 
     * @param key - the key.
     * @param newValue - the new value.
     * @return the new value or null if the key does not exist.
     * @throws IllegalArgumentException - if the key is not valid.
     */
    @Override
    public Optional<Object> update(final String key, final Object newValue) {
        if (!isValidContextKey(key)) {
            throw new IllegalArgumentException("key is not valid");
        }

        return Optional.ofNullable(data.computeIfPresent(key, (k, v) -> newValue));
    }

    /**
     * Removes the value from the context.
     * 
     * @param key - the key.
     * @throws IllegalArgumentException - if the key is not valid.
     */
    @Override
    public void remove(final String key) {
        if (!isValidContextKey(key)) {
            throw new IllegalArgumentException("key is not valid");
        }

        data.remove(key);
    }

    /**
     * Clears the context.
     */
    @Override
    public void clear() {
        data.clear();
    }

    /**
     * Puts all the values from the map into the context.
     * 
     * @param map - the map.
     * @throws NullArgumentException - if the map is null.
     */
    @Override
    public void putAll(final Map<String, Object> map) {
        if (map == null) {
            throw new NullArgumentException("map must not be null");
        }

        data.putAll(map);
    }

    /**
     * Converts the context to an immutable map.
     * 
     * @return the immutable map.
     */
    @Override
    public Map<String, Object> toImmutableMap() {
        return Map.copyOf(data);
    }
}
