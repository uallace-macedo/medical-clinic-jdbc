package com.uallace.clinic.model;

public class Patient {
  int id;
  String name;
  String cpf;
  String telephone;

  public Patient() {}
  public Patient(int id, String name, String cpf, String telephone) {
    this.id = id;
    this.name = name;
    this.cpf = cpf;
    this.telephone = telephone;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }
}
