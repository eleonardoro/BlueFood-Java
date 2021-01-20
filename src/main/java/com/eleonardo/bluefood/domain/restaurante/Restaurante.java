package com.eleonardo.bluefood.domain.restaurante;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.eleonardo.bluefood.domain.cliente.Usuario;
import com.eleonardo.bluefood.infraestructure.web.validator.UploadConstraint;
import com.eleonardo.bluefood.util.FileType;
import com.eleonardo.bluefood.util.StringUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "restaurante")
public class Restaurante extends Usuario {

	@NotBlank(message = "Digite o CNPJ!")
	@Pattern(regexp = "[0-9]{14}", message = "O CNPJ digitado é inválido!")
	@Column(length = 14, nullable = false)
	private String cnpj;

	@NotNull(message = "A Taxa de Entrega não pode ser vazia!")
	@DecimalMin("0.0")
	@DecimalMax("99.99")
	private BigDecimal taxaEntrega;

	@NotNull(message = "O Tempo de Entrega não pode ser vazia!")
	@Min(0)
	@Max(120)
	private Integer tempoEntregaBase;

	@Size(max = 80)
	private String logotipo;

	@UploadConstraint(acceptedFiles = { FileType.PNG, FileType.JPG })
	private transient MultipartFile logotipoFile;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "restaurante_has_categoria", joinColumns = @JoinColumn(name = "restaurante_id"), inverseJoinColumns = @JoinColumn(name = "categoria_restaurante_id"))
	@Size(min = 1, message = "Selecione pelo menos uma categoria!")
	@ToString.Exclude
	private Set<CategoriaRestaurante> categorias = new HashSet<>(0);

	@OneToMany(mappedBy = "restaurante")
	private Set<ItemCardapio> itensCardapio = new HashSet<>(0);

	public void setLogotipoFileName() {
		if (getId() == null) {
			throw new IllegalStateException("É preciso primeiro gravar o registro do restaurante!");
		}

		this.logotipo = String.format("%04d-logo.%s", getId(),
				FileType.of(logotipoFile.getContentType()).getExtension());
	}

	public String getCategoriasAsText() {
		Set<String> strings = new LinkedHashSet<>();

		for (CategoriaRestaurante categoria : categorias) {
			strings.add(categoria.getNome());
		}

		return StringUtils.concatenate(strings, ", ");
	}

	public Integer calcularTempoEntrega(String cep) {
		int soma = 0;

		for (char c : cep.toCharArray()) {
			int v = Character.getNumericValue(c);

			if (v > 0) {
				soma += v;
			}
		}

		soma /= 2;

		return tempoEntregaBase + soma;
	}
}
