package com.eleonardo.bluefood.domain.pagamento;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class DadosCartao {

	@Pattern(regexp = "\\d{16}", message = "O número do cartão é inválido!")
	@NotNull(message = "É preciso enviar os Dados do Cartão!")
	@NotBlank(message = "É preciso enviar os Dados do Cartão!")
	private String numCartao;

	public String getNumCartao() {
		return numCartao;
	}

	public void setNumCartao(String numCartao) {
		this.numCartao = numCartao;
	}
}
