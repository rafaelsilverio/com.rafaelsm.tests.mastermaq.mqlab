package com.rafaelsm.tests.mastermaq.mqlab.controller;

import com.rafaelsm.tests.mastermaq.mqlab.bo.FinancialTransactionBO;
import com.rafaelsm.tests.mastermaq.mqlab.domain.ResponseMessage;
import com.rafaelsm.tests.mastermaq.mqlab.entity.FinancialTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@RestController
@EnableAutoConfiguration
@ComponentScan("com.rafaelsm.tests.mastermaq.mqlab")
@EnableJpaRepositories("com.rafaelsm.tests.mastermaq.mqlab")
@EntityScan("com.rafaelsm.tests.mastermaq.mqlab")
public class FinancialTransactionController {

    @Autowired
    private FinancialTransactionBO financialTransactionBO;

    @RequestMapping(value = "/rest/financial-transaction", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseMessage> createFinancialTransaction(@RequestBody FinancialTransaction financialTransaction) {
        ResponseMessage responseMessage = financialTransactionBO.insert(financialTransaction);
        return new ResponseEntity<ResponseMessage>(responseMessage, responseMessage.getHttpStatus());
    }

    @RequestMapping(value = "/rest/financial-transaction/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<ResponseMessage> editFinancialTransaction(@PathVariable String id,
                                                                    @RequestBody FinancialTransaction financialTransaction) {
        ResponseMessage responseMessage = financialTransactionBO.edit(id, financialTransaction);
        return new ResponseEntity<ResponseMessage>(responseMessage, responseMessage.getHttpStatus());
    }

    @RequestMapping(value = "/rest/financial-transaction/all/{cnpj}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ResponseMessage> findAllFinancialTransaction(@PathVariable Long cnpj) {
        ResponseMessage responseMessage = financialTransactionBO.findAll(cnpj);
        return new ResponseEntity<ResponseMessage>(responseMessage, responseMessage.getHttpStatus());
    }

    @RequestMapping(value = "/rest/financial-transaction/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ResponseMessage> detailedFinancialTransaction(@PathVariable String id) {
        ResponseMessage responseMessage = financialTransactionBO.findDetailed(id);
        return new ResponseEntity<ResponseMessage>(responseMessage, responseMessage.getHttpStatus());
    }

    @RequestMapping(value = "/rest/financial-transaction/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<ResponseMessage> deleteFinancialTransaction(@PathVariable String id) {
        ResponseMessage responseMessage = financialTransactionBO.delete(id);
        return new ResponseEntity<ResponseMessage>(responseMessage, responseMessage.getHttpStatus());
    }

    @RequestMapping(value = "/rest/financial-transaction/report/{cnpj}/{month}/{year}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ResponseMessage> generateFinancialTransactionReport(@PathVariable Long cnpj,
                                                                      @PathVariable Integer month,
                                                                      @PathVariable Integer year) {
        ResponseMessage responseMessage = financialTransactionBO.transactionReport(cnpj, month, year);
        return new ResponseEntity<ResponseMessage>(responseMessage, responseMessage.getHttpStatus());
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FinancialTransactionController.class, args);
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
    }
}
