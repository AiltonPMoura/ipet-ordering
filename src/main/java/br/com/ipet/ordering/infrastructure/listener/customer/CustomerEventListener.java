package br.com.ipet.ordering.infrastructure.listener.customer;

import br.com.ipet.ordering.application.customer.notification.NotificationRegisterInput;
import br.com.ipet.ordering.application.customer.notification.CustomerNotificationService;
import br.com.ipet.ordering.domain.model.customer.CustomerRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventListener {

    private final CustomerNotificationService customerNotificationService;

    @EventListener
    public void onRegistered(CustomerRegisteredEvent event) {
        customerNotificationService.notifyNewResgistration(new NotificationRegisterInput(
                event.customerId().value(),
                event.fullName().firstName(),
                event.email().value())
        );
    }

}
