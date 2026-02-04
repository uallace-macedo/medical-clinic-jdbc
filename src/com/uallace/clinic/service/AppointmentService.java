package com.uallace.clinic.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.uallace.clinic.exception.BusinessException;
import com.uallace.clinic.exception.EntityException;
import com.uallace.clinic.model.Appointment;
import com.uallace.clinic.repository.AppointmentDAO;
import com.uallace.clinic.util.Page;

public class AppointmentService {
  private final AppointmentDAO appointmentDAO = new AppointmentDAO();
  private final int minDiffSchedule = 15;

  public void save(Appointment appointment) {
    if (appointment.getDoctor() == null) throw new EntityException("O agendamento precisa ter um doutor.");
    if (appointment.getPatient() == null) throw new EntityException("O agendamento precisa ter um paciente.");
    if (appointment.getAppointmentDate() == null) throw new EntityException("O agendamento precisa ter um data e hora.");
    if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())) throw new EntityException("Nao e possivel agendar para uma data retroativa.");

    List<Appointment> doctorAppointments = appointmentDAO.getOpenedDoctorAppointments(appointment.getDoctor().getId());
    for (Appointment doctorAppointment : doctorAppointments) {
      long diff = Duration.between(doctorAppointment.getAppointmentDate(), appointment.getAppointmentDate()).abs().toMinutes();
      if (diff < minDiffSchedule) {
        throw new BusinessException(String.format("O doutor ja tem um agendamento as %s. Deve haver uma diferenca minima de %d minutos.", 
          doctorAppointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
          minDiffSchedule
        ));
      }
    }

    List<Appointment> patientAppointments = appointmentDAO.getOpenedPatientAppointments(appointment.getPatient().getId());
    for (Appointment patientAppointment : patientAppointments) {
      long diff = Duration.between(patientAppointment.getAppointmentDate(), appointment.getAppointmentDate()).abs().toMinutes();
      if (diff < minDiffSchedule) {
        throw new BusinessException(String.format("O paciente ja tem um agendamento as %s. Deve haver uma diferenca minima de %d minutos.", 
          patientAppointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
          minDiffSchedule
        ));
      }
    }

    appointmentDAO.save(appointment);
  }

  public Appointment findById(int id) {
    return appointmentDAO.findById(id)
      .orElseThrow(() -> new BusinessException(String.format("Agendamento com id %d nao encontrado.", id)));
  }

  public Page<Appointment> findAll(int page, int size) {
    List<Appointment> appointments = appointmentDAO.findAll(page, size + 1);
    boolean hasNext = appointments.size() > size;

    if (hasNext) {
      appointments.remove(size);
    }

    return new Page<>(appointments, page, size, hasNext);
  }

  public List<Appointment> findAllOpenedDoctorAppointments(int id) {
    return appointmentDAO.getOpenedDoctorAppointments(id);
  }

  public void update(Appointment appointment) {
    if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())) throw new BusinessException("Nao e possivel agendar para uma data retroativa.");

    List<Appointment> doctorAppointments = appointmentDAO.getOpenedDoctorAppointments(appointment.getDoctor().getId());
    for (Appointment doctorAppointment: doctorAppointments) {
      long diff = Duration.between(doctorAppointment.getAppointmentDate(), appointment.getAppointmentDate()).abs().toMinutes();
      if (diff < minDiffSchedule) {
         throw new BusinessException(String.format("O doutor ja tem um agendamento as %s. Deve haver uma diferenca minima de %d minutos.", 
          doctorAppointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
          minDiffSchedule
        ));
      }
    }

    List<Appointment> patientAppointments = appointmentDAO.getOpenedPatientAppointments(appointment.getPatient().getId());
    for (Appointment patientAppointment : patientAppointments) {
      long diff = Duration.between(patientAppointment.getAppointmentDate(), appointment.getAppointmentDate()).abs().toMinutes();
      if (diff < minDiffSchedule) {
        throw new BusinessException(String.format("O paciente ja tem um agendamento as %s. Deve haver uma diferenca minima de %d minutos.", 
          patientAppointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
          minDiffSchedule
        ));
      }
    }

    appointmentDAO.update(appointment);
  }

  public void delete(int id) {
    appointmentDAO.delete(id);
  }
}
