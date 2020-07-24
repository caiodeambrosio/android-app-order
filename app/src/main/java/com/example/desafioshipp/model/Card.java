package com.example.desafioshipp.model;

import java.io.Serializable;

public class Card implements Serializable {
    private int id;
    private String number;
    private String name;
    private String validity;
    private String cvv;
    private String cpf;

    public Card() {}

    public Card(String number, String name, String validity, String cvv, String cpf) {
        this.number = number;
        this.name = name;
        this.validity = validity;
        this.cvv = cvv;
        this.cpf = cpf;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
