package br.com.ipet.ordering.infrastructure.persistence.company;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

@UtilityClass
public class CompanyPersistenceSpecification {

    public static Specification<CompanyPersistenceEntity> name(String name) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(name))
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + name + "%");

            return null;
        };
    }

    public static Specification<CompanyPersistenceEntity> email(String email) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(email))
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email + "%");

            return null;
        };
    }

}
