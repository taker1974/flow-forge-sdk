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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.spb.tksoft.common.exceptions.NullArgumentException;
import ru.spb.tksoft.flowforge.sdk.contract.Line;
import ru.spb.tksoft.flowforge.sdk.enumeration.LineState;

/**
 * Tests for LineJunctionImpl.
 *
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
class LineJunctionImplTest {

    private LineJunctionImpl junction;

    @BeforeEach
    void setUp() {
        junction = new LineJunctionImpl();
    }

    @Test
    void testConstructor() {
        assertThat(junction)
                .satisfies(j -> assertThat(j.hasLines()).isFalse())
                .satisfies(j -> assertThat(j.getState()).isEqualTo(LineState.OFF));
    }

    @Test
    void testAddLine() {
        Line line = mock(Line.class);
        junction.addLine(line);

        assertThat(junction.hasLines()).isTrue();
    }

    @Test
    void testAddLineWithNull() {
        assertThatThrownBy(() -> junction.addLine(null))
                .isInstanceOf(NullArgumentException.class)
                .hasMessageContaining("line must not be null");
    }

    @Test
    void testAddMultipleLines() {
        Line line1 = mock(Line.class);
        Line line2 = mock(Line.class);
        Line line3 = mock(Line.class);

        junction.addLine(line1);
        junction.addLine(line2);
        junction.addLine(line3);

        assertThat(junction.hasLines()).isTrue();
    }

    @Test
    void testSetState() {
        Line line1 = mock(Line.class);
        Line line2 = mock(Line.class);
        junction.addLine(line1);
        junction.addLine(line2);

        junction.setState(LineState.ON);

        assertThat(junction.getState()).isEqualTo(LineState.ON);
        verify(line1).setState(LineState.ON);
        verify(line2).setState(LineState.ON);
    }

    @Test
    void testSetStateWithNull() {
        assertThatThrownBy(() -> junction.setState(null))
                .isInstanceOf(NullArgumentException.class)
                .hasMessageContaining("state must not be null");
    }

    @Test
    void testSetStateToOff() {
        Line line = mock(Line.class);
        junction.addLine(line);

        junction.setState(LineState.OFF);

        assertThat(junction.getState()).isEqualTo(LineState.OFF);
        verify(line).setState(LineState.OFF);
    }

    @Test
    void testGetResultString() {
        Line line1 = mock(Line.class);
        Line line2 = mock(Line.class);
        Line line3 = mock(Line.class);

        when(line1.getResultText()).thenReturn("result1");
        when(line2.getResultText()).thenReturn("result2");
        when(line3.getResultText()).thenReturn("");

        junction.addLine(line1);
        junction.addLine(line2);
        junction.addLine(line3);

        String result = junction.getResultString();

        assertThat(result)
                .contains("result1")
                .contains("result2")
                .doesNotContain("result3");
    }

    @Test
    void testGetResultStringWithNullAndBlank() {
        Line line1 = mock(Line.class);
        Line line2 = mock(Line.class);
        Line line3 = mock(Line.class);

        when(line1.getResultText()).thenReturn("result1");
        when(line2.getResultText()).thenReturn(null);
        when(line3.getResultText()).thenReturn("   ");

        junction.addLine(line1);
        junction.addLine(line2);
        junction.addLine(line3);

        String result = junction.getResultString();

        assertThat(result)
                .contains("result1")
                .doesNotContain("null");
    }

    @Test
    void testGetResultStringWithEmptyJunction() {
        String result = junction.getResultString();

        assertThat(result).isEmpty();
    }
}

