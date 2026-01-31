package com.uallace.clinic.repository;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO<T> implements DAO<T> {
  protected static int queryLimit = 25;

  protected Connection getConnection() throws SQLException {
    return ConnectionFactory.getConnection();
  }
}
