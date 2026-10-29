package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.company.CompanyNotFoundException;
import br.com.ipet.ordering.domain.model.schedule.ScheduleAlreadyExistsException;
import br.com.ipet.ordering.domain.model.schedule.ScheduleName;
import br.com.ipet.ordering.domain.model.schedule.ServiceCategory;
import br.com.ipet.ordering.domain.model.transport.PetTransportNotFoundException;
import br.com.ipet.ordering.domain.model.transport.PetTransports;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AppointmentScheduleRegistrationService {

    private final AppointmentSchedules appointmentSchedules;
    private final Companies companies;
    private final PetTransports transport;

    public AppointmentSchedule register(CompanyId companyId, ScheduleName name, ServiceCategory serviceCategory) {
        if (!companies.exists(companyId))
            throw new CompanyNotFoundException("");

        if (!transport.existsByCompany(companyId))
            throw new PetTransportNotFoundException("");

        if (appointmentSchedules.existsByCompany(companyId))
            throw new ScheduleAlreadyExistsException();

        return AppointmentSchedule.create(companyId, name, serviceCategory);
    }

}
