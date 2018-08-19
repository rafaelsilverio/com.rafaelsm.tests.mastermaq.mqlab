package com.rafaelsm.tests.mastermaq.mqlab.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rafaelsm.tests.mastermaq.mqlab.entity.FinancialTransaction;
import org.springframework.http.HttpStatus;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"httpStatus", "code"})
public class ResponseMessage {
    public static final String REQUEST_SUCCESS = "OK.";
    public static  final String SAVE_SUCCESS = "FinancialTransaction created.";
    public static  final String INVALID_FIELDS = "Invalid FinancialTransaction provided: fields (%s) are invalid.";
    public static  final String INVALID_ID = "Invalid id provided - it must be a valid UUID.";
    public static  final String INVALID_DATES = "Invalid dates provided.";
    public static  final String NOT_FOUND = "FinancialTransaction not found with the specified id";
    public static  final String DELETE_SUCCESS = "FinancialTransaction deleted.";

    private HttpStatus httpStatus;
    private String message;

    @JsonProperty("financialTransaction")
    private FinancialTransaction financialTransaction;

    @JsonProperty("financialTransactionList")
    private List<FinancialTransaction> financialTransactionList;

    public ResponseMessage() {

    }

    public ResponseMessage(HttpStatus httpStatus, String message) {
        setHttpStatus(httpStatus);
        setMessage(message);
    }

    public String getCode() {
        return httpStatus.toString();
    }

    public void setCode(String code) {
        httpStatus = HttpStatus.valueOf(new Integer(code));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FinancialTransaction getFinancialTransaction() {
        return financialTransaction;
    }

    public void setFinancialTransaction(FinancialTransaction financialTransaction) {
        this.financialTransaction = financialTransaction;
    }

    public List<FinancialTransaction> getFinancialTransactionList() {
        return financialTransactionList;
    }

    public void setFinancialTransactionList(List<FinancialTransaction> financialTransactionList) {
        this.financialTransactionList = financialTransactionList;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
