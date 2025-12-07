package br.com.ipet.ordering.domain.model.entity;

import br.com.ipet.ordering.domain.model.valueobject.AgendaId;
import br.com.ipet.ordering.domain.model.valueobject.CompanyId;
import br.com.ipet.ordering.domain.model.valueobject.WorkingDay;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Agenda {
    private AgendaId id;
    private CompanyId companyId;
    private Set<WorkingDay> workingDays;

}
