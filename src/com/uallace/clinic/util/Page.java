package com.uallace.clinic.util;

import java.util.List;

public class Page<T> {
  private List<T> content;
  private int page;
  private int size;
  private boolean hasNext;

  public Page(List<T> content, int page, int size, boolean hasNext) {
    this.content = content;
    this.page = page;
    this.size = size;
    this.hasNext = hasNext;
  }

  public List<T> getContent() {
    return content;
  }

  public int getPage() {
    return page;
  }

  public int getSize() {
    return size;
  }

  public boolean hasNext() {
    return hasNext;
  }

  public boolean isEmpty() {
    return content.isEmpty();
  }
}