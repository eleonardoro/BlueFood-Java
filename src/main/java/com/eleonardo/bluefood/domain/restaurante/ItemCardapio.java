package com.eleonardo.bluefood.domain.restaurante;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.eleonardo.bluefood.infraestructure.web.validator.UploadConstraint;
import com.eleonardo.bluefood.util.FileType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "item_cardapio")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemCardapio implements Serializable{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @NotBlank(message = "O Nome não pode ser vazio!")
  @Size(max = 50, message = "O Nome deve ter até 50 caracteres!")
  private String nome;
  
  @NotBlank(message = "A Categoria não pode ser vazia!")
  @Size(max = 25, message = "A Categoria deve ter até 25 caracteres!")
  private String categoria;
  
  @NotBlank(message = "A Descrição não pode ser vazia!")
  @Size(max = 80, message = "A Descrição deve ter até 80 caracteres!")
  private String descricao;
  
  @Size(max = 50)
  private String imagem;
  
  @NotNull(message = "O Preço não pode ser vazio!")
  @DecimalMin("0.0")
  private BigDecimal preco;
  
  @NotNull
  private Boolean destaque;
  
  @NotNull
  @ManyToOne
  @JoinColumn(name = "restaurante_id")
  private Restaurante restaurante;
  
  @UploadConstraint(acceptedFiles = FileType.PNG, message = "O arquivo não é de uma extensão aceitado. Enviar apenas PNG!")
  private transient MultipartFile imagemFile;
  
  public void setImagemFileName() {
    if(getId() == null)
      throw new IllegalStateException("O objeto precisa ser criado primeiro!");
    
    this.imagem = String.format("%04d-comida.%s", getId(), FileType.of(imagemFile.getContentType()).getExtension());
  }
  
}
