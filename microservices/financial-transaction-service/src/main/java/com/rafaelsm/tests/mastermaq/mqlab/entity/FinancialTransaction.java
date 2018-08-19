package com.rafaelsm.tests.mastermaq.mqlab.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "FinancialTransaction", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FinancialTransaction {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "CNPJ")
    @JsonProperty("cnpj")
    private Long cnpj;

    @Column(name = "DESCRIPTION")
    @JsonProperty("description")
    private String description;

    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    @JsonProperty("date")
    private Date date;

    @Column(name = "VALUE")
    @JsonProperty("total_in_cents")
    private Integer value;

    public FinancialTransaction() {

    }

    public FinancialTransaction(Long cnpj, Date dueDate, Integer value, String description) {
        setCnpj(cnpj);
        setDate(dueDate);
        setValue(value);
        setDescription(description);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getCnpj() {
        return cnpj;
    }

    public void setCnpj(Long cnpj) {
        this.cnpj = cnpj;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date dueDate) {
        this.date = dueDate;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
