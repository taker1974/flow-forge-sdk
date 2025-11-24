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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.spb.tksoft.common.exception.ObjectAlreadyExistsException;

/**
 * Tests for Context.
 *
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
class ContextTest {

    private Context context;

    @BeforeEach
    void setUp() {
        context = new Context();
    }

    @Test
    void testPutAndGet() {
        context.put("key1", "value1");
        context.put("key2", 42);

        assertThat(context.get("key1")).isEqualTo("value1");
        assertThat(context.get("key2")).isEqualTo(42);
    }

    @Test
    void testPutDuplicateKey() {
        context.put("key1", "value1");

        assertThatThrownBy(() -> context.put("key1", "value2"))
                .isInstanceOf(ObjectAlreadyExistsException.class);
    }

    @Test
    void testGetNonExistentKey() {
        assertThat(context.get("nonExistent")).isNull();
    }

    @Test
    void testUpdate() {
        context.put("key1", "value1");
        Object result = context.update("key1", "value2");

        assertThat(result).isEqualTo("value2");
        assertThat(context.get("key1")).isEqualTo("value2");
    }

    @Test
    void testUpdateNonExistentKey() {
        Object result = context.update("nonExistent", "value");

        assertThat(result).isNull();
        assertThat(context.get("nonExistent")).isNull();
    }

    @Test
    void testPutWithInvalidKey() {
        assertThatThrownBy(() -> context.put(null, "value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("key is not valid");

        assertThatThrownBy(() -> context.put("", "value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("key is not valid");

        assertThatThrownBy(() -> context.put("1key", "value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("key is not valid");
    }

    @Test
    void testUpdateWithInvalidKey() {
        assertThatThrownBy(() -> context.update(null, "value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("key is not valid");

        assertThatThrownBy(() -> context.update("", "value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("key is not valid");
    }

    @Test
    void testIsValidContextKey() {
        assertThat(Context.isValidContextKey("key1")).isTrue();
        assertThat(Context.isValidContextKey("key_1")).isTrue();
        assertThat(Context.isValidContextKey("key#1")).isTrue();
        assertThat(Context.isValidContextKey("a")).isTrue();
        assertThat(Context.isValidContextKey("a1")).isTrue();

        assertThat(Context.isValidContextKey(null)).isFalse();
        assertThat(Context.isValidContextKey("")).isFalse();
        assertThat(Context.isValidContextKey("   ")).isFalse();
        assertThat(Context.isValidContextKey("1key")).isFalse();
        assertThat(Context.isValidContextKey("key-with-dash")).isFalse();
    }

    @Test
    void testPutWithDifferentValueTypes() {
        context.put("string", "value");
        context.put("integer", 42);
        context.put("double", 3.14);
        context.put("boolean", true);
        context.put("object", new Object());

        assertThat(context.get("string")).isEqualTo("value");
        assertThat(context.get("integer")).isEqualTo(42);
        assertThat(context.get("double")).isEqualTo(3.14);
        assertThat(context.get("boolean")).isEqualTo(true);
        assertThat(context.get("object")).isNotNull();
    }
}

