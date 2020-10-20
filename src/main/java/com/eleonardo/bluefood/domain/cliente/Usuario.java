package com.eleonardo.bluefood.domain.cliente;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
@MappedSuperclass
public class Usuario implements Serializable{

  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @NotBlank(message = "Digite o nome!")
  @Size(max = 80, message = "O nome digitado é muito grande!")
  private String nome;
  
  @NotBlank(message = "Digite o e-mail!")
  @Size(max = 60, message = "O e-mail digitado é muito grande!")
  @Email(message = "O e-mail digitado é inválido!")
  private String email;
  
  @NotBlank(message = "Digite a senha!")
  @Size(max = 80, message = "A senha digitada é muito grande!")
  private String senha;
  
  @NotBlank(message = "Digite o telefone!")
  @Pattern(regexp = "[0-9] {10,11}", message = "O telefone digitado é inválido!")
  @Column(length = 11, nullable = false)
  private String telefone;

}
