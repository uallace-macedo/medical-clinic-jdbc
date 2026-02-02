package com.uallace.clinic.view.components;

import java.util.Scanner;

import com.uallace.clinic.view.BaseUI;

public class SpecialtyUI extends BaseUI {
  public SpecialtyUI(Scanner scan) {
    super(scan);
  }

  @Override
  public void start() {
    boolean running = true;

    while (running) {
      showMenu();

      try {
        int opt = readInt("Opcao: ");
        switch (opt) {
          case 1 -> addSpecialty();
          case 2 -> findSpecialtyById();
          case 3 -> getAllSpecialties();
          case 4 -> updateSpecialty();
          case 5 -> running = false;
          default -> System.out.println("Opcao invalida");
        }
      } catch (Exception e) {
        System.out.println("ERRO: " + e.getMessage());
      }
    }
  }

  private void addSpecialty() {
    System.out.println("1 - Adicionar especialidade");
  }

  private void findSpecialtyById() {
    System.out.println("2 - Buscar especialidade por id");
  }
  
  private void getAllSpecialties() {
    System.out.println("3 - Ver especialidades");
  }
  
  private void updateSpecialty() {
    System.out.println("4 - Atualizar especialidade");
  }

  @Override
  protected void showMenu() {
    System.out.println("\n========== ESPECIALIDADES ==========");
    System.out.println("1 - Adicionar especialidade");
    System.out.println("2 - Buscar especialidade por id");
    System.out.println("3 - Ver especialidades");
    System.out.println("4 - Atualizar especialidade");
    System.out.println("5 - Voltar");
    System.out.println("=".repeat(36));
  }
}
