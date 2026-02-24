package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.Repository;
import br.com.ipet.ordering.domain.model.customer.CompanyId;

public interface Schedules extends Repository<Schedule, ScheduleId> {

    boolean existsByCompanyId(CompanyId companyId);
}
