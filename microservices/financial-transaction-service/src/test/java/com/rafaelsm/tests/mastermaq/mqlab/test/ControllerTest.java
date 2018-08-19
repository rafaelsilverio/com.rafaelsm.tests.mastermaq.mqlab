package com.rafaelsm.tests.mastermaq.mqlab.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelsm.tests.mastermaq.mqlab.controller.FinancialTransactionController;
import com.rafaelsm.tests.mastermaq.mqlab.domain.ResponseMessage;
import com.rafaelsm.tests.mastermaq.mqlab.entity.FinancialTransaction;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinancialTransactionController.class)
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void saveFinancialTransactionValidationMessageEmptyEntity() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FinancialTransaction financialTransaction = new FinancialTransaction();
        String jsonParam = mapper.writeValueAsString(financialTransaction);
        this.mockMvc.perform(post("/rest/financial-transaction").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(String.format(ResponseMessage.INVALID_FIELDS, "cnpj,date,total_in_cents,description")));
    }

    @Test
    public void deleteFinancialTransactionSuccessfully() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Long cnpj = 66640364000144L;
        FinancialTransaction financialTransaction = new FinancialTransaction(66640364000144L, new Date(), 1000, "IR");
        String jsonParam = mapper.writeValueAsString(financialTransaction);

        //Persist FinancialTransaction
        this.mockMvc.perform(post("/rest/financial-transaction").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("message").value(ResponseMessage.SAVE_SUCCESS));

        //Get the list of persisted FinancialTransactions
        ResultActions resultActions = this.mockMvc.perform(get("/rest/financial-transaction/all/" + cnpj))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value(ResponseMessage.REQUEST_SUCCESS));
        ResponseMessage responseMessage = mapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), ResponseMessage.class);

        //Delete the first one
        String firsFinancialTransactionId = responseMessage.getFinancialTransactionList().get(0).getId().toString();
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/rest/financial-transaction/" + firsFinancialTransactionId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value(ResponseMessage.DELETE_SUCCESS));

        //Try to request it by id
        this.mockMvc.perform(get("/rest/financial-transaction/" + firsFinancialTransactionId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(ResponseMessage.NOT_FOUND));
    }

    @Test
    public void createFinancialTransactionReportSuccessfully() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Long cnpj = 47166596000168L;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<FinancialTransaction> januaryList = new ArrayList<>();
        List<FinancialTransaction> mayList = new ArrayList<>();
        januaryList.add(new FinancialTransaction(cnpj, formatter.parse("2018-01-15"), 1000, "IR"));
        januaryList.add(new FinancialTransaction(cnpj, formatter.parse("2018-01-20"), 1000, "MEDIC"));

        mayList.add(new FinancialTransaction(cnpj, formatter.parse("2018-05-15"), 1000, "IR"));
        mayList.add(new FinancialTransaction(cnpj, formatter.parse("2018-05-20"), 1000, "MEDIC"));
        mayList.add(new FinancialTransaction(cnpj, formatter.parse("2018-05-21"), 1000, "ASSETS"));

        //Persists Lists
        String jsonParam;
        for (FinancialTransaction transaction : januaryList) {
            jsonParam = mapper.writeValueAsString(transaction);
            this.mockMvc.perform(post("/rest/financial-transaction").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("message").value(ResponseMessage.SAVE_SUCCESS));
        }
        for (FinancialTransaction transaction : mayList) {
            jsonParam = mapper.writeValueAsString(transaction);
            this.mockMvc.perform(post("/rest/financial-transaction").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("message").value(ResponseMessage.SAVE_SUCCESS));
        }

        //Get january report
        ResultActions resultActionsJanuary = this.mockMvc.perform(get("/rest/financial-transaction/report/" + cnpj + "/1/2018"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value(ResponseMessage.REQUEST_SUCCESS));
        ResponseMessage responseMessageJanuary = mapper.readValue(resultActionsJanuary.andReturn().getResponse().getContentAsString(), ResponseMessage.class);

        //Get may report
        ResultActions resultActionsMay = this.mockMvc.perform(get("/rest/financial-transaction/report/" + cnpj + "/5/2018"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value(ResponseMessage.REQUEST_SUCCESS));
        ResponseMessage responseMessageMay = mapper.readValue(resultActionsMay.andReturn().getResponse().getContentAsString(), ResponseMessage.class);

        //Check the report
        Assert.assertEquals("Report size for January", 2, responseMessageJanuary.getFinancialTransactionList().size());
        Assert.assertEquals("Report size for May", 3, responseMessageMay.getFinancialTransactionList().size());
    }
}
