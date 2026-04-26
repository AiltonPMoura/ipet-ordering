package br.com.ipet.ordering.application.customer.notification;

import java.util.UUID;

public record NotificationRegisterInput(UUID customerId,
                                        String firstName,
                                        String email) {
}
