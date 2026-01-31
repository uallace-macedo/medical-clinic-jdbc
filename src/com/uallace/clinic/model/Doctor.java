package com.uallace.clinic.model;

public class Doctor {
  private int id;
  private String name;
  private String crm;
  private Specialty specialty;

  public Doctor() {}
  public Doctor(int id, String name, String crm, Specialty specialty) {
    this.id = id;
    this.name = name;
    this.crm = crm;
    this.specialty = specialty;
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

  public String getCrm() {
    return crm;
  }

  public void setCrm(String crm) {
    this.crm = crm;
  }

  public Specialty getSpecialty() {
    return specialty;
  }

  public void setSpecialty(Specialty specialty) {
    this.specialty = specialty;
  }

  @Override
  public String toString() {
    return String.format("%d - %s [%s]: %s", id, name, crm, specialty.getName());
  }
}
