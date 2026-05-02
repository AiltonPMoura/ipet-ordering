package br.com.ipet.ordering.infrastructure.persistence.customer;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class CustomerPersistenceSpecification {

    private CustomerPersistenceSpecification(){}

    public static Specification<CustomerPersistenceEntity> firstNameLike(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(firstName))
                return null;

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
        };
    }

    public static Specification<CustomerPersistenceEntity> lastNameLike(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(lastName))
                return null;

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
        };
    }

    public static Specification<CustomerPersistenceEntity> emailLike(String email) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(email))
                return null;

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

}
