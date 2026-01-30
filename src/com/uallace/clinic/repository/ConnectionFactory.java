package com.uallace.clinic.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.uallace.clinic.exception.DatabaseException;

public abstract class ConnectionFactory {
  private static Properties props = new Properties();
  private static Connection connection;

  static {
    try (FileInputStream fs = new FileInputStream("resources/database.properties")) {
      props.load(fs);
    } catch (IOException e) {
      throw new RuntimeException("Falha ao ler o arquivo 'resources/database.properties'");
    }
  }

  public static Connection getConnection() {
    try {
      if (connection != null && !connection.isClosed()) {
        return connection;
      }

      String url = props.getProperty("db.url");
      String user = props.getProperty("db.user");
      String password = props.getProperty("db.password");
    
    
      connection = DriverManager.getConnection(url, user, password);
      return connection;
    } catch (SQLException e) {
      throw new DatabaseException("Nao foi possivel conectar ao banco! >> " + e.getMessage());
    }
  }
}