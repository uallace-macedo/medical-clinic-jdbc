package com.uallace.clinic.model;

import java.time.LocalDateTime;

public class Appointment {
  int id;
  Doctor doctor_id;
  Patient patient_id;
  LocalDateTime appointment_date;

  public Appointment() {}
  public Appointment(int id, Doctor doctor_id, Patient patient_id, LocalDateTime appointment_date) {
    this.id = id;
    this.doctor_id = doctor_id;
    this.patient_id = patient_id;
    this.appointment_date = appointment_date;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Doctor getDoctor() {
    return doctor_id;
  }

  public void setDoctor(Doctor doctor_id) {
    this.doctor_id = doctor_id;
  }

  public Patient getPatient() {
    return patient_id;
  }

  public void setPatient(Patient patient_id) {
    this.patient_id = patient_id;
  }

  public LocalDateTime getAppointmentDate() {
    return appointment_date;
  }

  public void setAppointmentDate(LocalDateTime appointment_date) {
    this.appointment_date = appointment_date;
  }
}
