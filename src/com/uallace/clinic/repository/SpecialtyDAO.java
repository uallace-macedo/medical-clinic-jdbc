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

public class SpecialtyDAO {
  private static int queryLimit = 25;

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

  public Optional<Specialty> findByName(String name) {
    String sql = "SELECT * FROM specialties WHERE name = ?";

    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setString(1, name);
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
    String sql = "SELECT * FROM specialties LIMIT ?";
    List<Specialty> specialties = new ArrayList<>();

    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setInt(1, queryLimit);
      ResultSet result = stmt.executeQuery();

      while (result.next()) {
        int id = result.getInt("id");
        String name = result.getString("name");
        specialties.add(new Specialty(id, name));
      }

      return specialties;
    } catch (SQLException e) {
      throw new DatabaseException("Erro ao buscar especialidades", e);
    }
  }

  public Specialty update(int id, String newName) {
    Specialty specialty = findById(id)
      .orElseThrow(() -> new DatabaseException("Especialidade nao encontrada."));

    if (specialty.getName().equals(newName)) {
      return specialty;
    }

    findByName(newName).ifPresent(s -> {
      throw new EntityException("Esse nome ja esta em uso!");
    });

    String sql = "UPDATE specialties SET name = ? WHERE id = ?";
    specialty.setName(newName);

    try (
      Connection conn = ConnectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
    ) {
      stmt.setString(1, specialty.getName());
      stmt.setInt(2, specialty.getId());
      stmt.executeUpdate();

      return specialty;
    } catch (SQLException e) {
      throw new DatabaseException("Nao foi possivel atualizar a especialidade.", e);
    }
  }
}