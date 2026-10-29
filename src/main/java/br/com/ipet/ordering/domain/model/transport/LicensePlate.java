package br.com.ipet.ordering.domain.model.transport;

import br.com.ipet.ordering.domain.model.FieldValidator;

import java.util.Locale;

public record LicensePlate(String value) {

    private static final String BRAZILIAN_LICENSE_PLATE_REGEX = "^[A-Z]{3}-?(?:\\d[A-Z\\d]\\d{2}|\\d{4})$";

    public LicensePlate {
        FieldValidator.requiresNonBlank("licensePlate", value);
        value = value.toUpperCase(Locale.ROOT);

        if (!value.matches(BRAZILIAN_LICENSE_PLATE_REGEX))
            throw new LicensePlateIsNotValidException(value);

        value = value.replace("-", "");
    }

}
