package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.Repository;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.ScheduleId;

public interface AppointmentSchedules extends Repository<AppointmentSchedule, ScheduleId> {

    boolean existsByCompanyId(CompanyId companyId);

}
