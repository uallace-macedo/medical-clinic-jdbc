package com.uallace.clinic.view.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.uallace.clinic.model.Specialty;
import com.uallace.clinic.service.SpecialtyService;
import com.uallace.clinic.util.Page;
import com.uallace.clinic.view.BaseUI;

public class SpecialtyUI extends BaseUI {
  private final String TABLE_HEADER_FORMAT = "| %5s | %-21s |%n";
  private final String TABLE_DATA_FORMAT = "| %5s | %-21s |%n";
  private final SpecialtyService service = new SpecialtyService();

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
          case 5 -> deleteSpecialty();
          case 6 -> running = false;
          default -> System.out.println("Opcao invalida");
        }
      } catch (Exception e) {
        System.out.println("ERRO: " + e.getMessage());
      }
    }
  }

  private void addSpecialty() {
    System.out.println("=".repeat(36));
    System.out.println(">>> Adicionar especialidade");
    String name = readString("Nome (enter para cancelar): ");

    if (name.isBlank()) {
      System.out.println("- Operacao cancelada.");
      return;
    }

    service.add(name);
    System.out.println("Especialidade adicionada com sucesso!");
  }

  private void findSpecialtyById() {
    System.out.println("=".repeat(36));
    System.out.println(">>> Buscar especialidade por ID");
    int id = readInt("Id: ");

    Specialty specialty = service.findById(id);
    showHeader();
    showSpecialty(specialty);
    showFooter();
  }
  
  private void getAllSpecialties() {
    System.out.println("=".repeat(36));
    System.out.println(">>> Ver especialidades");

    int currentPage = 1;
    int size = 10;
    boolean browsing = true;

    while (browsing) {
      System.out.println();
      Page<Specialty> pageData = service.getSpecialties(currentPage, size);
      showAllSpecialties(pageData.getContent());

      List<String> options = new ArrayList<>();;
      options.add(String.format("Pagina %d", pageData.getPage()));

      if (pageData.hasNext()) {
        options.add("[P]roxima");
      }

      if (pageData.hasPrevious()) {
        options.add("[A]nterior");
      }

      options.add("[S]air");

      System.out.println(String.join(" | ", options));
      String input = readString("Opcao: ").toLowerCase().trim();

      if (input.isBlank()) {
        continue;
      }

      char opt = input.charAt(0);

      switch (opt) {
        case 'p' -> {
          if (pageData.hasNext()) currentPage++;
          else System.out.println("Voce ja esta na ultima pagina.");
        }
        case 'a' -> {
          if (pageData.hasPrevious()) currentPage--;
          else System.out.println("Voce ja esta na primeira pagina.");
        }
        case 's' -> browsing = false;
        default -> System.out.println("Opcao invalida.");
      }
    }
  }
  
  private void updateSpecialty() {
    System.out.println("=".repeat(36));
    System.out.println(">>> Atualizar especialidade");

    int id = readInt("Id (enter para cancelar): ");
    if (id == 0) {
      System.out.println("- Operacao cancelada.");
      return;
    }

    String name = readString("Novo nome (enter para cancelar): ");
    if (name.isBlank()) {
      System.out.println("- Operacao cancelada.");
      return;
    }

    service.updateName(id, name);
    System.out.println("Especialidade atualizada com sucesso.");
  }

  private void deleteSpecialty() {
    System.out.println("=".repeat(36));
    System.out.println(">>> Excluir especialidade");

    int id = readInt("Id (enter para cancelar): ");
    if (id == 0) {
      System.out.println("- Operacao cancelada.");
      return;
    }

    service.delete(id);
    System.out.println("Especialidade excluida com sucesso.");
  }

  // --- UTILS

  @Override
  protected void showMenu() {
    System.out.println("\n========== ESPECIALIDADES ==========");
    System.out.println("1 - Adicionar especialidade");
    System.out.println("2 - Buscar especialidade por id");
    System.out.println("3 - Ver especialidades");
    System.out.println("4 - Atualizar especialidade");
    System.out.println("5 - Excluir especialidade");
    System.out.println("6 - Voltar");
    System.out.println("=".repeat(36));
  }

  private void showHeader() {
    System.out.println("-".repeat(32));
    System.out.printf(TABLE_HEADER_FORMAT, "Id", "Nome");
    System.out.println("-".repeat(32));
  }

  private void showSpecialty(Specialty specialty) {
    System.out.printf(TABLE_DATA_FORMAT, specialty.getId(), specialty.getName());
  }

  private void showAllSpecialties(List<Specialty> specialties) {
    showHeader();
    for (Specialty specialty : specialties) {
      showSpecialty(specialty);
    }
    showFooter();
  }

  private void showFooter() {
    System.out.println("-".repeat(32));
  }
}
