package br.com.ipet.ordering.infrastructure.persistence.order;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public final class OrderPersistenceSpecification {

    private OrderPersistenceSpecification(){}

    public static Specification<OrderPersistenceEntity> customerIdEquals(UUID customerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("customer").get("customerId"), customerId);
    }

    public static Specification<OrderPersistenceEntity> placedAtBetween(OffsetDateTime startTime, OffsetDateTime endTime) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(startTime) || Objects.isNull(endTime))
                return criteriaBuilder.conjunction();

            return criteriaBuilder.between(root.get("placedAt"), startTime, endTime);
        };
    }

    public static Specification<OrderPersistenceEntity> statusEquals(String status) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(status))
                return criteriaBuilder.conjunction();

            return criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")), status.toUpperCase());
        };
    }

}
