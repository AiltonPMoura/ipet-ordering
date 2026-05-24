package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.schedule.category.ServiceCategory;

public class ScheduleDontSupportSubcategoryException extends DomainException {
    public ScheduleDontSupportSubcategoryException(ServiceCategory category) {
        super(category.name());
    }
}
