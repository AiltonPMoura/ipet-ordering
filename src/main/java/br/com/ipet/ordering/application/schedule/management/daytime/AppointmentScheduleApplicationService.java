package br.com.ipet.ordering.application.schedule.management.daytime;

import br.com.ipet.ordering.application.schedule.management.ScheduleInput;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.AppointmentScheduleRegistrationService;
import br.com.ipet.ordering.domain.model.schedule.AppointmentSchedules;
import br.com.ipet.ordering.domain.model.schedule.ScheduleName;
import br.com.ipet.ordering.domain.model.schedule.category.ServiceCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentScheduleApplicationService {

    private final AppointmentScheduleRegistrationService appointmentScheduleRegistrationService;
    private final AppointmentSchedules appointmentSchedules;

    public UUID create(ScheduleInput input) {
        FieldValidator.requiresNonNull("input", input);

        var staySchedule = appointmentScheduleRegistrationService.register(
                new CompanyId(input.companyId()),
                new ScheduleName(input.name()),
                ServiceCategory.valueOf(input.subCategory())
        );

        appointmentSchedules.add(staySchedule);

        return staySchedule.id().value();
    }

}
