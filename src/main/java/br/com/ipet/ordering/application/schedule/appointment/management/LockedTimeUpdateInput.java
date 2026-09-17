package br.com.ipet.ordering.application.schedule.appointment.management;

public record LockedTimeUpdateInput(LockedTimeInput oldLockedTime, LockedTimeInput newLockedTime) {
}
