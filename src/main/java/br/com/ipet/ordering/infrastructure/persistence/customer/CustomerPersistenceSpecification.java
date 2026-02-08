package br.com.ipet.ordering.infrastructure.persistence.customer;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

@UtilityClass
public class CustomerPersistenceSpecification {

    public static Specification<CustomerPersistenceEntity> firstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(firstName))
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName + "%");

            return null;
        };
    }

    public static Specification<CustomerPersistenceEntity> lastName(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(lastName))
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName + "%");

            return null;
        };
    }

    public static Specification<CustomerPersistenceEntity> email(String email) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(email))
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email + "%");

            return null;
        };
    }

}
