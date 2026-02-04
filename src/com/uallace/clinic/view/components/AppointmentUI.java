package com.uallace.clinic.view.components;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.uallace.clinic.model.Appointment;
import com.uallace.clinic.model.AppointmentStatus;
import com.uallace.clinic.model.Doctor;
import com.uallace.clinic.model.Patient;
import com.uallace.clinic.service.AppointmentService;
import com.uallace.clinic.service.DoctorService;
import com.uallace.clinic.service.PatientService;
import com.uallace.clinic.util.Page;
import com.uallace.clinic.view.BaseUI;

public class AppointmentUI extends BaseUI {
  private final String TABLE_HEADER_FORMAT = "| %-5s | %-25.25s | %-25.25s | %-18.18s | %-10.10s |%n";
  private final String TABLE_DATA_FORMAT = "| %-5s | %-25.25s | %-25.25s | %-18.18s | %-10.10s |%n";
  private final int TABLE_SIZE = 99;
  private final int MENU_SIZE = 47;

  private final AppointmentService service = new AppointmentService();
  private final DoctorService doctorService = new DoctorService();
  private final PatientService patientService = new PatientService();

  public AppointmentUI(Scanner scan) {
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
          case 1 -> add();
          case 2 -> findById();
          case 3 -> findAll();
          case 4 -> findAllByDoctor();
          case 5 -> update();
          case 6 -> delete();
          case 7 -> running = false;
          default -> System.out.println("Opcao invalida");
        }
      } catch (Exception e) {
        System.out.println("ERRO: " + e.getMessage());
      }
    }
  }

  private void add() {
    System.out.println("=".repeat(MENU_SIZE));
    System.out.println(">>> Adicionar agendamento");

    int doctor_id = readInt("ID do Doutor (enter para cancelar): ");
    if (doctor_id == 0) {
      System.out.println("- Operacao cancelada.");
      return;
    }

    int patient_id = readInt("ID do Paciente: ");
    LocalDateTime appointment_date = readDateTime("Data e Hora");

    Doctor doctor = doctorService.findById(doctor_id);
    Patient patient = patientService.findById(patient_id);

    Appointment appointment = new Appointment();
    appointment.setDoctor(doctor);
    appointment.setPatient(patient);
    appointment.setAppointmentDate(appointment_date);

    showHeader();
    showAppointment(appointment);
    showFooter();

    System.out.println("Voce tem certeza que deseja agendar?");
    String input = readString("[S]im / [N]ao: ").trim().toLowerCase();

    if (input.isBlank() || input.charAt(0) != 's') {
      System.out.println("- Operacao cancelada.");
      return;
    }

    service.save(appointment);
    System.out.println("Agendamento realizado com sucesso.");
  }

  private void findById() {
    System.out.println("=".repeat(MENU_SIZE));
    System.out.println(">>> Buscar agendamento por id");

    int appointment_id = readInt("ID do Agendamento (enter para cancelar): ");
    if (appointment_id == 0) {
      System.out.println("- Operacao cancelada.");
    }

    Appointment appointment = service.findById(appointment_id);
    showHeader();
    showAppointment(appointment);
    showFooter();
  }

  private void findAll() {
    System.out.println("=".repeat(MENU_SIZE));
    System.out.println(">>> Ver todos os agendamentos");

    int page = 1;
    int size = 10;
    boolean browsing = true;

    while (browsing) {
      Page<Appointment> pageData = service.findAll(page, size);
      showAllAppointments(pageData.getContent());

      List<String> options = new ArrayList<>();
      options.add(String.format("Pagina %d", pageData.getPage()));

      if (pageData.hasPrevious()) options.add("[A]nterior");
      if (pageData.hasNext()) options.add("[P]roxima");
      options.add("[S]air");

      System.out.println(String.join(" | ", options));
      String input = readString("Opcao: ").trim().toLowerCase();

      if (input.isBlank()) {
        System.out.println("Opcao invalida.");
        continue;
      }

      switch (input.charAt(0)) {
        case 'a' -> {
          if (pageData.hasPrevious()) page--;
          else System.out.println("Voce ja esta na primeira pagina.");
        }
        case 'p' -> {
          if (pageData.hasNext()) page++;
          else System.out.println("Voce ja esta na ultima pagina.");
        }
        case 's' -> browsing = false;
        default -> System.out.println("Opcao invalida");
      }
    }
  }

  private void findAllByDoctor() {
    System.out.println("=".repeat(MENU_SIZE));
    System.out.println(">>> Ver todos os agendamentos do doutor(a)");

    int doctor_id = readInt("ID do Doutor (enter para cancelar): ");
    if (doctor_id == 0) {
      System.out.println("- Operacao cancelada.");
      return;
    }

    List<Appointment> appointments = service.findAllOpenedDoctorAppointments(doctor_id);
    showAllAppointments(appointments);
  }

  private void update() {
    System.out.println("=".repeat(MENU_SIZE));
    System.out.println(">>> Atualizar agendamento");

    int appointment_id = readInt("ID do Agendamento (enter para cancelar): ");
    if (appointment_id == 0) {
      System.out.println("- Operacao cancelada.");
    }

    Appointment appointment = service.findById(appointment_id);
    showHeader();
    showAppointment(appointment);
    showFooter();
    
    int doctor_id = readInt("ID do Doutor (enter para manter): ");
    int patient_id = readInt("ID do Paciente (enter para manter): ");
    
    LocalDateTime appointment_date = null;
    try {
      appointment_date = readDateTime("Data e Hora (enter para cancelar)");
      if (appointment_date != null && !appointment.getAppointmentDate().isEqual(appointment_date)) {
        appointment.setAppointmentDate(appointment_date);
      }
    } catch (Exception e) {}
    
    String status = readString("Status ([A]gendado, [C]ancelado, [F]inalizado, enter para manter): ").trim().toLowerCase();

    if (doctor_id == 0 && patient_id == 0 && status.isBlank() && appointment_date == null) {
      System.out.println("- Nenhuma alteracao realizada.");
      return;
    }

    if (doctor_id != 0 && doctor_id != appointment.getDoctor().getId()) appointment.setDoctor(doctorService.findById(doctor_id));
    if (patient_id != 0 && patient_id != appointment.getPatient().getId()) appointment.setPatient(patientService.findById(patient_id));

    if (!status.isBlank()) {
      switch (status.charAt(0)) {
        case 'a' -> appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);
        case 'c' -> appointment.setAppointmentStatus(AppointmentStatus.CANCELED);
        case 'f' -> appointment.setAppointmentStatus(AppointmentStatus.FINISHED);
        default -> System.out.println(String.format("Valor de status invalido. '%s' sera mantido.", appointment.getAppointmentStatus().getDescription()));
      }
    }

    showHeader();
    showAppointment(appointment);
    showFooter();

    System.out.println("Tem certeza que deseja atualizar o agendamento?");
    String opt = readString("[S]im / [N]ao: ");

    if (opt.isBlank() || opt.charAt(0) != 's') {
      System.out.println("- Operacao cancelada.");
      return;
    }

    service.update(appointment);
    System.out.println("Agendamento atualizado com sucesso.");
  }

  private void delete() {
    System.out.println("=".repeat(MENU_SIZE));
    System.out.println(">>> Excluir agendamento");

    int id = readInt("ID (enter para cancelar): ");
    if (id == 0) {
      System.out.println("- Operacao cancelada.");
      return;
    }

    Appointment appointment = service.findById(id);

    showHeader();
    showAppointment(appointment);
    showFooter();

    System.out.println("Voce tem certeza que deseja excluir?");
    String input = readString("[S]im | [N]ao: ").trim().toLowerCase();

    if (input.isBlank() || input.charAt(0) != 's') {
      System.out.println("- Operacao cancelada.");
      return;
    }

    service.delete(id);
    System.out.println("Registro de agendamento excluido com sucesso.");
  }

  // --- UTILS

  @Override
  protected void showMenu() {
    System.out.println("\n================ AGENDAMENTOS ================");
    System.out.println("1 - Adicionar agendamento");
    System.out.println("2 - Buscar agendamento por id");
    System.out.println("3 - Ver todos os agendamentos");
    System.out.println("4 - Ver todos os agendamentos do doutor(a)");
    System.out.println("5 - Atualizar agendamento");
    System.out.println("6 - Excluir agendamento");
    System.out.println("7 - Voltar");
    System.out.println("=".repeat(MENU_SIZE));
  }

  private void showHeader() {
    System.out.println();
    System.out.println("-".repeat(TABLE_SIZE));
    System.out.printf(TABLE_HEADER_FORMAT, "ID", "Doutor(a)", "Paciente", "Data Agendamento", "Status");
    System.out.println("-".repeat(TABLE_SIZE));
  }

  private void showAppointment(Appointment appointment) {
    System.out.printf(
      TABLE_DATA_FORMAT,
      appointment.getId() == 0 ? "-" : String.valueOf(appointment.getId()),
      appointment.getDoctor().getName(),
      appointment.getPatient().getName(),
      appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
      appointment.getAppointmentStatus() != null ? appointment.getAppointmentStatus().getDescription() : AppointmentStatus.SCHEDULED.getDescription()
    );
  }

  private void showAllAppointments(List<Appointment> appointments) {
    showHeader();
    if (appointments.size() == 0) {
      System.out.println("Pagina vazia");
    } else {
      for (Appointment appointment: appointments) {
        showAppointment(appointment);
      }
    }
    showFooter();
  }

  private void showFooter() {
    System.out.println("-".repeat(TABLE_SIZE));
  }
}
