package br.com.ipet.ordering.infrastructure.persistence.company;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

@UtilityClass
public class CompanyPersistenceSpecification {

    public static Specification<CompanyPersistenceEntity> name(String companyName) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(companyName))
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("companyName")), "%" + companyName + "%");

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
