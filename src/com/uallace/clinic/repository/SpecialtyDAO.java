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
import com.uallace.clinic.model.Specialty;

public class SpecialtyDAO extends BaseDAO<Specialty> {
  @Override
  public void save(Specialty specialty) { 
    String sql = "INSERT INTO specialties (name) VALUES (?)";
    
    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setString(1, specialty.getName().trim().toUpperCase());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DatabaseException("Nao foi possivel salvar especialidade.", e);
    }
  }

  @Override
  public Optional<Specialty> findById(int id) {
    String sql = "SELECT * FROM specialties WHERE id = ?";

    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setInt(1, id);
      try (ResultSet result = stmt.executeQuery()) {
        if (result.next()) {
          int specialty_id = result.getInt("id");
          String specialty_name = result.getString("name");

          return Optional.of(new Specialty(specialty_id, specialty_name));
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Nao foi possivel buscar especialidade.", e);
    }

    return Optional.empty();
  }
  
  public List<Specialty> findAll() {
    return findAll(0, queryLimit);
  }

  @Override
  public List<Specialty> findAll(int page, int size) {
    String sql = "SELECT * FROM specialties ORDER BY id LIMIT ? OFFSET ?";
    List<Specialty> specialties = new ArrayList<>();

    int currentPage = Math.max(page, 1);
    int offset = (currentPage - 1) * size;

    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setInt(1, size);
      stmt.setInt(2, offset);

      try(ResultSet result = stmt.executeQuery();) {
        while (result.next()) {
          int id = result.getInt("id");
          String name = result.getString("name");
          specialties.add(new Specialty(id, name));
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Erro ao buscar especialidades", e);
    }

    return specialties;
  }

  @Override
  public void update(Specialty specialty) {
    String sql = "UPDATE specialties SET name = ? WHERE id = ?";

    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setString(1, specialty.getName().trim().toUpperCase());
      stmt.setInt(2, specialty.getId());

      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected == 0) {
        throw new DatabaseException("Especialidade nao encontrada.");
      }
    } catch (SQLException e) {
      if (e.getErrorCode() == 1069) {
        throw new EntityException("Esse nome ja esta sendo utilizado!");
      }

      throw new DatabaseException("Nao foi possivel atualizar a especialidade.", e);
    }
  }

  @Override
  public void delete(int id) {
    String sql = "DELETE FROM specialties WHERE id = ?";

    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setInt(1, id);

      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected == 0) {
        throw new DatabaseException("Especialidade nao encontrada.");
      }
    } catch (SQLException e) {
      if (e.getErrorCode() == 1451) {
        throw new EntityException("Nao eh possivel excluir: existem medicos vinculados a essa especialidade.");
      }

      throw new DatabaseException("Nao foi possivel excluir a especialidade.", e);
    }
  }
}