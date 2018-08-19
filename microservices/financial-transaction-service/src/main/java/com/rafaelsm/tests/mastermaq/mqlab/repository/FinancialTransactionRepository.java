package com.rafaelsm.tests.mastermaq.mqlab.repository;

import com.rafaelsm.tests.mastermaq.mqlab.entity.FinancialTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface FinancialTransactionRepository extends CrudRepository<FinancialTransaction, Long>  {
    FinancialTransaction findById(UUID id);
    List<FinancialTransaction> findByCnpj(Long cnpj);
    List<FinancialTransaction> findByCnpjAndDateBetween(Long cnpj, Date initial, Date ending);
}
