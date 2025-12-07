package br.com.ipet.ordering.domain.model.entity;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.valueobject.CompanyId;
import br.com.ipet.ordering.domain.model.valueobject.Money;
import br.com.ipet.ordering.domain.model.valueobject.ServiceDescription;
import br.com.ipet.ordering.domain.model.valueobject.ServiceId;
import br.com.ipet.ordering.domain.model.valueobject.ServiceName;
import lombok.Builder;

public class Service {
    private ServiceId id;
    private CompanyId companyId;
    private ServiceName name;
    private ServiceDescription description;
    private Money price;

    @Builder(builderClassName = "CreateNewServiceBuilder", builderMethodName = "createNew")
    private static Service create(CompanyId companyId, ServiceName name,
                                  ServiceDescription description, Money price) {
        return new Service(new ServiceId(), companyId, name, description, price);
    }

    @Builder(builderClassName = "CreateExistingServiceBuilder", builderMethodName = "existing")
    private Service(ServiceId id, CompanyId companyId,
                    ServiceName name, ServiceDescription description, Money price) {
        this.setId(id);
        this.setCompanyId(companyId);
        this.setName(name);
        this.setDescription(description);
        this.setPrice(price);
    }

    void changeName(ServiceName name) {
        this.setName(name);
    }

    void changeDescription(ServiceDescription description) {
        FieldValidator.requiresNonNull("service description", description);
    }

    void changePrice(Money price) {
        this.setPrice(price);
    }

    public ServiceId id() {
        return id;
    }

    public CompanyId companyId() {
        return companyId;
    }

    public ServiceName name() {
        return name;
    }

    public ServiceDescription description() {
        return description;
    }

    public Money price() {
        return price;
    }

    private void setId(ServiceId id) {
        FieldValidator.requiresNonNull("serviceId", id);
        this.id = id;
    }

    private void setCompanyId(CompanyId companyId) {
        FieldValidator.requiresNonNull("companyId", companyId);
        this.companyId = companyId;
    }

    private void setName(ServiceName name) {
        FieldValidator.requiresNonNull("service name", name);
        this.name = name;
    }

    private void setDescription(ServiceDescription description) {
        FieldValidator.requiresNonNull("service description", description);
        this.description = description;
    }

    private void setPrice(Money price) {
        FieldValidator.requiresNonNull("service price", price);
        this.price = price;
    }
}
