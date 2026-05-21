package br.com.ipet.ordering.application.schedule.management.daytime;

import br.com.ipet.ordering.application.schedule.management.ScheduleInput;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.DayTimeScheduleRegistrationService;
import br.com.ipet.ordering.domain.model.schedule.DayTimeSchedules;
import br.com.ipet.ordering.domain.model.schedule.ScheduleName;
import br.com.ipet.ordering.domain.model.schedule.SubCategoryServiceNotFoundException;
import br.com.ipet.ordering.domain.model.schedule.category.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DayTimeScheduleApplicationService {

    private final DayTimeScheduleRegistrationService dayTimeScheduleRegistrationService;
    private final DayTimeSchedules dayTimeSchedules;
    private final SubcategoryService subcategoryService;

    public UUID create(ScheduleInput input) {
        FieldValidator.requiresNonNull("input", input);

        var serviceSubcategory = subcategoryService.ofCompanyId(new CompanyId(input.companyId()))
                .orElseThrow(() -> new SubCategoryServiceNotFoundException(input.companyId()));

        var dayTimeSchedule = dayTimeScheduleRegistrationService.register(
                new CompanyId(input.companyId()),
                new ScheduleName(input.name()),
                serviceSubcategory
        );

        dayTimeSchedules.add(dayTimeSchedule);

        return dayTimeSchedule.id().value();
    }

}
