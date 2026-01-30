package com.uallace.clinic.model;

import java.time.LocalDateTime;

public class Appointment {
  int id;
  Doctor doctor;
  Patient patient;
  LocalDateTime appointment_date;

  public Appointment() {}
  public Appointment(int id, Doctor doctor, Patient patient, LocalDateTime appointment_date) {
    this.id = id;
    this.doctor = doctor;
    this.patient = patient;
    this.appointment_date = appointment_date;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Doctor getDoctor() {
    return doctor;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public LocalDateTime getAppointmentDate() {
    return appointment_date;
  }

  public void setAppointmentDate(LocalDateTime appointment_date) {
    this.appointment_date = appointment_date;
  }
}
