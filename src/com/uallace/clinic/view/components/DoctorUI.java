package com.uallace.clinic.view.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.uallace.clinic.model.Doctor;
import com.uallace.clinic.model.Specialty;
import com.uallace.clinic.service.DoctorService;
import com.uallace.clinic.util.Page;
import com.uallace.clinic.view.BaseUI;

public class DoctorUI extends BaseUI {
  private final String TABLE_HEADER_FORMAT = "| %5s | %-25.25s | %-12.12s | %-21.21s |%n";
  private final String TABLE_DATA_FORMAT = "| %5d | %-25.25s | %-12.12s | %-21.21s |%n";
  private final int TABLE_SIZE = 76;

  private final DoctorService service = new DoctorService();

  public DoctorUI(Scanner scan) {
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
          case 1 -> addDoctor();
          case 2 -> getDoctorById();
          case 3 -> getAllDoctors();
          case 4 -> updateDoctor();
          case 5 -> deleteDoctor();
          case 6 -> running = false;
          default -> System.out.println("Opcao invalida");
        }
      } catch (Exception e) {
        System.out.println("ERRO: " + e.getMessage());
      }
    }
  }

  private void addDoctor() {
    System.out.println("=".repeat(36));
    System.out.println(">>> Adicionar Doutor");

    String name = readString("Nome (enter para cancelar): ");
    if (name.isBlank()) {
      System.out.println("- Operacao cancelada.");
      return;
    }

    String crm = readString("CRM: ");
    int specialty_id = readInt("ID da Especialidade: ");

    Specialty specialty = new Specialty();
    specialty.setId(specialty_id);

    Doctor doctor = new Doctor();
    doctor.setName(name);
    doctor.setCrm(crm);
    doctor.setSpecialty(specialty);

    service.add(doctor);
    System.out.println("Doutor com sucesso!");
  }

  private void getDoctorById() {
    System.out.println("=".repeat(36));
    System.out.println(">>> Buscar Doutor por ID");

    int id = readInt("ID (enter para cancelar): ");
    if (id == 0) {
      System.out.println("- Operacao cancelada.");
      return;
    }

    Doctor doctor = service.findById(id);
    showHeader();
    showDoctor(doctor);
    showFooter();
  }

  private void getAllDoctors() {
    System.out.println("=".repeat(36));
    System.out.println(">>> Buscar todos os doutores");

    int page = 1;
    int size = 10;
    boolean browsing = true;

    while (browsing) {
      Page<Doctor> pageData = service.getDoctors(page, size);
      if (pageData.getContent().size() == 0) {
        System.out.println("- Nenhum doutor encontrado.");
        browsing = false;
        return;
      }

      showAllDoctors(pageData.getContent());

      List<String> options = new ArrayList<>();
      options.add(String.format("Page %d", pageData.getPage()));
      
      if (pageData.hasPrevious()) {
        options.add("[A]nterior");
      }
      
      if (pageData.hasNext()) {
        options.add("[P]roxima");
      }

      options.add("[S]air");
      System.out.println(String.join(" | ", options));

      String input = readString("Opcao: ").trim().toLowerCase();
      if (input.isBlank()) {
        System.out.println("Opcao invalida.");
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

  private void updateDoctor() {
    System.out.println("=".repeat(36));
    System.out.println(">>> Atualizar doutor");

    int id = readInt("ID (enter para cancelar): ");
    if (id == 0) {
      System.out.println("- Operacao cancelada.");
      return;
    }

    Doctor doctor = service.findById(id);

    showHeader();
    showDoctor(doctor);
    showFooter();

    String newName = readString("Novo nome (enter para manter): ");
    String newCrm = readString("Novo CRM (enter para manter): ");
    int newSpecialty_id = readInt("Digite o ID da nova especialidade (enter para manter): ");

    Specialty specialty = new Specialty();
    specialty.setId(newSpecialty_id);

    doctor.setName(newName.isBlank() ? doctor.getName() : newName);
    doctor.setCrm(newCrm.isBlank() ? doctor.getCrm() : newCrm);
    doctor.setSpecialty(newSpecialty_id == 0 ? doctor.getSpecialty() : specialty);

    service.updateDoctor(doctor);
    System.out.println("Doutor atualizado com sucesso.");
  }

  private void deleteDoctor() {
    System.out.println("=".repeat(36));
    System.out.println(">>> Excluir doutor");

    int id = readInt("ID (enter para cancelar): ");
    if (id == 0) {
      System.out.println("- Operacao cancelada.");
      return;
    }

    Doctor doctor = service.findById(id);

    showHeader();
    showDoctor(doctor);
    showFooter();

    System.out.println("Voce tem certeza que deseja excluir?");
    String input = readString("[S]im | [N]ao: ").trim().toLowerCase();

    if (input.isBlank() || input.charAt(0) != 's') {
      System.out.println("- Operacao cancelada.");
      return;
    }

    service.delete(id);
    System.out.println("Registro de doutor excluido com sucesso.");
  }

  // --- UTILS

  @Override
  protected void showMenu() {
    System.out.println("\n============= DOUTORES =============");
    System.out.println("1 - Adicionar doutor");
    System.out.println("2 - Buscar doutor por id");
    System.out.println("3 - Ver doutores");
    System.out.println("4 - Atualizar doutor");
    System.out.println("5 - Excluir doutor");
    System.out.println("6 - Voltar");
    System.out.println("=".repeat(36));
  }

  private void showHeader() {
    System.out.println();
    System.out.println("-".repeat(TABLE_SIZE));
    System.out.printf(TABLE_HEADER_FORMAT, "ID", "Nome", "CRM", "Especialidade");
    System.out.println("-".repeat(TABLE_SIZE));
  }

  private void showFooter() {
    System.out.println("-".repeat(TABLE_SIZE));
  }

  private void showDoctor(Doctor doctor) {
    System.out.printf(TABLE_DATA_FORMAT, doctor.getId(), doctor.getName(), doctor.getCrm(), doctor.getSpecialty().getName());
  }

  private void showAllDoctors(List<Doctor> doctors) {
    showHeader();
    for (Doctor doctor: doctors) {
      showDoctor(doctor);
    }
    showFooter();
  }
}
