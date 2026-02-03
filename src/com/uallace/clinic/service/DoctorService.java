package com.uallace.clinic.service;

import java.util.List;

import com.uallace.clinic.exception.BusinessException;
import com.uallace.clinic.exception.EntityException;
import com.uallace.clinic.model.Doctor;
import com.uallace.clinic.repository.DoctorDAO;
import com.uallace.clinic.util.Page;

public class DoctorService {
  private final DoctorDAO doctorDAO = new DoctorDAO();

  public void add(Doctor doctor) {
    if (doctor.getName().isBlank() || doctor.getName().length() < 3) {
      throw new BusinessException("O nome nao pode ser vazio e deve conter no minimo 3 caracteres.");
    }

    if (doctor.getCrm().isBlank() || doctor.getCrm().length() != 11) {
      throw new BusinessException("O crm nao pode ser vazio e deve conter 11 caracteres.");
    }

    doctorDAO.save(doctor);
  }

  public Doctor findById(int id) {
    return doctorDAO.findById(id)
      .orElseThrow(() -> new EntityException(String.format("Doutor(a) com id %d nao encontrado(a).", id)));
  }

  public Page<Doctor> getDoctors() {
    return getDoctors(0, doctorDAO.getDefaultLimit());
  }

  public Page<Doctor> getDoctors(int page, int size) {
    List<Doctor> doctors = doctorDAO.findAll(page, size + 1);
    boolean hasNext = doctors.size() > size;

    if (hasNext) {
      doctors.remove(size);
    }

    return new Page<>(doctors, page, size, hasNext);
  }

  public void updateDoctor(Doctor doctor) {
    doctorDAO.update(doctor);
  }
  
  public void delete(int id) {
    doctorDAO.delete(id);
  }
}
