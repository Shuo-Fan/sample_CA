package com.sample.sample_ca.entity;

import javax.persistence.*;

@Entity
@Table
public class CurrencyChineseNameData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column
    String currencyCode;

    @Column
    String codeName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    @Override
    public String toString() {
        return " id : " + id + ", currencyCode : " + currencyCode + ", codeName : " + codeName;
    }

}
