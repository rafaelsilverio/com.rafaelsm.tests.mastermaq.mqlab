package com.rafaelsm.tests.mastermaq.mqlab.bo;

import com.rafaelsm.tests.mastermaq.mqlab.domain.ResponseMessage;
import com.rafaelsm.tests.mastermaq.mqlab.entity.FinancialTransaction;

public interface FinancialTransactionBO {
    ResponseMessage insert(FinancialTransaction financialTransaction);
    ResponseMessage edit(String id, FinancialTransaction financialTransaction);
    ResponseMessage delete(String id);
    ResponseMessage findAll(Long cnpj);
    ResponseMessage findDetailed(String id);
    ResponseMessage transactionReport(Long cnpj, Integer month, Integer year);
}
