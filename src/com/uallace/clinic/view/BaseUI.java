package com.uallace.clinic.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.uallace.clinic.exception.BusinessException;
import com.uallace.clinic.exception.InputException;

public abstract class BaseUI {
  protected Scanner scan;

  public BaseUI(Scanner scan) {
    this.scan = scan;
  }

  public abstract void start();
  protected abstract void showMenu();

  protected int readInt(String msg) {
    System.out.print(msg);
    String input = scan.nextLine().trim();

    if (input.isBlank()) {
      return 0;
    }
    
    try {
      return Integer.parseInt(input);
    } catch (NumberFormatException e) {
      throw new InputException("Por favor, informe um valor valido.");
    }
  }

  protected String readString(String msg) {
    System.out.print(msg);
    return scan.nextLine().trim();
  }

  protected LocalDateTime readDateTime(String msg) {
    String input = readString(msg + " (dd/MM/yyyy HH:mm): ");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    try {
      return LocalDateTime.parse(input, formatter);
    } catch (Exception e) {
      throw new BusinessException("Data e hora invalidos.");
    }
  }
}
