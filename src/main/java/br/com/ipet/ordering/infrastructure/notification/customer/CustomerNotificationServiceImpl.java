package br.com.ipet.ordering.infrastructure.notification.customer;

import br.com.ipet.ordering.application.customer.notification.NotificationRegisterInput;
import br.com.ipet.ordering.application.customer.notification.CustomerNotificationService;
import br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerNotificationServiceImpl implements CustomerNotificationService {

    private final CustomerPersistenceRepository customerRepository;

    @Override
    public void notifyNewResgistration(NotificationRegisterInput input) {
        log.info("Welcome {}", input.firstName());
        log.info("Use your email: {} to access your account", input.email());
    }
}
