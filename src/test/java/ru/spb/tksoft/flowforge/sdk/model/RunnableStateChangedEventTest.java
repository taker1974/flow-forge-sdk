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

import org.junit.jupiter.api.Test;
import ru.spb.tksoft.flowforge.sdk.enumeration.RunnableState;

/**
 * Tests for RunnableStateChangedEvent.
 *
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
class RunnableStateChangedEventTest {

    @Test
    void testConstructor() {
        RunnableStateChangedEvent event = new RunnableStateChangedEvent(RunnableState.READY);

        assertThat(event.getNewState()).isEqualTo(RunnableState.READY);
    }

    @Test
    void testConstructorWithDifferentStates() {
        RunnableStateChangedEvent event1 = new RunnableStateChangedEvent(RunnableState.RUNNING);
        RunnableStateChangedEvent event2 = new RunnableStateChangedEvent(RunnableState.DONE);
        RunnableStateChangedEvent event3 = new RunnableStateChangedEvent(RunnableState.STOPPED);

        assertThat(event1.getNewState()).isEqualTo(RunnableState.RUNNING);
        assertThat(event2.getNewState()).isEqualTo(RunnableState.DONE);
        assertThat(event3.getNewState()).isEqualTo(RunnableState.STOPPED);
    }
}

