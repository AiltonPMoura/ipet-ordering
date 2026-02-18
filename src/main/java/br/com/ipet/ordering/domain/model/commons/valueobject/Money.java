package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.exception.NumberCannotBeNegativeException;
import br.com.ipet.ordering.domain.model.commons.exception.QuantityGreaterThanZeroException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money (BigDecimal value){

    public static final Money ZERO = new Money(BigDecimal.ZERO);
    public static final RoundingMode roundingMode = RoundingMode.HALF_EVEN;

    public Money(String value) {
        this(new BigDecimal(value));
    }

    public Money(BigDecimal value) {
        FieldValidator.requiresNonNull("money value", value);
        this.value = value.setScale(2, roundingMode);

        if (value.signum() == -1)
            throw new NumberCannotBeNegativeException("money value");
    }

    public Money multiply(Quantity quantity) {
        if (quantity.value() < 1)
            throw new QuantityGreaterThanZeroException();

        return new Money(value.multiply(new BigDecimal(quantity.value())));
    }

}
