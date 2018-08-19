package com.rafaelsm.tests.mastermaq.mqlab.bo;

import com.rafaelsm.tests.mastermaq.mqlab.domain.ResponseMessage;
import com.rafaelsm.tests.mastermaq.mqlab.entity.FinancialTransaction;
import com.rafaelsm.tests.mastermaq.mqlab.repository.FinancialTransactionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Component
public class FinancialTransactionBOImpl implements FinancialTransactionBO {

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Override
    public ResponseMessage insert(FinancialTransaction financialTransaction) {
        ResponseMessage responseMessage = validateFields(financialTransaction);
        if (responseMessage == null) {
            financialTransactionRepository.save(financialTransaction);
            responseMessage = new ResponseMessage(HttpStatus.CREATED, ResponseMessage.SAVE_SUCCESS);
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage edit(String id, FinancialTransaction financialTransaction) {
        ResponseMessage responseMessage = fillFinancialTransaction(id);
        if (responseMessage.getFinancialTransaction() != null) {
            ResponseMessage responseMessageValidation = validateFields(financialTransaction);
            if (responseMessageValidation != null) {
                return responseMessageValidation;
            }
            financialTransaction.setId(UUID.fromString(id));
            financialTransactionRepository.save(financialTransaction);
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage delete(String id) {
        ResponseMessage responseMessage = fillFinancialTransaction(id);
        if (responseMessage.getFinancialTransaction() != null) {
            responseMessage.setMessage(ResponseMessage.DELETE_SUCCESS);
            financialTransactionRepository.delete(responseMessage.getFinancialTransaction());
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage findAll(Long cnpj) {
        ResponseMessage responseMessage = new ResponseMessage(HttpStatus.OK, ResponseMessage.REQUEST_SUCCESS);
        List<FinancialTransaction> list = new ArrayList<>();
        Iterable<FinancialTransaction> result = financialTransactionRepository.findByCnpj(cnpj);
        result.iterator().forEachRemaining(list::add);
        responseMessage.setFinancialTransactionList(list);
        return responseMessage;
    }

    @Override
    public ResponseMessage findDetailed(String id) {
        ResponseMessage responseMessage = fillFinancialTransaction(id);
        if (responseMessage.getFinancialTransaction() != null) {
            responseMessage.setFinancialTransaction(responseMessage.getFinancialTransaction());
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage transactionReport(Long cnpj, Integer month, Integer year) {
        try {
            LocalDate.of(year, month, 1);
        } catch (DateTimeException e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_DATES);
        }

        Calendar calendarInitial = Calendar.getInstance();
        calendarInitial.set(Calendar.MONTH, month - 1);
        calendarInitial.set(Calendar.YEAR, year);
        calendarInitial.set(Calendar.DAY_OF_MONTH, 1);

        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.set(Calendar.MONTH, month - 1);
        calendarFinal.set(Calendar.YEAR, year);
        calendarFinal.set(Calendar.DAY_OF_MONTH, 1);
        calendarFinal.set(Calendar.DATE, calendarFinal.getActualMaximum(Calendar.DATE));

        ResponseMessage responseMessage = new ResponseMessage(HttpStatus.OK, ResponseMessage.REQUEST_SUCCESS);
        responseMessage.setFinancialTransactionList(financialTransactionRepository.findByCnpjAndDateBetween(cnpj, calendarInitial.getTime(), calendarFinal.getTime()));
        return responseMessage;
    }

    /**
     * Method that check if entity fields are valid, it should be used before saving a Financial Transaction.
     * If invalid, a filled ResponseMessage will be returned, null otherwise.
     */
    private ResponseMessage validateFields (FinancialTransaction financialTransaction) {
        ResponseMessage responseMessage = null;
        List<String> invalidFields = new ArrayList<>();
        if (financialTransaction.getCnpj() == null) {
            invalidFields.add("cnpj");
        }
        if (financialTransaction.getDate() == null) {
            invalidFields.add("date");
        }
        if (financialTransaction.getValue() == null || financialTransaction.getValue() < 0) {
            invalidFields.add("total_in_cents");
        }
        if (StringUtils.isEmpty(financialTransaction.getDescription())) {
            invalidFields.add("description");
        }
        if (!invalidFields.isEmpty()) {
            responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST, String.format(ResponseMessage.INVALID_FIELDS, StringUtils.join(invalidFields, ",")));
        }
        return responseMessage;
    }

    /**
     * Method that check if id is a valid UUID and, if valid, corresponds to a persisted
     * FinancialTransaction. If id is invalid or if is not on the database, a ResponseMessage
     * will be returned filled with the correct message and code. If the FinancialTransaction exists
     * it will be set on the financialTransaction attribute of the returned ResponseMessage.
     */
    private ResponseMessage fillFinancialTransaction (String id) {
        UUID uuid;
        ResponseMessage responseMessage = new ResponseMessage(HttpStatus.OK, ResponseMessage.REQUEST_SUCCESS);
        try{
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException exception){
            responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_ID);
            return responseMessage;
        }
        FinancialTransaction financialTransaction = financialTransactionRepository.findById(uuid);
        if (financialTransaction == null) {
            responseMessage = new ResponseMessage(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
        } else {
            responseMessage.setFinancialTransaction(financialTransaction);
        }
        return responseMessage;
    }
}
