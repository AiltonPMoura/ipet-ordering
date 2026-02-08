package br.com.ipet.ordering.infrastructure.persistence.pet;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.UUID;

@UtilityClass
public class PetPersistenceSpecification {

    public static Specification<PetPersistenceEntity> customerId(UUID customerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal((root.get("customer").get("id")), customerId);
    }

    public static Specification<PetPersistenceEntity> name(String name) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(name))
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name + "%");

            return null;
        };
    }

    public static Specification<PetPersistenceEntity> type(String type) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(type))
                return criteriaBuilder.equal(criteriaBuilder.upper(root.get("type")), type.toUpperCase());

            return null;
        };
    }

    public static Specification<PetPersistenceEntity> size(String size) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(size))
                return criteriaBuilder.equal(criteriaBuilder.upper(root.get("size")), size.toUpperCase());

            return null;
        };
    }

    public static Specification<PetPersistenceEntity> breed(String breed) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(breed))
                return criteriaBuilder.equal(criteriaBuilder.upper(root.get("breed")), breed.toUpperCase());

            return null;
        };
    }

}
