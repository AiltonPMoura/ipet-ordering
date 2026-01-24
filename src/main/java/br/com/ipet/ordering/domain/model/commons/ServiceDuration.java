package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record ServiceDuration(Integer minutes) {

    public ServiceDuration {
        FieldValidator.requiresNonNull("minutes", minutes);

        if (minutes % 15 > 0)
            throw new RuntimeException("Minutos precisa ser múltiplos de 15");
    }

}
