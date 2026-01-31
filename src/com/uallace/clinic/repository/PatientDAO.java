package com.uallace.clinic.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uallace.clinic.exception.DatabaseException;
import com.uallace.clinic.exception.EntityException;
import com.uallace.clinic.model.Patient;

public class PatientDAO {
  private int queryLimit = 25;

  public void save(Patient patient) {
    String sql = "INSERT INTO patients (name,cpf,telephone) VALUES (?,?,?)";

    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setString(1, patient.getName());
      stmt.setString(2, patient.getCpf());
      stmt.setString(3, patient.getTelephone());

      stmt.executeUpdate();
    } catch (SQLException e) {
      if (e.getErrorCode() == 1062) {
        throw new EntityException("Esse CPF ja esta em uso.");
      }

      throw new DatabaseException("Nao foi possivel salvar o paciente.", e);
    }
  }

  public Optional<Patient> findById(int id) {
    String sql = "SELECT * FROM patients WHERE id = ?";
    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setInt(1, id);
      try (ResultSet result = stmt.executeQuery()) {
        if (result.next()) {
          Patient patient = new Patient(
            id,
            result.getString("name"),
            result.getString("cpf"),
            result.getString("telephone")
          );

          return Optional.of(patient);
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Nao foi possivel encontrar o paciente.", e);
    }

    return Optional.empty();
  }

  public void update(Patient patient) {
    String sql = "UPDATE patients SET name = ?, cpf = ?, telephone = ? WHERE id = ?";

    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setString(1, patient.getName());
      stmt.setString(2, patient.getCpf());
      stmt.setString(3, patient.getTelephone());
      stmt.setInt(4, patient.getId());

      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected == 0) {
        throw new DatabaseException("Paciente nao encontrado.");
      }

    } catch (SQLException e) {
      if (e.getErrorCode() == 1062) {
        throw new EntityException("Esse CPF ja esta em uso.");
      }

      throw new DatabaseException("Nao foi possivel atualizar os dados do paciente.", e);
    }
  }

  public void delete (int id) {
    String sql = "DELETE FROM patients WHERE id = ?";
     try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setInt(1, id);

      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected == 0) {
        throw new DatabaseException("Paciente nao encontrado.");
      }
    } catch (SQLException e) {
      throw new DatabaseException("Nao foi possivel excluir o paciente.", e);
    }
  }

  public List<Patient> findAll() {
    return findAll(0, queryLimit);
  }

  public List<Patient> findAll(int page, int size) {
    List<Patient> patients = new ArrayList<>();
    
    int currentPage = Math.max(page, 1) - 1;
    int offset = currentPage * size;

    String sql = "SELECT * FROM patients ORDER BY id LIMIT ? OFFSET ?";
    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setInt(1, size);
      stmt.setInt(2, offset);
      
      try(ResultSet result = stmt.executeQuery()) {
        while (result.next()) {
          Patient patient = new Patient(
            result.getInt("id"),
            result.getString("name"),
            result.getString("cpf"),
            result.getString("telephone")
          );

          patients.add(patient);
        }
      }

    } catch (SQLException e) {
      throw new DatabaseException("Nao foi possivel buscar os pacientes.", e);
    }

    return patients;
  }
}
