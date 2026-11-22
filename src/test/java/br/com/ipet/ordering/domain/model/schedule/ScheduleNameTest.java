package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.commons.exception.FieldCannotBeEmptyException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ScheduleNameTest {

    @Test
    void givenNonBlankValue_whenCreatingScheduleName_thenCreatesScheduleName() {
        var value = "Appointment schedule";

        var scheduleName = new ScheduleName(value);

        assertThat(scheduleName.value()).isEqualTo(value);
    }

    @Test
    void givenNullValue_whenCreatingScheduleName_thenThrowsFieldCannotBeEmptyException() {
        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> new ScheduleName(null));
    }

    @Test
    void givenEmptyValue_whenCreatingScheduleName_thenThrowsFieldCannotBeEmptyException() {
        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> new ScheduleName(""));
    }

    @Test
    void givenBlankValue_whenCreatingScheduleName_thenThrowsFieldCannotBeEmptyException() {
        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> new ScheduleName("   "));
    }
}
