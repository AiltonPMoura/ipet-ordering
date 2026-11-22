package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.commons.exception.FieldCannotBeEmptyException;
import br.com.ipet.ordering.domain.model.commons.exception.StartTimeMustBeBeforeEndTimeException;
import br.com.ipet.ordering.domain.model.commons.exception.TimeMustHaveMinutePrecisionException;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class WorkingHoursTest {

    @Test
    void givenAllowedBoundaryTimes_whenCreatingWorkingHours_thenCreatesSuccessfully() {
        var startTime = LocalTime.of(8, 0);
        var endTime = LocalTime.of(18, 0);

        var workingHours = new WorkingHours(startTime, endTime);

        assertThat(workingHours.startTime()).isEqualTo(startTime);
        assertThat(workingHours.endTime()).isEqualTo(endTime);
    }

    @Test
    void givenMinimumDuration_whenCreatingWorkingHours_thenAcceptsDuration() {
        var startTime = LocalTime.of(8, 0);
        var endTime = LocalTime.of(8, 30);

        var workingHours = new WorkingHours(startTime, endTime);

        assertThat(workingHours.workingMinutes()).isEqualTo(WorkingHours.MINIMUM_WORKING_MINUTES);
    }

    @Test
    void givenNullStartTime_whenCreatingWorkingHours_thenThrowsFieldCannotBeEmptyException() {
        var endTime = LocalTime.of(9, 0);

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> new WorkingHours(null, endTime));
    }

    @Test
    void givenNullEndTime_whenCreatingWorkingHours_thenThrowsFieldCannotBeEmptyException() {
        var startTime = LocalTime.of(8, 0);

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> new WorkingHours(startTime, null));
    }

    @Test
    void givenStartTimeWithSeconds_whenCreatingWorkingHours_thenThrowsTimeMustHaveMinutePrecisionException() {
        var startTime = LocalTime.of(8, 0, 1);
        var endTime = LocalTime.of(9, 0);

        assertThatExceptionOfType(TimeMustHaveMinutePrecisionException.class)
                .isThrownBy(() -> new WorkingHours(startTime, endTime));
    }

    @Test
    void givenEndTimeWithNanoseconds_whenCreatingWorkingHours_thenThrowsTimeMustHaveMinutePrecisionException() {
        var startTime = LocalTime.of(8, 0);
        var endTime = LocalTime.of(9, 0, 0, 1);

        assertThatExceptionOfType(TimeMustHaveMinutePrecisionException.class)
                .isThrownBy(() -> new WorkingHours(startTime, endTime));
    }

    @Test
    void givenStartTimeAfterEndTime_whenCreatingWorkingHours_thenThrowsStartTimeMustBeBeforeEndTimeException() {
        var startTime = LocalTime.of(9, 0);
        var endTime = LocalTime.of(8, 30);

        assertThatExceptionOfType(StartTimeMustBeBeforeEndTimeException.class)
                .isThrownBy(() -> new WorkingHours(startTime, endTime));
    }

    @Test
    void givenStartTimeBeforeMinimum_whenCreatingWorkingHours_thenThrowsMinimumStartWorkingException() {
        var startTime = LocalTime.of(7, 30);
        var endTime = LocalTime.of(9, 0);

        assertThatExceptionOfType(MinimumStartWorkingException.class)
                .isThrownBy(() -> new WorkingHours(startTime, endTime));
    }

    @Test
    void givenEndTimeAfterMaximum_whenCreatingWorkingHours_thenThrowsMaximumEndWorkingException() {
        var startTime = LocalTime.of(17, 0);
        var endTime = LocalTime.of(18, 30);

        assertThatExceptionOfType(MaximumEndWorkingException.class)
                .isThrownBy(() -> new WorkingHours(startTime, endTime));
    }

    @Test
    void givenDurationBelowMinimum_whenCreatingWorkingHours_thenThrowsMinimumWorkingHoursException() {
        var startTime = LocalTime.of(8, 0);
        var endTime = LocalTime.of(8, 15);

        assertThatExceptionOfType(MinimumWorkingHoursException.class)
                .isThrownBy(() -> new WorkingHours(startTime, endTime));
    }

    @Test
    void givenStartTimeOutsideThirtyMinuteInterval_whenCreatingWorkingHours_thenThrowsInvalidIntervalException() {
        var startTime = LocalTime.of(8, 15);
        var endTime = LocalTime.of(9, 0);

        assertThatExceptionOfType(InvalidIntervalException.class)
                .isThrownBy(() -> new WorkingHours(startTime, endTime));
    }

    @Test
    void givenEndTimeOutsideThirtyMinuteInterval_whenCreatingWorkingHours_thenThrowsInvalidIntervalException() {
        var startTime = LocalTime.of(8, 0);
        var endTime = LocalTime.of(9, 15);

        assertThatExceptionOfType(InvalidIntervalException.class)
                .isThrownBy(() -> new WorkingHours(startTime, endTime));
    }

    @Test
    void givenStartTimeAtOpeningAndEndTimeWithinWorkingHours_whenCallingIsWithinWorkingHours_thenReturnsTrue() {
        var workingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        var startTime = LocalTime.of(9, 0);
        var endTime = LocalTime.of(10, 0);

        var isWithinWorkingHours = workingHours.isWithinWorkingHours(startTime, endTime);

        assertThat(isWithinWorkingHours).isTrue();
    }

    @Test
    void givenStartTimeWithinWorkingHoursAndEndTimeAtClosing_whenCallingIsWithinWorkingHours_thenReturnsTrue() {
        var workingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        var startTime = LocalTime.of(16, 0);
        var endTime = LocalTime.of(17, 0);

        var isWithinWorkingHours = workingHours.isWithinWorkingHours(startTime, endTime);

        assertThat(isWithinWorkingHours).isTrue();
    }

    @Test
    void givenStartTimeBeforeOpening_whenCallingIsWithinWorkingHours_thenReturnsFalse() {
        var workingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        var startTime = LocalTime.of(8, 30);
        var endTime = LocalTime.of(9, 30);

        var isWithinWorkingHours = workingHours.isWithinWorkingHours(startTime, endTime);

        assertThat(isWithinWorkingHours).isFalse();
    }

    @Test
    void givenStartTimeWithSeconds_whenCallingIsWithinWorkingHours_thenThrowsTimeMustHaveMinutePrecisionException() {
        var workingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        var startTime = LocalTime.of(9, 0, 30);
        var endTime = LocalTime.of(10, 0);

        assertThatExceptionOfType(TimeMustHaveMinutePrecisionException.class)
                .isThrownBy(() -> workingHours.isWithinWorkingHours(startTime, endTime));
    }

    @Test
    void givenEndTimeAfterClosing_whenCallingIsNotWithinWorkingHours_thenReturnsTrue() {
        var workingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        var startTime = LocalTime.of(16, 30);
        var endTime = LocalTime.of(17, 30);

        var isNotWithinWorkingHours = workingHours.isNotWithinWorkingHours(startTime, endTime);

        assertThat(isNotWithinWorkingHours).isTrue();
    }

    @Test
    void givenStartTimeAfterEndTime_whenCallingIsNotWithinWorkingHours_thenReturnsTrue() {
        var workingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(9, 30);

        var isNotWithinWorkingHours = workingHours.isNotWithinWorkingHours(startTime, endTime);

        assertThat(isNotWithinWorkingHours).isTrue();
    }

    @Test
    void givenStartTimeAndEndTimeStrictlyInsideWorkingHours_whenCallingIsStrictlyWithinWorkingHours_thenReturnsTrue() {
        var workingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        var startTime = LocalTime.of(9, 30);
        var endTime = LocalTime.of(10, 30);

        var isStrictlyWithinWorkingHours = workingHours.isStrictlyWithinWorkingHours(startTime, endTime);

        assertThat(isStrictlyWithinWorkingHours).isTrue();
    }

    @Test
    void givenEndTimeWithNanoseconds_whenCallingIsStrictlyWithinWorkingHours_thenThrowsTimeMustHaveMinutePrecisionException() {
        var workingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        var startTime = LocalTime.of(9, 30);
        var endTime = LocalTime.of(10, 30, 0, 1);

        assertThatExceptionOfType(TimeMustHaveMinutePrecisionException.class)
                .isThrownBy(() -> workingHours.isStrictlyWithinWorkingHours(startTime, endTime));
    }

    @Test
    void givenStartTimeAtOpening_whenCallingIsNotStrictlyWithinWorkingHours_thenReturnsTrue() {
        var workingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        var startTime = LocalTime.of(9, 0);
        var endTime = LocalTime.of(10, 0);

        var isNotStrictlyWithinWorkingHours = workingHours.isNotStrictlyWithinWorkingHours(startTime, endTime);

        assertThat(isNotStrictlyWithinWorkingHours).isTrue();
    }

    @Test
    void givenEndTimeAtClosing_whenCallingIsNotStrictlyWithinWorkingHours_thenReturnsTrue() {
        var workingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        var startTime = LocalTime.of(16, 0);
        var endTime = LocalTime.of(17, 0);

        var isNotStrictlyWithinWorkingHours = workingHours.isNotStrictlyWithinWorkingHours(startTime, endTime);

        assertThat(isNotStrictlyWithinWorkingHours).isTrue();
    }

    @Test
    void givenStartTimeAfterEndTime_whenCallingIsNotStrictlyWithinWorkingHours_thenReturnsTrue() {
        var workingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(9, 30);

        var isNotStrictlyWithinWorkingHours = workingHours.isNotStrictlyWithinWorkingHours(startTime, endTime);

        assertThat(isNotStrictlyWithinWorkingHours).isTrue();
    }

    @Test
    void givenWorkingHours_whenCallingWorkingMinutes_thenReturnsDurationInMinutes() {
        var workingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 30));

        var workingMinutes = workingHours.workingMinutes();

        assertThat(workingMinutes).isEqualTo(510);
    }
}
