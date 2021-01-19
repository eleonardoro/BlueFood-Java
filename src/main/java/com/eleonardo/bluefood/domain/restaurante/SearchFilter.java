package com.eleonardo.bluefood.domain.restaurante;

import lombok.Data;

@Data
public class SearchFilter {

  public enum SearchType {
    TEXTO, CATEGORIA;
  }
  
  public String texto;
  private SearchType searchType;
}
