package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.commons.exception.FieldCannotBeEmptyException;
import br.com.ipet.ordering.domain.model.commons.id.ScheduleId;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class AppointmentWorkDayTest {

    @Test
    void givenValidAttributes_whenCreatingWorkDay_thenCreatesWithEmptyLockedTimes() {
        var scheduleId = new ScheduleId();
        var workingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));

        var workDay = AppointmentWorkDay.createNew(scheduleId, DayOfWeek.MONDAY, workingHours);

        assertThat(workDay.id()).isNotNull();
        assertThat(workDay.scheduleId()).isEqualTo(scheduleId);
        assertThat(workDay.dayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(workDay.workingHours()).isEqualTo(workingHours);
        assertThat(workDay.lockedTimes()).isEmpty();
    }

    @Test
    void givenValidAttributes_whenCreatingExistingWorkDay_thenCreatesSuccessfully() {
        var workDayId = new AppointmentWorkDayId();
        var scheduleId = new ScheduleId();
        var workingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        var lockedTime = new LockedTime(LocalTime.of(10, 0), LocalTime.of(11, 0));

        var workDay = AppointmentWorkDay.existing()
                .id(workDayId)
                .scheduleId(scheduleId)
                .dayOfWeek(DayOfWeek.MONDAY)
                .workingHours(workingHours)
                .lockedTimes(Set.of(lockedTime))
                .build();

        assertThat(workDay.id()).isEqualTo(workDayId);
        assertThat(workDay.scheduleId()).isEqualTo(scheduleId);
        assertThat(workDay.dayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(workDay.workingHours()).isEqualTo(workingHours);
        assertThat(workDay.lockedTimes()).containsExactly(lockedTime);
    }

    @Test
    void givenLockedTimes_whenCreatingExistingWorkDay_thenCopiesLockedTimes() {
        var lockedTimes = new HashSet<LockedTime>();
        var lockedTime = new LockedTime(LocalTime.of(10, 0), LocalTime.of(11, 0));
        lockedTimes.add(lockedTime);

        var workDay = AppointmentWorkDay.existing()
                .id(new AppointmentWorkDayId())
                .scheduleId(new ScheduleId())
                .dayOfWeek(DayOfWeek.MONDAY)
                .workingHours(new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0)))
                .lockedTimes(lockedTimes)
                .build();

        lockedTimes.clear();

        assertThat(workDay.lockedTimes()).containsExactly(lockedTime);
    }

    @Test
    void givenLockedTimes_whenGettingLockedTimes_thenReturnsUnmodifiableSet() {
        var workDay = existingWorkDay();

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> workDay.lockedTimes().clear());
    }

    @Test
    void givenNullDayOfWeek_whenChangingDayOfWeek_thenThrowsFieldCannotBeEmptyException() {
        var workDay = existingWorkDay();

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> workDay.changeDayOfWeek(null));
    }

    @Test
    void givenNewDayOfWeek_whenChangingDayOfWeek_thenChangesDayOfWeek() {
        var workDay = existingWorkDay();

        workDay.changeDayOfWeek(DayOfWeek.TUESDAY);

        assertThat(workDay.dayOfWeek()).isEqualTo(DayOfWeek.TUESDAY);
    }

    @Test
    void givenNullWorkingHours_whenChangingWorkingHours_thenThrowsFieldCannotBeEmptyException() {
        var workDay = existingWorkDay();

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> workDay.changeWorkingHours(null));
    }

    @Test
    void givenNewWorkingHoursAccommodatingLockedTimes_whenChangingWorkingHours_thenChangesWorkingHours() {
        var workDay = existingWorkDay();
        var newWorkingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(16, 0));

        workDay.changeWorkingHours(newWorkingHours);

        assertThat(workDay.workingHours()).isEqualTo(newWorkingHours);
    }

    @Test
    void givenLockedTimeAtWorkingHoursStartTime_whenChangingWorkingHours_thenThrowsLockedTimeOutsideWorkingHoursException() {
        var workDay = existingWorkDay();
        var newWorkingHours = new WorkingHours(LocalTime.of(12, 0), LocalTime.of(17, 0));

        assertThatExceptionOfType(LockedTimeOutsideWorkingHoursException.class)
                .isThrownBy(() -> workDay.changeWorkingHours(newWorkingHours));
    }

    @Test
    void givenLockedTimeAtWorkingHoursEndTime_whenChangingWorkingHours_thenThrowsLockedTimeOutsideWorkingHoursException() {
        var workDay = existingWorkDay();
        var newWorkingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(13, 0));

        assertThatExceptionOfType(LockedTimeOutsideWorkingHoursException.class)
                .isThrownBy(() -> workDay.changeWorkingHours(newWorkingHours));
    }

    @Test
    void givenNullLockedTimes_whenChangingLockedTimes_thenThrowsFieldCannotBeEmptyException() {
        var workDay = existingWorkDay();

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> workDay.changeLockedTimes(null));
    }

    @Test
    void givenNullLockedTime_whenChangingLockedTimes_thenThrowsFieldCannotBeEmptyException() {
        var workDay = existingWorkDay();
        var lockedTimes = new HashSet<LockedTime>();
        lockedTimes.add(null);

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> workDay.changeLockedTimes(lockedTimes));
    }

    @Test
    void givenLockedTimesWithinWorkingHours_whenChangingLockedTimes_thenChangesLockedTimes() {
        var workDay = existingWorkDay();
        var lockedTime = new LockedTime(LocalTime.of(10, 0), LocalTime.of(11, 0));

        workDay.changeLockedTimes(Set.of(lockedTime));

        assertThat(workDay.lockedTimes()).containsExactly(lockedTime);
    }

    @Test
    void givenEmptyLockedTimes_whenChangingLockedTimes_thenRemovesAllLockedTimes() {
        var workDay = existingWorkDay();

        workDay.changeLockedTimes(Set.of());

        assertThat(workDay.lockedTimes()).isEmpty();
    }

    @Test
    void givenLockedTimeNotStrictlyWithinWorkingHours_whenChangingLockedTimes_thenThrowsLockedTimeOutsideWorkingHoursException() {
        var workDay = existingWorkDay();
        var lockedTime = new LockedTime(LocalTime.of(9, 0), LocalTime.of(10, 0));

        assertThatExceptionOfType(LockedTimeOutsideWorkingHoursException.class)
                .isThrownBy(() -> workDay.changeLockedTimes(Set.of(lockedTime)));
    }

    @Test
    void givenConflictingLockedTimes_whenChangingLockedTimes_thenThrowsConflictLockedTimeException() {
        var workDay = existingWorkDay();
        var firstLockedTime = new LockedTime(LocalTime.of(10, 0), LocalTime.of(11, 0));
        var conflictingLockedTime = new LockedTime(LocalTime.of(10, 30), LocalTime.of(11, 30));

        assertThatExceptionOfType(ConflictLockedTimeException.class)
                .isThrownBy(() -> workDay.changeLockedTimes(Set.of(firstLockedTime, conflictingLockedTime)));
    }

    @Test
    void givenAdjacentLockedTimes_whenChangingLockedTimes_thenAcceptsBothLockedTimes() {
        var workDay = existingWorkDay();
        var firstLockedTime = new LockedTime(LocalTime.of(10, 0), LocalTime.of(11, 0));
        var adjacentLockedTime = new LockedTime(LocalTime.of(11, 0), LocalTime.of(12, 0));

        workDay.changeLockedTimes(Set.of(firstLockedTime, adjacentLockedTime));

        assertThat(workDay.lockedTimes()).containsExactlyInAnyOrder(firstLockedTime, adjacentLockedTime);
    }

    @Test
    void givenWorkDaysWithSameId_whenComparingForEquality_thenReturnsEqualAndSameHashCode() {
        var workDayId = new AppointmentWorkDayId();
        var firstWorkDay = AppointmentWorkDay.existing()
                .id(workDayId)
                .scheduleId(new ScheduleId())
                .dayOfWeek(DayOfWeek.MONDAY)
                .workingHours(standardWorkingHours())
                .lockedTimes(Set.of(standardLockedTime()))
                .build();
        var secondWorkDay = AppointmentWorkDay.existing()
                .id(workDayId)
                .scheduleId(new ScheduleId())
                .dayOfWeek(DayOfWeek.TUESDAY)
                .workingHours(new WorkingHours(LocalTime.of(10, 0), LocalTime.of(18, 0)))
                .lockedTimes(Set.of())
                .build();

        assertThat(firstWorkDay)
                .isEqualTo(secondWorkDay)
                .hasSameHashCodeAs(secondWorkDay);
    }

    @Test
    void givenWorkDaysWithDifferentIds_whenComparingForEquality_thenReturnsNotEqual() {
        var firstWorkDay = existingWorkDay();
        var secondWorkDay = existingWorkDay();

        assertThat(firstWorkDay).isNotEqualTo(secondWorkDay);
    }

    @Test
    void givenNull_whenComparingWithWorkDay_thenReturnsNotEqual() {
        var workDay = existingWorkDay();

        assertThat(workDay).isNotEqualTo(null);
    }

    @Test
    void givenObjectOfDifferentType_whenComparingWithWorkDay_thenReturnsNotEqual() {
        var workDay = existingWorkDay();

        assertThat(workDay).isNotEqualTo("not a work day");
    }

    private AppointmentWorkDay existingWorkDay() {
        return AppointmentWorkDay.existing()
                .id(new AppointmentWorkDayId())
                .scheduleId(new ScheduleId())
                .dayOfWeek(DayOfWeek.MONDAY)
                .workingHours(standardWorkingHours())
                .lockedTimes(Set.of(standardLockedTime()))
                .build();
    }

    private WorkingHours standardWorkingHours() {
        return new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
    }

    private LockedTime standardLockedTime() {
        return new LockedTime(LocalTime.of(12, 0), LocalTime.of(13, 0));
    }
}
