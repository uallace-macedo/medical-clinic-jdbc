package com.uallace.clinic.exception;

public class DatabaseException extends RuntimeException {
  public DatabaseException(String msg) {
    super(msg);
  }

  public DatabaseException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
