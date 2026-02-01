package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.FieldValidator;
import lombok.Builder;

import java.time.OffsetTime;

@Builder
public record Service(
        ServiceName name,
        ServiceDescription description,
        Money price,
        OffsetTime startTime,
        OffsetTime endTime) {

    public Service {
        FieldValidator.requiresNonNull("service name", name);
        FieldValidator.requiresNonNull("service description", description);
        FieldValidator.requiresNonNull("service price", price);
        FieldValidator.requiresNonNull("service startTime", startTime);
        FieldValidator.requiresNonNull("service endTime", endTime);
    }

}
