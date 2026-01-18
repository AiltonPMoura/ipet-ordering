package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.exception.message.MessageCode;

public class StockCannotBeNegativeException extends DomainException {

    public StockCannotBeNegativeException() {
        super(MessageCode.ERROR_STOCK_CANNOT_BE_NEGATIVE);
    }

}
