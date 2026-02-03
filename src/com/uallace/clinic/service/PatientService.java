package com.uallace.clinic.service;

import java.util.List;

import com.uallace.clinic.exception.EntityException;
import com.uallace.clinic.model.Patient;
import com.uallace.clinic.repository.PatientDAO;
import com.uallace.clinic.util.Page;

public class PatientService {
  private final PatientDAO patientDAO = new PatientDAO();

  public void addPatient(Patient patient) {
    validateName(patient.getName());
    validateCpf(patient.getCpf());

    if (patient.getTelephone().length() > 15 || 5 > patient.getTelephone().length()) throw new EntityException("Telefone invalido.");
    patientDAO.save(patient);
  }

  public Patient findById(int id) {
    return patientDAO.findById(id)
      .orElseThrow(() -> new EntityException(String.format("Paciente com id %d nao encontrado.", id)));
  }

  public Page<Patient> findAll(int page) {
    return findAll(page, patientDAO.getDefaultLimit());
  }

  public Page<Patient> findAll(int page, int size) {
    List<Patient> patients = patientDAO.findAll(page, size + 1);
    boolean hasNext = patients.size() > size;

    if (hasNext) {
      patients.remove(size);
    }

    return new Page<>(patients, page, size, hasNext);
  }

  public void updatePatient(Patient patient) {
    patientDAO.update(patient);
  }

  public void deletePatient(int id) {
    patientDAO.delete(id);
  }

  private void validateName(String name) {
    if (3 > name.length() || name.isBlank()) throw new EntityException("Nome nao pode ser vazio e deve conter no minimo 3 caracteres.");
  }

  private String validateCpf(String cpf) {
    cpf = cpf.replaceAll("\\D", "");
    if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) throw new EntityException("CPF invalido.");
    return cpf;
  }
}
