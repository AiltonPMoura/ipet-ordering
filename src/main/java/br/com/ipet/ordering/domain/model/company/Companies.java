package br.com.ipet.ordering.domain.model.company;

import br.com.ipet.ordering.domain.model.Repository;
import br.com.ipet.ordering.domain.model.commons.document.Document;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;

public interface Companies extends Repository<Company, CompanyId> {
    boolean isEmailUnique(Email email, CompanyId excepedCompanyId);
    boolean isDocumentUnique(Document cnpj, CompanyId excepedCompanyId);
}
