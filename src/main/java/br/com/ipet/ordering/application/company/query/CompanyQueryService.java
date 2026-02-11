package br.com.ipet.ordering.application.company.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CompanyQueryService {
    CompanyDetailOutput findById(UUID companyId);
    Page<CompanySummaryOutput> filter(CompanyFilter filter, Pageable pageable);
}
