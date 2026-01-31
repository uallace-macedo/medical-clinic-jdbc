package com.uallace.clinic.repository;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
  public void save(T entity);
  public Optional<T> findById(int id);
  public List<T> findAll(int page, int size);
  public void update(T entity);
  public void delete(int id);
}
