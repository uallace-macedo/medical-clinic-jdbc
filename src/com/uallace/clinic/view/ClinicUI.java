package com.uallace.clinic.view;

import java.util.Scanner;

import com.uallace.clinic.view.components.DoctorUI;
import com.uallace.clinic.view.components.PatientUI;
import com.uallace.clinic.view.components.SpecialtyUI;

public class ClinicUI extends BaseUI {
  public ClinicUI() {
    super(new Scanner(System.in));
  }

  @Override
  public void start() {
    SpecialtyUI specialtyUI = new SpecialtyUI(scan);
    DoctorUI doctorUI = new DoctorUI(scan);
    PatientUI patientUI = new PatientUI(scan);
    
    boolean running = true;
    while(running) {
      System.out.println();
      showMenu();

      try {
        int option = readInt("Opcao: ");

        switch (option) {
          case 1 -> specialtyUI.start();
          case 2 -> doctorUI.start();
          case 3 -> patientUI.start();
          // case 4 -> appointmentUI.start();
          case 5 -> running = false;
          default -> System.out.println("Opcao invalida");
        }
      } catch (Exception e) {
        System.out.println("ERRO: " + e.getMessage());
      }
    }

    System.out.println("Sistema encerrado.");
  }

  @Override
  protected void showMenu() {
    System.out.println("========== SISTEMA CLINICA ==========");
    System.out.println("1 - Especializacoes");
    System.out.println("2 - Doutores");
    System.out.println("3 - Pacientes");
    System.out.println("4 - Agendamentos");
    System.out.println("5 - Sair");
    System.out.println("=".repeat(37));
  }
}
