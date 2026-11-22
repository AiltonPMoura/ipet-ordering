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
    void givenPastAndFutureLockedDates_whenChangingLockedDates_thenPreservesPastAndReplacesFuture() {
        var today = LocalDate.now(ZoneOffset.UTC);
        var pastDate = new LockedDate(today.minusDays(1));
        var oldFutureDate = new LockedDate(today.plusDays(1));
        var newFutureDate = new LockedDate(today.plusDays(2));
        var schedule = existingSchedule(Set.of(pastDate, oldFutureDate));

        schedule.changeLockedDates(Set.of(newFutureDate), new CompanyId());

        assertThat(schedule.lockedDates()).containsExactlyInAnyOrder(pastDate, newFutureDate);
    }

    @Test
    void givenPastAndFutureLockedDates_whenChangingLockedDatesToEmpty_thenPreservesPastAndRemovesFuture() {
        var today = LocalDate.now(ZoneOffset.UTC);
        var pastDate = new LockedDate(today.minusDays(1));
        var futureDate = new LockedDate(today.plusDays(1));
        var schedule = existingSchedule(Set.of(pastDate, futureDate));

        schedule.changeLockedDates(Set.of(), new CompanyId());

        assertThat(schedule.lockedDates()).containsExactly(pastDate);
    }

    @Test
    void givenTomorrowLockedDate_whenChangingLockedDates_thenAddsLockedDate() {
        var tomorrow = new LockedDate(LocalDate.now(ZoneOffset.UTC).plusDays(1));
        var schedule = existingSchedule(Set.of());

        schedule.changeLockedDates(Set.of(tomorrow), new CompanyId());

        assertThat(schedule.lockedDates()).containsExactly(tomorrow);
    }

    @Test
    void givenTodayLockedDate_whenChangingLockedDates_thenThrowsDateMustBeAfterNow() {
        var today = new LockedDate(LocalDate.now(ZoneOffset.UTC));
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(DateMustBeAfterNow.class)
                .isThrownBy(() -> schedule.changeLockedDates(Set.of(today), new CompanyId()));
    }

    @Test
    void givenPastLockedDate_whenChangingLockedDates_thenThrowsDateMustBeAfterNow() {
        var pastDate = new LockedDate(LocalDate.now(ZoneOffset.UTC).minusDays(1));
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(DateMustBeAfterNow.class)
                .isThrownBy(() -> schedule.changeLockedDates(Set.of(pastDate), new CompanyId()));
    }

    @Test
    void givenNullLockedDates_whenChangingLockedDates_thenThrowsFieldCannotBeEmptyException() {
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> schedule.changeLockedDates(null, new CompanyId()));
    }

    @Test
    void givenNullLockedDate_whenChangingLockedDates_thenThrowsFieldCannotBeEmptyException() {
        var lockedDates = new HashSet<LockedDate>();
        lockedDates.add(null);
        var schedule = existingSchedule(Set.of());

        assertThatExceptionOfType(FieldCannotBeEmptyException.class)
                .isThrownBy(() -> schedule.changeLockedDates(lockedDates, new CompanyId()));
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
