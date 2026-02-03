package com.uallace.clinic.view.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.uallace.clinic.model.Patient;
import com.uallace.clinic.service.PatientService;
import com.uallace.clinic.util.Page;
import com.uallace.clinic.view.BaseUI;

public class PatientUI extends BaseUI {
  private final String TABLE_HEADER_FORMAT = "| %-5s | %-25.25s | %-12.12s | %-17.17s |%n";
  private final String TABLE_DATA_FORMAT = "| %-5d | %-25.25s | %-12.12s | %-17.17s |%n";
  private final int TABLE_SIZE = 72;

  private final PatientService service = new PatientService();

  public PatientUI(Scanner scan) {
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
          case 1 -> addPatient();
          case 2 -> getPatientById();
          case 3 -> getAllPatients();
          case 4 -> updatePatient();
          case 5 -> deletePatient();
          case 6 -> running = false;
          default -> System.out.println("Opcao invalida");
        }
      } catch (Exception e) {
        System.out.println("ERRO: " + e.getMessage());
      }
    }
  }

  private void addPatient() {
    System.out.println("=".repeat(36));
    System.out.println(">>> Adicionar paciente");

    String name = readString("Nome (enter para cancelar): ");
    if (name.isBlank()) {
      System.out.println("- Operacao cancelada.");
      return;
    }

    String cpf = readString("CPF (apenas numeros; 11 digitos): ");
    String telephone = readString("Telefone [(xx) xxxxx-xxxx]: ");

    Patient patient = new Patient();
    patient.setName(name);
    patient.setCpf(cpf);
    patient.setTelephone(telephone);

    service.addPatient(patient);
    System.out.println("Paciente adicionado com sucesso.");
  }

  private void getPatientById() {
    System.out.println("=".repeat(36));
    System.out.println(">>> Buscar paciente por id");

    int id = readInt("ID: ");
    Patient patient = service.findById(id);

    showHeader();
    showPatient(patient);
    showFooter();
  }

  private void getAllPatients() {
    System.out.println("=".repeat(36));
    System.out.println(">>> Ver pacientes");

    int page = 1;
    int size = 10;
    boolean browsing = true;

    while (browsing) {
      Page<Patient> pageData = service.findAll(page, size);
      showAllPatients(pageData.getContent());
      List<String> options = new ArrayList<>();

      options.add(String.format("Pagina %d", pageData.getPage()));
      if (pageData.hasPrevious()) options.add("[A]nterior");
      if (pageData.hasNext()) options.add("[P]roxima");
      options.add("[S]air");

      System.out.println(String.join(" | ", options));
      String input = readString("Opcao: ");

      if (input.isBlank()) {
        System.out.println("Opcao invalida");
        continue;
      }

      char opt = input.charAt(0);
      switch (opt) {
        case 'a' -> {
          if (pageData.hasPrevious()) page--;
          else System.out.println("Voce ja esta na primeira pagina.");
        }
        case 'p' -> {
          if (pageData.hasNext()) page++;
          else System.out.println("Voce ja esta na ultima pagina.");
        }
        case 's' -> browsing = false;
        default -> System.out.println("Opcao invalida.");
      }
    }
  }

  private void updatePatient() {
    System.out.println("=".repeat(36));
    System.out.println(">>> Atualizar paciente");

    int id = readInt("ID (enter para cancelar): ");
    if (id == 0) {
      System.out.println("- Operacao cancelada.");
      return;
    }

    Patient patient = service.findById(id);

    showHeader();
    showPatient(patient);
    showFooter();

    String newName = readString("Novo nome (enter para manter): ");
    String newCPF = readString("Novo CPF (enter para manter): ");
    String newTelephone = readString("Novo telefone ([(xx) xxxxx-xxxx] / enter para manter): ");

    patient.setName(newName.isBlank() ? patient.getName() : newName);
    patient.setCpf(newCPF.isBlank() ? patient.getCpf() : newCPF);
    patient.setTelephone(newTelephone.isBlank() ? patient.getTelephone() : newTelephone);

    service.updatePatient(patient);
    System.out.println("Paciente atualizado com sucesso.");
  }

  private void deletePatient() {
    System.out.println("=".repeat(36));
    System.out.println(">>> Excluir paciente");

    int id = readInt("ID (enter para cancelar): ");
    if (id == 0) {
      System.out.println("- Operacao cancelada.");
      return;
    }

    Patient patient = service.findById(id);

    showHeader();
    showPatient(patient);
    showFooter();

    System.out.println("Voce tem certeza que deseja excluir?");
    String input = readString("[S]im | [N]ao: ").trim().toLowerCase();

    if (input.isBlank() || input.charAt(0) != 's') {
      System.out.println("- Operacao cancelada.");
      return;
    }

    service.deletePatient(id);
    System.out.println("Registro de paciente excluido com sucesso.");
  }

  @Override
  protected void showMenu() {
    System.out.println("\n============= PACIENTES =============");
    System.out.println("1 - Adicionar paciente");
    System.out.println("2 - Buscar paciente por id");
    System.out.println("3 - Ver pacientes");
    System.out.println("4 - Atualizar paciente");
    System.out.println("5 - Excluir paciente");
    System.out.println("6 - Voltar");
    System.out.println("=".repeat(36));
  }

  protected void showHeader() {
    System.out.println();
    System.out.println("-".repeat(TABLE_SIZE));
    System.out.printf(TABLE_HEADER_FORMAT, "ID", "Nome", "CPF", "Telefone");
    System.out.println("-".repeat(TABLE_SIZE));
  }

  protected void showFooter() {
    System.out.println("-".repeat(TABLE_SIZE));
  }

  protected void showPatient(Patient patient) {
    System.out.printf(TABLE_DATA_FORMAT, patient.getId(), patient.getName(), patient.getCpf(), patient.getTelephone());
  }

  protected void showAllPatients(List<Patient> patients) {
    showHeader();
    if (patients.size() == 0) {
      System.out.println("Pagina vazia.");
    } else {
      for (Patient patient: patients) {
        showPatient(patient);
      }
    }
    showFooter();
  }
}
