package br.com.ipet.ordering.application.customer.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CustomerQueryService {
    CustomerDetailOutput findById(UUID customerId);
    Page<CustomerSummaryOutput> filter(CustomerFilter filter, Pageable pageable);
}
