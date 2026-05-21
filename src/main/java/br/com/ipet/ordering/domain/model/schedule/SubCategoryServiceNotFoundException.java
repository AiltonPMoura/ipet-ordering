package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;

import java.util.UUID;

public class SubCategoryServiceNotFoundException extends DomainException {
    public SubCategoryServiceNotFoundException(UUID companyId) {
        super(companyId.toString());
    }
}
