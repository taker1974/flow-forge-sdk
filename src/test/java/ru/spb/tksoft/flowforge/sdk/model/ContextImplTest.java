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

import static org.assertj.core.api.Assertions.*;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.spb.tksoft.common.exceptions.ObjectAlreadyExistsException;

/**
 * Tests for Context.
 *
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
class ContextImplTest {

    private ContextImpl context;

    @BeforeEach
    void setUp() {
        context = new ContextImpl();
    }

    @Test
    void testPutAndGet() {
        context.put("key1", "value1");
        context.put("key2", 42);

        assertThat(context.get("key1").get()).isEqualTo("value1");
        assertThat(context.get("key2").get()).isEqualTo(42);
    }

    @Test
    void testPutDuplicateKey() {
        context.put("key1", "value1");

        assertThatThrownBy(() -> context.put("key1", "value2"))
                .isInstanceOf(ObjectAlreadyExistsException.class);
    }

    @Test
    void testGetNonExistentKey() {
        assertThat(context.get("nonExistent").isEmpty()).isTrue();
    }

    @Test
    void testUpdate() {
        context.put("key1", "value1");
        Optional<Object> result = context.update("key1", "value2");

        assertThat(result.orElse(null)).isEqualTo("value2");
        assertThat(context.get("key1").orElse(null)).isEqualTo("value2");
    }

    @Test
    void testUpdateNonExistentKey() {
        Optional<Object> result = context.update("nonExistent", "value");

        assertThat(result).isNotPresent();
        assertThat(context.get("nonExistent")).isNotPresent();
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
        assertThat(context.isValidContextKey("key1")).isTrue();
        assertThat(context.isValidContextKey("key_1")).isTrue();
        assertThat(context.isValidContextKey("key#1")).isTrue();
        assertThat(context.isValidContextKey("a")).isTrue();
        assertThat(context.isValidContextKey("a1")).isTrue();

        assertThat(context.isValidContextKey(null)).isFalse();
        assertThat(context.isValidContextKey("")).isFalse();
        assertThat(context.isValidContextKey("   ")).isFalse();
        assertThat(context.isValidContextKey("1key")).isFalse();
        assertThat(context.isValidContextKey("key-with-dash")).isFalse();
    }

    @Test
    void testPutWithDifferentValueTypes() {
        context.put("string", "value");
        context.put("integer", 42);
        context.put("double", 3.14);
        context.put("boolean", true);
        context.put("object", new Object());

        assertThat(context.get("string").get()).isEqualTo("value");
        assertThat(context.get("integer").get()).isEqualTo(42);
        assertThat(context.get("double").get()).isEqualTo(3.14);
        assertThat((Boolean) context.get("boolean").get()).isTrue();
        assertThat(context.get("object").get()).isNotNull();
    }
}

