package br.com.ipet.ordering.domain.model.booking;

import org.junit.jupiter.api.Test;

import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

class AppointmentBookingStatusTest {
    
    // ============= Transições válidas (positivas) =============

    @Test
    void givenDraft_whenToPaid_thenAllowed() {
        assertThat(DRAFT.canChangeTo(REQUESTED)).isTrue();
    }

    @Test
    void givenPlaced_whenToDraft_thenAllowed() {
        assertThat(REQUESTED.canChangeTo(DRAFT)).isTrue();
    }

    @Test
    void givenScheduled_whenToInProgress_thenAllowed() {
        assertThat(SCHEDULED.canChangeTo(IN_PROGRESS)).isTrue();
    }

    @Test
    void givenScheduled_whenToCanceled_thenAllowed() {
        assertThat(SCHEDULED.canChangeTo(CANCELED)).isTrue();
    }

    @Test
    void givenInProgress_whenToCompleted_thenAllowed() {
        assertThat(IN_PROGRESS.canChangeTo(COMPLETED)).isTrue();
    }

    @Test
    void givenInProgress_whenToCanceled_thenAllowed() {
        assertThat(IN_PROGRESS.canChangeTo(CANCELED)).isTrue();
    }

    // ============= Transições inválidas (negativas) =============

    @Test
    void givenDraft_whenToScheduled_thenNotAllowed() {
        assertThat(DRAFT.canChangeTo(SCHEDULED)).isFalse();
    }

    @Test
    void givenDraft_whenToInProgress_thenNotAllowed() {
        assertThat(DRAFT.canChangeTo(IN_PROGRESS)).isFalse();
    }

    @Test
    void givenDraft_whenToCompleted_thenNotAllowed() {
        assertThat(DRAFT.canChangeTo(COMPLETED)).isFalse();
    }

    @Test
    void givenDraft_whenToCanceled_thenNotAllowed() {
        assertThat(DRAFT.canChangeTo(CANCELED)).isFalse();
    }

    @Test
    void givenPlaced_whenToScheduled_thenNotAllowed() {
        assertThat(REQUESTED.canChangeTo(SCHEDULED)).isFalse();
    }

    @Test
    void givenPlaced_whenToInProgress_thenNotAllowed() {
        assertThat(REQUESTED.canChangeTo(IN_PROGRESS)).isFalse();
    }

    @Test
    void givenPlaced_whenToCompleted_thenNotAllowed() {
        assertThat(REQUESTED.canChangeTo(COMPLETED)).isFalse();
    }

    @Test
    void givenPlaced_whenToCanceled_thenNotAllowed() {
        assertThat(REQUESTED.canChangeTo(CANCELED)).isFalse();
    }

    @Test
    void givenScheduled_whenToDraft_thenNotAllowed() {
        assertThat(SCHEDULED.canChangeTo(DRAFT)).isFalse();
    }

    @Test
    void givenScheduled_whenToPlaced_thenNotAllowed() {
        assertThat(SCHEDULED.canChangeTo(REQUESTED)).isFalse();
    }

    @Test
    void givenScheduled_whenToCompleted_thenNotAllowed() {
        assertThat(SCHEDULED.canChangeTo(COMPLETED)).isFalse();
    }

    @Test
    void givenInProgress_whenToDraft_thenNotAllowed() {
        assertThat(IN_PROGRESS.canChangeTo(DRAFT)).isFalse();
    }

    @Test
    void givenInProgress_whenToPlaced_thenNotAllowed() {
        assertThat(IN_PROGRESS.canChangeTo(REQUESTED)).isFalse();
    }

    @Test
    void givenInProgress_whenToScheduled_thenNotAllowed() {
        assertThat(IN_PROGRESS.canChangeTo(SCHEDULED)).isFalse();
    }

    @Test
    void givenCompleted_whenToDraft_thenNotAllowed() {
        assertThat(COMPLETED.canChangeTo(DRAFT)).isFalse();
    }

    @Test
    void givenCompleted_whenToPlaced_thenNotAllowed() {
        assertThat(COMPLETED.canChangeTo(REQUESTED)).isFalse();
    }

    @Test
    void givenCompleted_whenToScheduled_thenNotAllowed() {
        assertThat(COMPLETED.canChangeTo(SCHEDULED)).isFalse();
    }

    @Test
    void givenCompleted_whenToInProgress_thenNotAllowed() {
        assertThat(COMPLETED.canChangeTo(IN_PROGRESS)).isFalse();
    }

    @Test
    void givenCompleted_whenToCanceled_thenNotAllowed() {
        assertThat(COMPLETED.canChangeTo(CANCELED)).isFalse();
    }

    @Test
    void givenCanceled_whenToDraft_thenNotAllowed() {
        assertThat(CANCELED.canChangeTo(DRAFT)).isFalse();
    }

    @Test
    void givenCanceled_whenToPlaced_thenNotAllowed() {
        assertThat(CANCELED.canChangeTo(REQUESTED)).isFalse();
    }

    @Test
    void givenCanceled_whenToScheduled_thenNotAllowed() {
        assertThat(CANCELED.canChangeTo(SCHEDULED)).isFalse();
    }

    @Test
    void givenCanceled_whenToInProgress_thenNotAllowed() {
        assertThat(CANCELED.canChangeTo(IN_PROGRESS)).isFalse();
    }

    @Test
    void givenCanceled_whenToCompleted_thenNotAllowed() {
        assertThat(CANCELED.canChangeTo(COMPLETED)).isFalse();
    }

    // ============= Transições para si mesmo =============

    @Test
    void givenDraft_whenToDraft_thenNotAllowed() {
        assertThat(DRAFT.canChangeTo(DRAFT)).isFalse();
    }

    @Test
    void givenPlaced_whenToPlaced_thenNotAllowed() {
        assertThat(REQUESTED.canChangeTo(REQUESTED)).isFalse();
    }

    @Test
    void givenScheduled_whenToScheduled_thenNotAllowed() {
        assertThat(SCHEDULED.canChangeTo(SCHEDULED)).isFalse();
    }

    @Test
    void givenInProgress_whenToInProgress_thenNotAllowed() {
        assertThat(IN_PROGRESS.canChangeTo(IN_PROGRESS)).isFalse();
    }

    @Test
    void givenCompleted_whenToCompleted_thenNotAllowed() {
        assertThat(COMPLETED.canChangeTo(COMPLETED)).isFalse();
    }


    @Test
    void givenCanceled_whenToCanceled_thenNotAllowed() {
        assertThat(CANCELED.canChangeTo(CANCELED)).isFalse();
    }

    // ============= Transições com null =============

    @Test
    void givenAny_whenToNull_thenNotAllowed() {
        assertThat(DRAFT.canChangeTo(null)).isFalse();
        assertThat(REQUESTED.canChangeTo(null)).isFalse();
        assertThat(SCHEDULED.canChangeTo(null)).isFalse();
        assertThat(IN_PROGRESS.canChangeTo(null)).isFalse();
        assertThat(COMPLETED.canChangeTo(null)).isFalse();
        assertThat(CANCELED.canChangeTo(null)).isFalse();
    }
}