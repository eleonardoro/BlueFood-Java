package com.eleonardo.bluefood.domain.cliente;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "cliente")
public class Cliente extends Usuario{
  
  @NotBlank(message = "Digite o CPF!")
  @Pattern(regexp = "[0-9] {11}", message = "O CPF digitado é inválido!")
  @Column(length = 11, nullable = false)
  private String cpf;
  
  @NotBlank(message = "Digite o CEP!")
  @Pattern(regexp = "[0-9] {8}", message = "O CEP digitado é inválido!")
  @Column(length = 8)
  private String cep;

}
