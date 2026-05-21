package br.com.ipet.ordering.domain.model.schedule.category;

import br.com.ipet.ordering.domain.model.company.CompanyId;

import java.util.Optional;

public interface SubcategoryService {
    Optional<ServiceSubcategory> ofCompanyId(CompanyId companyId);
}
