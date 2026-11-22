package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.commons.exception.FieldCannotBeEmptyException;
import br.com.ipet.ordering.domain.model.commons.exception.StartTimeMustBeBeforeEndTimeException;
import br.com.ipet.ordering.domain.model.commons.exception.TimeMustHaveMinutePrecisionException;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class LockedTimeTest {

    @Test
    void givenValidTimes_whenCreatingLockedTime_thenCreatesSuccessfully() {
        var startTime = LocalTime.of(9, 0);
        var endTime = LocalTime.of(10, 0);

        var lockedTime = new LockedTime(startTime, endTime);

        assertThat(lockedTime.startTime()).isEqualTo(startTime);
        assertThat(lockedTime.endTime()).isEqualTo(endTime);
    }

    @Test
    void givenNullStartTime_whenCreatingLockedTime_thenThrowsFieldCannotBeEmptyException() {
        var endTime = LocalTime.of(10, 0);

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> new LockedTime(null, endTime));
    }

    @Test
    void givenNullEndTime_whenCreatingLockedTime_thenThrowsFieldCannotBeEmptyException() {
        var startTime = LocalTime.of(9, 0);

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> new LockedTime(startTime, null));
    }

    @Test
    void givenStartTimeWithSeconds_whenCreatingLockedTime_thenThrowsTimeMustHaveMinutePrecisionException() {
        var startTime = LocalTime.of(9, 0, 1);
        var endTime = LocalTime.of(10, 0);

        assertThatExceptionOfType(TimeMustHaveMinutePrecisionException.class)
                .isThrownBy(() -> new LockedTime(startTime, endTime));
    }

    @Test
    void givenEndTimeWithNanoseconds_whenCreatingLockedTime_thenThrowsTimeMustHaveMinutePrecisionException() {
        var startTime = LocalTime.of(9, 0);
        var endTime = LocalTime.of(10, 0, 0, 1);

        assertThatExceptionOfType(TimeMustHaveMinutePrecisionException.class)
                .isThrownBy(() -> new LockedTime(startTime, endTime));
    }

    @Test
    void givenStartTimeAfterEndTime_whenCreatingLockedTime_thenThrowsStartTimeMustBeBeforeEndTimeException() {
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(9, 30);

        assertThatExceptionOfType(StartTimeMustBeBeforeEndTimeException.class)
                .isThrownBy(() -> new LockedTime(startTime, endTime));
    }

    @Test
    void givenStartTimeEqualToEndTime_whenCreatingLockedTime_thenThrowsStartTimeMustBeBeforeEndTimeException() {
        var startTime = LocalTime.of(9, 0);
        var endTime = LocalTime.of(9, 0);

        assertThatExceptionOfType(StartTimeMustBeBeforeEndTimeException.class)
                .isThrownBy(() -> new LockedTime(startTime, endTime));
    }

    @Test
    void givenDurationBelowMinimum_whenCreatingLockedTime_thenThrowsMinimumLockedTimeIntervalException() {
        var startTime = LocalTime.of(9, 0);
        var endTime = LocalTime.of(9, 15);

        assertThatExceptionOfType(MinimumLockedTimeIntervalException.class)
                .isThrownBy(() -> new LockedTime(startTime, endTime));
    }

    @Test
    void givenStartTimeOutsideThirtyMinuteInterval_whenCreatingLockedTime_thenThrowsInvalidIntervalException() {
        var startTime = LocalTime.of(9, 15);
        var endTime = LocalTime.of(10, 0);

        assertThatExceptionOfType(InvalidIntervalException.class)
                .isThrownBy(() -> new LockedTime(startTime, endTime));
    }

    @Test
    void givenEndTimeOutsideThirtyMinuteInterval_whenCreatingLockedTime_thenThrowsInvalidIntervalException() {
        var startTime = LocalTime.of(9, 0);
        var endTime = LocalTime.of(10, 15);

        assertThatExceptionOfType(InvalidIntervalException.class)
                .isThrownBy(() -> new LockedTime(startTime, endTime));
    }

    @Test
    void givenOverlappingLockedTimes_whenCallingVerifyConflict_thenReturnsTrue() {
        var lockedTime = new LockedTime(LocalTime.of(9, 0), LocalTime.of(10, 0));
        var overlappingLockedTime = new LockedTime(LocalTime.of(9, 30), LocalTime.of(10, 30));

        var hasConflict = lockedTime.verifyConflict(overlappingLockedTime);

        assertThat(hasConflict).isTrue();
    }

    @Test
    void givenNonOverlappingLockedTimes_whenCallingVerifyConflict_thenReturnsFalse() {
        var lockedTime = new LockedTime(LocalTime.of(9, 0), LocalTime.of(10, 0));
        var separateLockedTime = new LockedTime(LocalTime.of(10, 30), LocalTime.of(11, 0));

        var hasConflict = lockedTime.verifyConflict(separateLockedTime);

        assertThat(hasConflict).isFalse();
    }

    @Test
    void givenAdjacentLockedTimes_whenCallingVerifyConflict_thenReturnsFalse() {
        var lockedTime = new LockedTime(LocalTime.of(9, 0), LocalTime.of(10, 0));
        var adjacentLockedTime = new LockedTime(LocalTime.of(10, 0), LocalTime.of(10, 30));

        var hasConflict = lockedTime.verifyConflict(adjacentLockedTime);

        assertThat(hasConflict).isFalse();
    }

    @Test
    void givenLockedTimeEndingWhenAnotherStarts_whenCallingVerifyConflict_thenReturnsFalse() {
        var lockedTime = new LockedTime(LocalTime.of(9, 0), LocalTime.of(10, 0));
        var precedingLockedTime = new LockedTime(LocalTime.of(8, 0), LocalTime.of(9, 0));

        var hasConflict = lockedTime.verifyConflict(precedingLockedTime);

        assertThat(hasConflict).isFalse();
    }

    @Test
    void givenLockedTime_whenCallingLockedMinutes_thenReturnsDurationInMinutes() {
        var lockedTime = new LockedTime(LocalTime.of(9, 0), LocalTime.of(10, 30));

        var lockedMinutes = lockedTime.lockedMinutes();

        assertThat(lockedMinutes).isEqualTo(90);
    }
}
