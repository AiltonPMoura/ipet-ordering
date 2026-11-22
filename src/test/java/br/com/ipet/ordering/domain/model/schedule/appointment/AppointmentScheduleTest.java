package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.commons.exception.FieldCannotBeEmptyException;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import br.com.ipet.ordering.domain.model.commons.id.ScheduleId;
import br.com.ipet.ordering.domain.model.schedule.ScheduleDontSupportSubcategoryException;
import br.com.ipet.ordering.domain.model.schedule.ScheduleName;
import br.com.ipet.ordering.domain.model.schedule.ScheduleStatus;
import br.com.ipet.ordering.domain.model.schedule.ServiceCategory;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class AppointmentScheduleTest {

    @Test
    void givenSupportedServiceCategory_whenCreatingAppointmentSchedule_thenCreatesInactiveSchedule() {
        var companyId = new CompanyId();
        var name = new ScheduleName("Appointment schedule");

        var schedule = AppointmentSchedule.create(companyId, name, ServiceCategory.HYGIENE);

        assertThat(schedule.companyId()).isEqualTo(companyId);
        assertThat(schedule.name()).isEqualTo(name);
        assertThat(schedule.serviceCategory()).isEqualTo(ServiceCategory.HYGIENE);
        assertThat(schedule.status()).isEqualTo(ScheduleStatus.INACTIVE);
        assertThat(schedule.lockedDates()).isEmpty();
        assertThat(schedule.workDays()).isEmpty();
    }

    @Test
    void givenUnsupportedServiceCategory_whenCreatingAppointmentSchedule_thenThrowsScheduleDontSupportSubcategoryException() {
        assertThatExceptionOfType(ScheduleDontSupportSubcategoryException.class)
                .isThrownBy(() -> AppointmentSchedule.create(
                        new CompanyId(), new ScheduleName("Appointment schedule"), ServiceCategory.DAYCARE));
    }

    @Test
    void givenNullServiceCategory_whenCreatingAppointmentSchedule_thenThrowsFieldCannotBeEmptyException() {
        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> AppointmentSchedule.create(
                        new CompanyId(), new ScheduleName("Appointment schedule"), null));
    }

    @Test
    void givenWorkDays_whenCreatingExistingAppointmentSchedule_thenCopiesWorkDays() {
        var workDay = existingWorkDay(DayOfWeek.MONDAY);
        var workDays = new HashSet<>(Set.of(workDay));
        var schedule = existingSchedule(workDays);

        workDays.clear();

        assertThat(schedule.workDays()).containsExactly(workDay);
    }

    @Test
    void givenWorkDays_whenGettingWorkDays_thenReturnsUnmodifiableSet() {
        var schedule = existingSchedule(Set.of(existingWorkDay(DayOfWeek.MONDAY)));

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> schedule.workDays().clear());
    }

    @Test
    void givenNullWorkDays_whenCreatingExistingAppointmentSchedule_thenThrowsFieldCannotBeEmptyException() {
        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> existingSchedule(null));
    }

    @Test
    void givenNoWorkDayForWeekday_whenAddingWorkDay_thenAddsWorkDay() {
        var schedule = existingSchedule(Set.of());
        var workingHours = standardWorkingHours();

        schedule.addWorkDay(DayOfWeek.MONDAY, workingHours, new CompanyId());

        assertThat(schedule.workDays())
                .singleElement()
                .satisfies(workDay -> {
                    assertThat(workDay.scheduleId()).isEqualTo(schedule.id());
                    assertThat(workDay.dayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
                    assertThat(workDay.workingHours()).isEqualTo(workingHours);
                    assertThat(workDay.lockedTimes()).isEmpty();
                });
    }

    @Test
    void givenExistingWorkDayForWeekday_whenAddingWorkDay_thenThrowsWorkDayAlreadyExistsException() {
        var schedule = existingSchedule(Set.of(existingWorkDay(DayOfWeek.MONDAY)));

        assertThatExceptionOfType(WorkDayAlreadyExistsException.class)
                .isThrownBy(() -> schedule.addWorkDay(DayOfWeek.MONDAY, standardWorkingHours(), new CompanyId()));
    }

    @Test
    void givenNullDayOfWeek_whenAddingWorkDay_thenThrowsFieldCannotBeEmptyException() {
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> schedule.addWorkDay(null, standardWorkingHours(), new CompanyId()));
    }

    @Test
    void givenNullWorkingHours_whenAddingWorkDay_thenThrowsFieldCannotBeEmptyException() {
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> schedule.addWorkDay(DayOfWeek.MONDAY, null, new CompanyId()));
    }

    @Test
    void givenExistingWorkDay_whenRemovingWorkDay_thenRemovesWorkDay() {
        var workDay = existingWorkDay(DayOfWeek.MONDAY);
        var schedule = existingSchedule(Set.of(workDay));

        schedule.removeWorkDay(workDay.id(), new CompanyId());

        assertThat(schedule.workDays()).isEmpty();
    }

    @Test
    void givenUnknownWorkDayId_whenRemovingWorkDay_thenThrowsWorkinDayNotFoundException() {
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(WorkinDayNotFoundException.class)
                .isThrownBy(() -> schedule.removeWorkDay(new AppointmentWorkDayId(), new CompanyId()));
    }

    @Test
    void givenNullWorkDayId_whenRemovingWorkDay_thenThrowsFieldCannotBeEmptyException() {
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> schedule.removeWorkDay(null, new CompanyId()));
    }

    @Test
    void givenExistingWorkDay_whenChangingDayOfWeek_thenChangesDayOfWeek() {
        var workDay = existingWorkDay(DayOfWeek.MONDAY);
        var schedule = existingSchedule(Set.of(workDay));

        schedule.changeWorkDayOfWeek(workDay.id(), DayOfWeek.TUESDAY, new CompanyId());

        assertThat(workDay.dayOfWeek()).isEqualTo(DayOfWeek.TUESDAY);
    }

    @Test
    void givenSameWorkDay_whenChangingToItsCurrentDayOfWeek_thenKeepsDayOfWeek() {
        var workDay = existingWorkDay(DayOfWeek.MONDAY);
        var schedule = existingSchedule(Set.of(workDay));

        schedule.changeWorkDayOfWeek(workDay.id(), DayOfWeek.MONDAY, new CompanyId());

        assertThat(workDay.dayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
    }

    @Test
    void givenAnotherWorkDayAlreadyUsesRequestedDay_whenChangingDayOfWeek_thenThrowsWorkDayAlreadyExistsException() {
        var mondayWorkDay = existingWorkDay(DayOfWeek.MONDAY);
        var tuesdayWorkDay = existingWorkDay(DayOfWeek.TUESDAY);
        var schedule = existingSchedule(Set.of(mondayWorkDay, tuesdayWorkDay));

        assertThatExceptionOfType(WorkDayAlreadyExistsException.class)
                .isThrownBy(() -> schedule.changeWorkDayOfWeek(mondayWorkDay.id(), DayOfWeek.TUESDAY, new CompanyId()));
    }

    @Test
    void givenUnknownWorkDayId_whenChangingDayOfWeek_thenThrowsWorkinDayNotFoundException() {
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(WorkinDayNotFoundException.class)
                .isThrownBy(() -> schedule.changeWorkDayOfWeek(new AppointmentWorkDayId(), DayOfWeek.MONDAY, new CompanyId()));
    }

    @Test
    void givenNullDayOfWeek_whenChangingDayOfWeek_thenThrowsFieldCannotBeEmptyException() {
        var workDay = existingWorkDay(DayOfWeek.MONDAY);
        var schedule = existingSchedule(Set.of(workDay));

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> schedule.changeWorkDayOfWeek(workDay.id(), null, new CompanyId()));
    }

    @Test
    void givenExistingWorkDay_whenChangingWorkingHours_thenChangesWorkingHours() {
        var workDay = existingWorkDay(DayOfWeek.MONDAY);
        var schedule = existingSchedule(Set.of(workDay));
        var newWorkingHours = new WorkingHours(LocalTime.of(9, 0), LocalTime.of(16, 0));

        schedule.changeWorkDayHours(workDay.id(), newWorkingHours, new CompanyId());

        assertThat(workDay.workingHours()).isEqualTo(newWorkingHours);
    }

    @Test
    void givenUnknownWorkDayId_whenChangingWorkingHours_thenThrowsWorkinDayNotFoundException() {
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(WorkinDayNotFoundException.class)
                .isThrownBy(() -> schedule.changeWorkDayHours(new AppointmentWorkDayId(), standardWorkingHours(), new CompanyId()));
    }

    @Test
    void givenNullWorkingHours_whenChangingWorkingHours_thenThrowsFieldCannotBeEmptyException() {
        var workDay = existingWorkDay(DayOfWeek.MONDAY);
        var schedule = existingSchedule(Set.of(workDay));

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> schedule.changeWorkDayHours(workDay.id(), null, new CompanyId()));
    }

    @Test
    void givenExistingWorkDay_whenChangingLockedTimes_thenChangesLockedTimes() {
        var workDay = existingWorkDay(DayOfWeek.MONDAY);
        var schedule = existingSchedule(Set.of(workDay));
        var newLockedTime = new LockedTime(LocalTime.of(10, 0), LocalTime.of(11, 0));

        schedule.changeWorkDayLockedTime(workDay.id(), Set.of(newLockedTime), new CompanyId());

        assertThat(workDay.lockedTimes()).containsExactly(newLockedTime);
    }

    @Test
    void givenEmptyLockedTimes_whenChangingLockedTimes_thenRemovesExistingLockedTimes() {
        var workDay = existingWorkDay(DayOfWeek.MONDAY);
        var schedule = existingSchedule(Set.of(workDay));

        schedule.changeWorkDayLockedTime(workDay.id(), Set.of(), new CompanyId());

        assertThat(workDay.lockedTimes()).isEmpty();
    }

    @Test
    void givenUnknownWorkDayId_whenChangingLockedTimes_thenThrowsWorkinDayNotFoundException() {
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(WorkinDayNotFoundException.class)
                .isThrownBy(() -> schedule.changeWorkDayLockedTime(new AppointmentWorkDayId(), Set.of(), new CompanyId()));
    }

    @Test
    void givenNullLockedTimes_whenChangingLockedTimes_thenThrowsFieldCannotBeEmptyException() {
        var workDay = existingWorkDay(DayOfWeek.MONDAY);
        var schedule = existingSchedule(Set.of(workDay));

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> schedule.changeWorkDayLockedTime(workDay.id(), null, new CompanyId()));
    }

    private AppointmentSchedule existingSchedule(Set<AppointmentWorkDay> workDays) {
        return AppointmentSchedule.existing()
                .id(new ScheduleId())
                .companyId(new CompanyId())
                .name(new ScheduleName("Appointment schedule"))
                .serviceCategory(ServiceCategory.HYGIENE)
                .status(ScheduleStatus.INACTIVE)
                .lockedDates(Set.of())
                .createdAt(OffsetDateTime.now(ZoneOffset.UTC))
                .workDays(workDays)
                .build();
    }

    private AppointmentWorkDay existingWorkDay(DayOfWeek dayOfWeek) {
        return AppointmentWorkDay.existing()
                .id(new AppointmentWorkDayId())
                .scheduleId(new ScheduleId())
                .dayOfWeek(dayOfWeek)
                .workingHours(standardWorkingHours())
                .lockedTimes(Set.of())
                .build();
    }

    private WorkingHours standardWorkingHours() {
        return new WorkingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
    }
}
