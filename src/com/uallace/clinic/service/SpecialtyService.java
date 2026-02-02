package com.uallace.clinic.service;

import java.util.List;

import com.uallace.clinic.exception.BusinessException;
import com.uallace.clinic.model.Specialty;
import com.uallace.clinic.repository.SpecialtyDAO;
import com.uallace.clinic.util.Page;

public class SpecialtyService {
  private final SpecialtyDAO specialtyDAO = new SpecialtyDAO();
  
  public void add(String name) {
    verifyName(name);
    specialtyDAO.save(new Specialty(0, name));
  }

  public Specialty findById(int id) {
    return specialtyDAO.findById(id)
      .orElseThrow(() -> new BusinessException(String.format("Especialidade com id %d nao encontrada.", id)));
  }

  public Page<Specialty> getSpecialties(int page) {
    return getSpecialties(page, specialtyDAO.getDefaultLimit());
  }

  public Page<Specialty> getSpecialties(int page, int size) {
    List<Specialty> specialties = specialtyDAO.findAll(page, size + 1);
    boolean hasNext = specialties.size() > size;

    if (hasNext) {
      specialties.remove(size);
    }

    return new Page<>(specialties, page, size, hasNext);
  }

  public void updateName(int id, String newName) {
    verifyName(newName);
    specialtyDAO.update(new Specialty(id, newName));
  }

  public void delete(int id) {
    specialtyDAO.delete(id);
  }

  private void verifyName(String name) {
    if (name == null || name.isBlank()) {
      throw new BusinessException("O nome nao pode estar vazio.");
    }

    if (name.length() < 3) {
      throw new BusinessException("Nome muito curto! Minimo de 3 caracteres.");
    }
  }
}
