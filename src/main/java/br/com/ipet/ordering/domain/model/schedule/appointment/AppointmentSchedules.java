package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.Repository;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import br.com.ipet.ordering.domain.model.commons.id.ScheduleId;

public interface AppointmentSchedules extends Repository<AppointmentSchedule, ScheduleId> {

    boolean existsByCompany(CompanyId companyId);

}
