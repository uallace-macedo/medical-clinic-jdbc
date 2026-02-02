package com.uallace.clinic.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uallace.clinic.exception.DatabaseException;
import com.uallace.clinic.exception.EntityException;
import com.uallace.clinic.model.Appointment;
import com.uallace.clinic.model.Doctor;
import com.uallace.clinic.model.Patient;
import com.uallace.clinic.model.Specialty;

public class AppointmentDAO extends BaseDAO<Appointment> {

  @Override
  public void save(Appointment appointment) {
    String sql = "INSERT INTO appointments (doctor_id,patient_id,appointment_date) VALUES (?,?,?)";
    try (
      Connection conn = getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setInt(1, appointment.getDoctor().getId());
      stmt.setInt(2, appointment.getPatient().getId());
      stmt.setObject(3, appointment.getAppointmentDate());
      stmt.executeUpdate();
    } catch (SQLException e) {
      if (e.getErrorCode() == 1062) {
        String msg = e.getMessage().toLowerCase();

        if (msg.contains("uk_doctor_schedule")) {
          throw new EntityException("O doutor ja possui um agendamento para esse horario.");
        } else {
          throw new EntityException("O paciente ja possui um agendamento para esse horario.");
        }
      }
      throw new DatabaseException("Nao foi possivel concluir o agendamento.", e);
    }
  }

  @Override
  public Optional<Appointment> findById(int id) {
    String sql = """
      SELECT 
        a.id AS appointment_id,
        a.appointment_date,
        s.name AS specialty_name,
        d.id AS doctor_id,
        d.name AS doctor_name,
        p.id AS patient_id,
        p.name AS patient_name
      FROM appointments a
      INNER JOIN doctors d ON d.id=a.doctor_id
      INNER JOIN specialties s ON s.id=d.specialty_id
      INNER JOIN patients p ON p.id=a.patient_id
      WHERE a.id = ?
    """;

    try (
      Connection conn = getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setInt(1, id);
      try (ResultSet result = stmt.executeQuery()) {
        if (result.next()) {
          return Optional.of(mapResultSetToAppointment(result));
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Nao foi possivel buscar o agendamento.", e);
    }

    return Optional.empty();
  }

  public List<Appointment> findAll() {
    return findAll(0, queryLimit);
  }

  @Override
  public List<Appointment> findAll(int page, int size) {
    String sql = """
      SELECT 
	      a.id AS appointment_id,
	      a.appointment_date,
	      s.name AS specialty_name,
	      d.id AS doctor_id,
	      d.name AS doctor_name,
	      p.id AS patient_id,
	      p.name AS patient_name
      FROM appointments a
      INNER JOIN doctors d ON d.id=a.doctor_id
      INNER JOIN specialties s ON s.id=d.specialty_id
      INNER JOIN patients p ON p.id=a.patient_id
      ORDER BY a.appointment_date
      LIMIT ? OFFSET ?
    """;

    List<Appointment> appointments = new ArrayList<>();
    int currentPage = Math.max(page, 1) - 1;
    int offset = currentPage * size;

    try (
      Connection conn = getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setInt(1, size);
      stmt.setInt(2, offset);

      try(ResultSet result = stmt.executeQuery()) {
        while (result.next()) {
          appointments.add(mapResultSetToAppointment(result));
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Nao foi possivel buscar os agendamentos.", e);
    }

    return appointments;
  }

  @Override
  public void update(Appointment entity) {
    String sql = "UPDATE appointments SET doctor_id = ?, patient_id = ?, appointment_date = ? WHERE id = ?";

    try (
      Connection conn = getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setInt(1, entity.getDoctor().getId());
      stmt.setInt(2, entity.getPatient().getId());
      stmt.setObject(3, entity.getAppointmentDate());
      stmt.setInt(4, entity.getId());

      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected == 0) {
        throw new DatabaseException("Agendamento nao encontrado.");
      }
    } catch (SQLException e) {
      if (e.getErrorCode() == 1062) {
        String msg = e.getMessage().toLowerCase();

        if (msg.contains("uk_doctor_schedule")) {
          throw new EntityException("O doutor ja possui um agendamento para esse horario.");
        } else {
          throw new EntityException("O paciente ja possui um agendamento para esse horario.");
        }
      }
      throw new DatabaseException("Nao foi possivel atualizar o agendamento.", e);
    }
  }

  @Override
  public void delete(int id) {
    String sql = "DELETE FROM appointments WHERE id = ?";

    try (
      Connection conn = getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setInt(1, id);

      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected == 0) {
        throw new DatabaseException("Agendamento nao encontrado.");
      }
    } catch (SQLException e) {
      throw new DatabaseException("Nao foi possivel excluir esse agendamento.", e);
    }
  }

  // --- UTILS

  private Appointment mapResultSetToAppointment(ResultSet result) throws SQLException {
    Specialty specialty = new Specialty();
    specialty.setName(result.getString("specialty_name"));

    Doctor doctor = new Doctor();
    doctor.setId(result.getInt("doctor_id"));
    doctor.setName(result.getString("doctor_name"));
    doctor.setSpecialty(specialty);

    Patient patient = new Patient();
    patient.setId(result.getInt("patient_id"));
    patient.setName(result.getString("patient_name"));

    return new Appointment(
      result.getInt("appointment_id"),
      doctor,
      patient,
      result.getObject("appointment_date", LocalDateTime.class)
    );
  }
}
