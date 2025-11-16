package br.com.ipet.ordering.domain.model;

import br.com.ipet.ordering.domain.valueobject.AgendaId;
import br.com.ipet.ordering.domain.valueobject.CompanyId;
import br.com.ipet.ordering.domain.valueobject.WorkingDay;
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
