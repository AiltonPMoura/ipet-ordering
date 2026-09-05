package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.commons.exception.FieldCannotBeEmptyException;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.category.ServiceCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AppointmentScheduleTest {

    private CompanyId companyId;
    private ScheduleName name;

    @BeforeEach
    void setUp() {
        this.companyId = new CompanyId();
        this.name = new ScheduleName("Minha agenda");
    }

    @Test
    void givenSupportedServiceCategory_whenCreate_thenReturnDaytimeSchedule() {
        var schedule = AppointmentSchedule.create(companyId, name, ServiceCategory.HIGIENE);

        assertThat(schedule).isNotNull();
        assertThat(schedule.id()).isNotNull();
        assertThat(schedule.companyId()).isEqualTo(companyId);
        assertThat(schedule.name()).isEqualTo(name);
        assertThat(schedule.serviceCategory()).isEqualTo(ServiceCategory.HIGIENE);
        assertThat(schedule.status()).isEqualTo(ScheduleStatus.DRAFT);
        assertThat(schedule.workingDays()).isEmpty();
        assertThat(schedule.createdAt()).isNotNull();
    }

    @Test
    void givenUnsupportedServiceCategory_whenCreate_thenThrow() {
        assertThatThrownBy(() -> AppointmentSchedule.create(companyId, name, ServiceCategory.DAYCARE))
                .isInstanceOf(ScheduleDontSupportSubcategoryException.class);
    }

    @Test
    void givenNullName_whenCreate_thenThrowFieldCannotBeEmptyException() {
        assertThatThrownBy(() -> AppointmentSchedule.create(companyId, null, ServiceCategory.HIGIENE))
                .isInstanceOf(FieldCannotBeEmptyException.class);
    }

    @Test
    void givenNullCompanyId_whenCreate_thenReturnFieldCannotBeEmptyException() {
        assertThatThrownBy(() -> AppointmentSchedule.create(null, name, ServiceCategory.HIGIENE))
                .isInstanceOf(FieldCannotBeEmptyException.class);
    }

    @Test
    void givenNullServiceCategory_whenCreate_thenThrowFieldCannotBeEmptyException() {
        assertThatThrownBy(() -> AppointmentSchedule.create(companyId, name, null))
                .isInstanceOf(FieldCannotBeEmptyException.class);
    }

}



