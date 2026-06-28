package br.com.ipet.ordering.infrastructure.persistence.customer;

import br.com.ipet.ordering.application.customer.query.CustomerAddressOutput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CustomerPersistenceRepository
        extends JpaRepository<CustomerPersistenceEntity, UUID>,
        JpaSpecificationExecutor<CustomerPersistenceEntity> {

    boolean existsByEmailAndIdNot(String email, UUID id);

    @Query("""
       SELECT new br.com.ipet.ordering.application.customer.query.CustomerAddressOutput(
           customerAddresses.id,
           customerAddresses.address.street,
           customerAddresses.address.number,
           customerAddresses.address.neighborhood,
           customerAddresses.address.complement,
           customerAddresses.address.city,
           customerAddresses.address.state,
           customerAddresses.address.zipCode,
           customerAddresses.isPrincipal
       )
       FROM CustomerPersistenceEntity customer
       JOIN customer.customerAddresses customerAddresses
       WHERE customer.id = :customerId
       """)
    List<CustomerAddressOutput> findAddressesByCustomerId(UUID customerId);

}
