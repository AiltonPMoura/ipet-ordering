package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.Repository;
import br.com.ipet.ordering.domain.model.company.CompanyId;

public interface DayTimeSchedules extends Repository<DayTimeSchedule, ScheduleId> {

    boolean existsByCompanyId(CompanyId companyId);
}
