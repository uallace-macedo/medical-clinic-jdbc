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
import com.uallace.clinic.model.Doctor;
import com.uallace.clinic.model.Specialty;

public class DoctorDAO extends BaseDAO<Doctor> {
  @Override
  public void save(Doctor doctor) {
    String sql = "INSERT INTO doctors (name,crm,specialty_id) VALUES (?,?,?)";

    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setString(1, doctor.getName());
      stmt.setString(2, doctor.getCrm());
      stmt.setInt(3, doctor.getSpecialty().getId());
      stmt.executeUpdate();
    } catch (SQLException e) {
      if (e.getErrorCode() == 1062) {
        throw new DatabaseException("Esse CRM ja esta cadastrado.");
      }

      if (e.getErrorCode() == 1452) {
        throw new DatabaseException("Especialidade nao encontrada.");
      }

      throw new DatabaseException("Ocorreu um erro ao salvar o doutor.", e);
    }
  }

  @Override
  public Optional<Doctor> findById(int id) {
    String sql = """
      SELECT d.*, s.name AS specialty_name
      FROM doctors d
      INNER JOIN specialties s
      ON d.specialty_id=s.id
      WHERE d.id = ?
    """;

    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setInt(1, id);
      try (ResultSet result = stmt.executeQuery();) {
        if (result.next()) {
          Specialty specialty = new Specialty(
            result.getInt("specialty_id"),
            result.getString("specialty_name")
          );
  
          Doctor doctor = new Doctor(
            id,
            result.getString("name"),
            result.getString("crm"),
            specialty
          );
  
          return Optional.of(doctor);
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Ocorreu um erro ao buscar o doutor.", e);
    }

    return Optional.empty();
  }

  @Override
  public void update(Doctor doctor) {
    String sql = "UPDATE doctors SET name = ?, crm = ?, specialty_id = ? WHERE id = ?";
    
    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql)
    ) {    
      stmt.setString(1, doctor.getName());
      stmt.setString(2, doctor.getCrm());
      stmt.setInt(3, doctor.getSpecialty().getId());
      stmt.setInt(4, doctor.getId());

      int rowsAffected = stmt.executeUpdate();

      if (rowsAffected == 0) {
        throw new EntityException("Doutor nao encontrado.");
      }
    } catch (SQLException e) {
      if (e.getErrorCode() == 1062) {
        throw new EntityException("Esse CRM já está cadastrado.");
      }
      throw new DatabaseException("Erro ao atualizar o doutor.", e);
    }
  }

  @Override
  public void delete(int id) {
    String sql = "DELETE FROM doctors WHERE id = ?";
    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ){
      stmt.setInt(1, id);
      int rowsAffected = stmt.executeUpdate();

      if (rowsAffected == 0) {
        throw new EntityException("Doutor nao encontrado.");
      }
    } catch (SQLException e) {
      throw new DatabaseException("Ocorreu um erro ao excluir o doutor.", e);
    }
  }

  public List<Doctor> findAll() {
    return findAll(0, queryLimit);
  }

  @Override
  public List<Doctor> findAll(int page, int size) {
    List<Doctor> doctors = new ArrayList<>();

    int currentPage = Math.max(page, 1) - 1;
    int offset = (currentPage) * (size - 1);

    String sql = """
      SELECT d.*, s.name AS specialty_name
      FROM doctors d
      INNER JOIN specialties s
      ON d.specialty_id=s.id
      ORDER BY d.id
      LIMIT ? OFFSET ?
    """;

    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql)
    ) {
      stmt.setInt(1, size);
      stmt.setInt(2, offset);

      try (ResultSet result = stmt.executeQuery()) {
        while(result.next()) {
          Specialty specialty = new Specialty(result.getInt("specialty_id"), result.getString("specialty_name"));
          Doctor doctor = new Doctor(result.getInt("id"), result.getString("name"), result.getString("crm"), specialty);
          doctors.add(doctor);
        }

        return doctors;
      }
    } catch (SQLException e) {
      throw new DatabaseException("Nao foi possivel buscar os doutores.", e);
    }
  }
}
