package br.com.ipet.ordering.domain.model.agenda;

import br.com.ipet.ordering.domain.model.Repository;
import br.com.ipet.ordering.domain.model.customer.CompanyId;

public interface Agendas extends Repository<Agenda, AgendaId> {

    boolean existsByCompanyId(CompanyId companyId);
}
