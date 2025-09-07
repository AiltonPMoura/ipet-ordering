package br.com.ipet.ordering.domain.exception;

import br.com.ipet.ordering.domain.exception.message.MessageCode;
import lombok.Getter;
@Getter
public class EmailValidatorException extends DomainException {

    private final String email;

    public EmailValidatorException(String email) {
        super(MessageCode.ERROR_EMAIL_NOT_VALID);
        this.email = email;
    }

}
