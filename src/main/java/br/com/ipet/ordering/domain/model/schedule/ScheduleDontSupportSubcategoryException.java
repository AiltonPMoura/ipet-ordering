package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;

public class ScheduleDontSupportSubcategoryException extends DomainException {
    public ScheduleDontSupportSubcategoryException(ServiceCategory category) {
        super(category.name());
    }
}
