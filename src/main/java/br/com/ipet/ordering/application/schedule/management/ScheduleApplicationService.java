package br.com.ipet.ordering.application.schedule.management;

import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.company.CompanyNotFoundException;
import br.com.ipet.ordering.domain.model.customer.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.ScheduleName;
import br.com.ipet.ordering.domain.model.schedule.ScheduleService;
import br.com.ipet.ordering.domain.model.schedule.Schedules;
import br.com.ipet.ordering.domain.model.schedule.ServiceSubCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleApplicationService {

    private final ScheduleService scheduleService;
    private final Schedules schedules;
    private final Companies companies;


    public UUID create(ScheduleInput input) {
        if (!companies.exists(new CompanyId(input.getCompanyId())))
            throw new CompanyNotFoundException();

        var schedule = scheduleService.generate(
                new CompanyId(input.getCompanyId()),
                new ScheduleName(input.getName()),
                ServiceSubCategory.valueOf(input.getService())
        );

        schedules.add(schedule);

        return schedule.id().value();

    }

}
