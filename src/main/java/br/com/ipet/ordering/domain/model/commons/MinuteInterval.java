package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record MinuteInterval(Integer value) {
    private static final int MINIMUM_MINUTE_INTERVAL = 10;
    private static final int MAXIMUM_MINUTE_INTERVAL = 30;

    public MinuteInterval {
        FieldValidator.requiresNonNull("minuteInterval", value);

        if (value < MINIMUM_MINUTE_INTERVAL)
            throw new RuntimeException();

        if (value > MAXIMUM_MINUTE_INTERVAL)
            throw new RuntimeException();
    }
}
