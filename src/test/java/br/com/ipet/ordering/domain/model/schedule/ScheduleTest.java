package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.commons.exception.CannotChangeStatusException;
import br.com.ipet.ordering.domain.model.commons.exception.DateMustBeAfterNow;
import br.com.ipet.ordering.domain.model.commons.exception.FieldCannotBeEmptyException;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import br.com.ipet.ordering.domain.model.commons.id.ScheduleId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ScheduleTest {

    @Test
    void givenNewName_whenChangingName_thenChangesName() {
        var schedule = existingSchedule(Set.of());
        var newName = new ScheduleName("New schedule name");

        schedule.changeName(newName, new CompanyId());

        assertThat(schedule.name()).isEqualTo(newName);
    }

    @Test
    void givenNullName_whenChangingName_thenThrowsFieldCannotBeEmptyException() {
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> schedule.changeName(null, new CompanyId()));
    }

    @Test
    void givenLockedDates_whenCreatingExistingSchedule_thenCopiesLockedDates() {
        var lockedDate = new LockedDate(LocalDate.now(ZoneOffset.UTC).minusDays(1));
        var lockedDates = new HashSet<>(Set.of(lockedDate));
        var schedule = existingSchedule(lockedDates);

        lockedDates.clear();

        assertThat(schedule.lockedDates()).containsExactly(lockedDate);
    }

    @Test
    void givenLockedDates_whenGettingLockedDates_thenReturnsUnmodifiableSet() {
        var schedule = existingSchedule(Set.of(new LockedDate(LocalDate.now(ZoneOffset.UTC).minusDays(1))));

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> schedule.lockedDates().clear());
    }

    @Test
    void givenExistingLockedDates_whenAddingLockedDates_thenAddsDatesWithoutReplacingExistingOnes() {
        var today = LocalDate.now(ZoneOffset.UTC);
        var existingPastDate = new LockedDate(today.minusDays(1));
        var existingFutureDate = new LockedDate(today.plusDays(1));
        var newFutureDate = new LockedDate(today.plusDays(2));
        var schedule = existingSchedule(Set.of(existingPastDate, existingFutureDate));

        schedule.addLockedDates(Set.of(newFutureDate), new CompanyId());

        assertThat(schedule.lockedDates())
                .containsExactlyInAnyOrder(existingPastDate, existingFutureDate, newFutureDate);
    }

    @Test
    void givenExistingLockedDates_whenRemovingLockedDates_thenRemovesOnlySpecifiedFutureDates() {
        var today = LocalDate.now(ZoneOffset.UTC);
        var pastDate = new LockedDate(today.minusDays(1));
        var dateToRemove = new LockedDate(today.plusDays(1));
        var dateToKeep = new LockedDate(today.plusDays(2));
        var schedule = existingSchedule(Set.of(pastDate, dateToRemove, dateToKeep));

        schedule.removeLockedDates(Set.of(dateToRemove), new CompanyId());

        assertThat(schedule.lockedDates()).containsExactlyInAnyOrder(pastDate, dateToKeep);
    }

    @Test
    void givenExistingLockedDate_whenRemovingLockedDates_thenRemovesDate() {
        var existingDate = new LockedDate(LocalDate.now(ZoneOffset.UTC).plusDays(1));
        var schedule = existingSchedule(Set.of(existingDate));

        schedule.removeLockedDates(Set.of(existingDate), new CompanyId());

        assertThat(schedule.lockedDates()).isEmpty();
    }

    @Test
    void givenExistingLockedDate_whenAddingLockedDates_thenDoesNotDuplicateDate() {
        var existingDate = new LockedDate(LocalDate.now(ZoneOffset.UTC).plusDays(1));
        var schedule = existingSchedule(Set.of(existingDate));

        schedule.addLockedDates(Set.of(existingDate), new CompanyId());

        assertThat(schedule.lockedDates()).containsExactly(existingDate);
    }

    @Test
    void givenExistingLockedDates_whenAddingEmptySet_thenKeepsLockedDates() {
        var existingDate = new LockedDate(LocalDate.now(ZoneOffset.UTC).plusDays(1));
        var schedule = existingSchedule(Set.of(existingDate));

        schedule.addLockedDates(Set.of(), new CompanyId());

        assertThat(schedule.lockedDates()).containsExactly(existingDate);
    }

    @Test
    void givenExistingLockedDates_whenRemovingEmptySet_thenKeepsLockedDates() {
        var existingDate = new LockedDate(LocalDate.now(ZoneOffset.UTC).plusDays(1));
        var schedule = existingSchedule(Set.of(existingDate));

        schedule.removeLockedDates(Set.of(), new CompanyId());

        assertThat(schedule.lockedDates()).containsExactly(existingDate);
    }

    @Test
    void givenLockedDateNotInSchedule_whenRemovingLockedDates_thenKeepsExistingLockedDates() {
        var existingDate = new LockedDate(LocalDate.now(ZoneOffset.UTC).plusDays(1));
        var absentDate = new LockedDate(LocalDate.now(ZoneOffset.UTC).plusDays(2));
        var schedule = existingSchedule(Set.of(existingDate));

        schedule.removeLockedDates(Set.of(absentDate), new CompanyId());

        assertThat(schedule.lockedDates()).containsExactly(existingDate);
    }

    @Test
    void givenTodayLockedDate_whenAddingLockedDates_thenThrowsDateMustBeAfterNow() {
        var today = new LockedDate(LocalDate.now(ZoneOffset.UTC));
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(DateMustBeAfterNow.class)
                .isThrownBy(() -> schedule.addLockedDates(Set.of(today), new CompanyId()));
    }

    @Test
    void givenValidAndTodayLockedDates_whenAddingLockedDates_thenDoesNotAddAnyDate() {
        var today = LocalDate.now(ZoneOffset.UTC);
        var existingDate = new LockedDate(today.plusDays(3));
        var validDate = new LockedDate(today.plusDays(1));
        var invalidDate = new LockedDate(today);
        var schedule = existingSchedule(Set.of(existingDate));

        assertThatExceptionOfType(DateMustBeAfterNow.class)
                .isThrownBy(() -> schedule.addLockedDates(Set.of(validDate, invalidDate), new CompanyId()));

        assertThat(schedule.lockedDates()).containsExactly(existingDate);
    }

    @Test
    void givenTodayLockedDate_whenRemovingLockedDates_thenThrowsDateMustBeAfterNow() {
        var today = new LockedDate(LocalDate.now(ZoneOffset.UTC));
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(DateMustBeAfterNow.class)
                .isThrownBy(() -> schedule.removeLockedDates(Set.of(today), new CompanyId()));
    }

    @Test
    void givenValidAndTodayLockedDates_whenRemovingLockedDates_thenDoesNotRemoveAnyDate() {
        var today = LocalDate.now(ZoneOffset.UTC);
        var existingDate = new LockedDate(today.plusDays(1));
        var anotherExistingDate = new LockedDate(today.plusDays(2));
        var invalidDate = new LockedDate(today);
        var schedule = existingSchedule(Set.of(existingDate, anotherExistingDate));

        assertThatExceptionOfType(DateMustBeAfterNow.class)
                .isThrownBy(() -> schedule.removeLockedDates(Set.of(existingDate, invalidDate), new CompanyId()));

        assertThat(schedule.lockedDates()).containsExactlyInAnyOrder(existingDate, anotherExistingDate);
    }

    @Test
    void givenNullLockedDates_whenAddingLockedDates_thenThrowsFieldCannotBeEmptyException() {
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> schedule.addLockedDates(null, new CompanyId()));
    }

    @Test
    void givenNullLockedDates_whenRemovingLockedDates_thenThrowsFieldCannotBeEmptyException() {
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> schedule.removeLockedDates(null, new CompanyId()));
    }

    @Test
    void givenNullLockedDate_whenAddingLockedDates_thenThrowsFieldCannotBeEmptyException() {
        var lockedDates = new HashSet<LockedDate>();
        lockedDates.add(null);
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> schedule.addLockedDates(lockedDates, new CompanyId()));
    }

    @Test
    void givenNullLockedDate_whenRemovingLockedDates_thenThrowsFieldCannotBeEmptyException() {
        var lockedDates = new HashSet<LockedDate>();
        lockedDates.add(null);
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> schedule.removeLockedDates(lockedDates, new CompanyId()));
    }

    @Test
    void givenPastLockedDate_whenAddingLockedDates_thenThrowsDateMustBeAfterNow() {
        var pastDate = new LockedDate(LocalDate.now(ZoneOffset.UTC).minusDays(1));
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(DateMustBeAfterNow.class)
                .isThrownBy(() -> schedule.addLockedDates(Set.of(pastDate), new CompanyId()));
    }

    @Test
    void givenPastLockedDate_whenRemovingLockedDates_thenThrowsDateMustBeAfterNow() {
        var pastDate = new LockedDate(LocalDate.now(ZoneOffset.UTC).minusDays(1));
        var schedule = existingSchedule(Set.of(pastDate));

        assertThatExceptionOfType(DateMustBeAfterNow.class)
                .isThrownBy(() -> schedule.removeLockedDates(Set.of(pastDate), new CompanyId()));
    }

    @Test
    void givenInactiveSchedule_whenActivating_thenBecomesActive() {
        var schedule = existingSchedule(Set.of());

        schedule.activate(new CompanyId());

        assertThat(schedule.status()).isEqualTo(ScheduleStatus.ACTIVE);
        assertThat(schedule.isActive()).isTrue();
        assertThat(schedule.isInactive()).isFalse();
    }

    @Test
    void givenActiveSchedule_whenActivating_thenThrowsCannotChangeStatusException() {
        var schedule = existingSchedule(Set.of(), ScheduleStatus.ACTIVE);

        assertThatExceptionOfType(CannotChangeStatusException.class)
                .isThrownBy(() -> schedule.activate(new CompanyId()));
    }

    @Test
    void givenActiveSchedule_whenDeactivating_thenBecomesInactive() {
        var schedule = existingSchedule(Set.of(), ScheduleStatus.ACTIVE);

        schedule.deactivate(new CompanyId());

        assertThat(schedule.status()).isEqualTo(ScheduleStatus.INACTIVE);
        assertThat(schedule.isInactive()).isTrue();
        assertThat(schedule.isActive()).isFalse();
    }

    @Test
    void givenInactiveSchedule_whenDeactivating_thenThrowsCannotChangeStatusException() {
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(CannotChangeStatusException.class)
                .isThrownBy(() -> schedule.deactivate(new CompanyId()));
    }

    private ScheduleStub existingSchedule(Set<LockedDate> lockedDates) {
        return existingSchedule(lockedDates, ScheduleStatus.INACTIVE);
    }

    private ScheduleStub existingSchedule(Set<LockedDate> lockedDates, ScheduleStatus status) {
        return new ScheduleStub(
                new ScheduleId(),
                new CompanyId(),
                new ScheduleName("Schedule"),
                ServiceCategory.HYGIENE,
                status,
                lockedDates,
                OffsetDateTime.now(ZoneOffset.UTC));
    }

    private static class ScheduleStub extends Schedule {
        private ScheduleStub(ScheduleId id, CompanyId companyId, ScheduleName name,
                             ServiceCategory serviceCategory, ScheduleStatus status,
                             Set<LockedDate> lockedDates, OffsetDateTime createdAt) {
            super(id, companyId, name, serviceCategory, status, lockedDates, createdAt);
        }
    }
}
