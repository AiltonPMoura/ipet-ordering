package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.company.CompanyNotFoundException;
import br.com.ipet.ordering.domain.model.schedule.category.ServiceCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AppointmentScheduleRegistrationService {

    private final AppointmentSchedules appointmentSchedules;
    private final Companies companies;

    public AppointmentSchedule register(CompanyId companyId, ScheduleName name, ServiceCategory serviceCategory) {
        if (!companies.exists(companyId))
            throw new CompanyNotFoundException("");

        if (appointmentSchedules.existsByCompanyId(companyId))
            throw new ScheduleAlreadyExistsException();

        return AppointmentSchedule.create(companyId, name, serviceCategory);
    }

    public void removeWorkingDay(AppointmentSchedule appointmentSchedule,
                                 WorkingDayTimeId workingDayTimeId,
                                 CompanyId companyId) {

        appointmentSchedule.removeWorkingDayTime(workingDayTimeId, companyId);
    }

}
