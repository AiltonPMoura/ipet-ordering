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
                criteriaBuilder.equal(root.get("customer").get("id"), customerId);
    }

    public static Specification<OrderPersistenceEntity> placedAtBetween(OffsetDateTime placedAtFrom, OffsetDateTime placedAtTo) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(placedAtFrom) || Objects.isNull(placedAtTo))
                return criteriaBuilder.conjunction();

            return criteriaBuilder.between(root.get("placedAt"), placedAtFrom, placedAtTo);
        };
    }

    public static Specification<OrderPersistenceEntity> statusEquals(String status) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(status))
                return criteriaBuilder.conjunction();

            return criteriaBuilder.equal(root.get("status"), status.toUpperCase());
        };
    }

}
