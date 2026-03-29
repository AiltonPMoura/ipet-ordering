package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.exception.NumberCannotBeNegativeException;
import br.com.ipet.ordering.domain.model.commons.exception.QuantityNeedsGreaterThanZeroException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money (BigDecimal value) implements Comparable<Money> {

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
        FieldValidator.requiresNonNull("quantity", quantity);

        if (quantity.value() < 1)
            throw new QuantityNeedsGreaterThanZeroException();

        return new Money(value.multiply(new BigDecimal(quantity.value())));
    }

    public Money add(Money money) {
        FieldValidator.requiresNonNull("money", money);
        return new Money(value.add(money.value));
    }

    public Money divide(Money money) {
        FieldValidator.requiresNonNull("money", money);
        return new Money(value.divide(money.value, roundingMode));
    }

    @Override
    public int compareTo(Money other) {
        return value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
