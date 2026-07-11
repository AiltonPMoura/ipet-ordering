package br.com.ipet.ordering.domain.model.booking;

import org.junit.jupiter.api.Test;

import static br.com.ipet.ordering.domain.model.booking.TimeSlotBookingStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

class TimeSlotBookingStatusTest {
    
    // ============= Transições válidas (positivas) =============

    @Test
    void givenDraft_whenToPaid_thenAllowed() {
        assertThat(DRAFT.canChangeTo(PLACED)).isTrue();
    }

    @Test
    void givenPlaced_whenToDraft_thenAllowed() {
        assertThat(PLACED.canChangeTo(DRAFT)).isTrue();
    }

    @Test
    void givenPlaced_whenToPaid_thenAllowed() {
        assertThat(PLACED.canChangeTo(PAID)).isTrue();
    }

    @Test
    void givenPaid_whenToCanceled_thenAllowed() {
        assertThat(PAID.canChangeTo(CANCELED)).isTrue();
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
    void givenDraft_whenToPaid_thenNotAllowed() {
        assertThat(DRAFT.canChangeTo(PAID)).isFalse();
    }

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
        assertThat(PLACED.canChangeTo(SCHEDULED)).isFalse();
    }

    @Test
    void givenPlaced_whenToInProgress_thenNotAllowed() {
        assertThat(PLACED.canChangeTo(IN_PROGRESS)).isFalse();
    }

    @Test
    void givenPlaced_whenToCompleted_thenNotAllowed() {
        assertThat(PLACED.canChangeTo(COMPLETED)).isFalse();
    }

    @Test
    void givenPlaced_whenToCanceled_thenNotAllowed() {
        assertThat(PLACED.canChangeTo(CANCELED)).isFalse();
    }

    @Test
    void givenPaid_whenToDraft_thenNotAllowed() {
        assertThat(PAID.canChangeTo(DRAFT)).isFalse();
    }

    @Test
    void givenPaid_whenToPlaced_thenNotAllowed() {
        assertThat(PAID.canChangeTo(PLACED)).isFalse();
    }

    @Test
    void givenPaid_whenToScheduled_thenNotAllowed() {
        assertThat(PAID.canChangeTo(SCHEDULED)).isFalse();
    }

    @Test
    void givenPaid_whenToInProgress_thenNotAllowed() {
        assertThat(PAID.canChangeTo(IN_PROGRESS)).isFalse();
    }

    @Test
    void givenPaid_whenToCompleted_thenNotAllowed() {
        assertThat(PAID.canChangeTo(COMPLETED)).isFalse();
    }

    @Test
    void givenScheduled_whenToDraft_thenNotAllowed() {
        assertThat(SCHEDULED.canChangeTo(DRAFT)).isFalse();
    }

    @Test
    void givenScheduled_whenToPlaced_thenNotAllowed() {
        assertThat(SCHEDULED.canChangeTo(PLACED)).isFalse();
    }

    @Test
    void givenScheduled_whenToPaid_thenNotAllowed() {
        assertThat(SCHEDULED.canChangeTo(PAID)).isFalse();
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
        assertThat(IN_PROGRESS.canChangeTo(PLACED)).isFalse();
    }

    @Test
    void givenInProgress_whenToPaid_thenNotAllowed() {
        assertThat(IN_PROGRESS.canChangeTo(PAID)).isFalse();
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
        assertThat(COMPLETED.canChangeTo(PLACED)).isFalse();
    }

    @Test
    void givenCompleted_whenToPaid_thenNotAllowed() {
        assertThat(COMPLETED.canChangeTo(PAID)).isFalse();
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
        assertThat(CANCELED.canChangeTo(PLACED)).isFalse();
    }

    @Test
    void givenCanceled_whenToPaid_thenNotAllowed() {
        assertThat(CANCELED.canChangeTo(PAID)).isFalse();
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
        assertThat(PLACED.canChangeTo(PLACED)).isFalse();
    }

    @Test
    void givenPaid_whenToPaid_thenNotAllowed() {
        assertThat(PAID.canChangeTo(PAID)).isFalse();
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
        assertThat(PLACED.canChangeTo(null)).isFalse();
        assertThat(PAID.canChangeTo(null)).isFalse();
        assertThat(SCHEDULED.canChangeTo(null)).isFalse();
        assertThat(IN_PROGRESS.canChangeTo(null)).isFalse();
        assertThat(COMPLETED.canChangeTo(null)).isFalse();
        assertThat(CANCELED.canChangeTo(null)).isFalse();
    }
}