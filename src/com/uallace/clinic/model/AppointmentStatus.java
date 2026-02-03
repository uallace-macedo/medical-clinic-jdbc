package com.uallace.clinic.model;

public enum AppointmentStatus {
  SCHEDULED("Agendado"),
  CANCELED("Cancelado"),
  FINISHED("Finalizado");

  private final String description;
  AppointmentStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
