package com.eleonardo.bluefood.infraestructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.eleonardo.bluefood.domain.pedido.Carrinho;
import com.eleonardo.bluefood.domain.pedido.ItemPedido;
import com.eleonardo.bluefood.domain.pedido.Pedido;
import com.eleonardo.bluefood.domain.pedido.PedidoRepository;
import com.eleonardo.bluefood.domain.pedido.RestauranteDiferenteException;
import com.eleonardo.bluefood.domain.restaurante.ItemCardapio;
import com.eleonardo.bluefood.domain.restaurante.ItemCardapioRepository;

@Controller
@RequestMapping("/cliente/carrinho")
@SessionAttributes("carrinho")
public class CarrinhoController {

	@Autowired
	private ItemCardapioRepository itemCardapioRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@ModelAttribute("carrinho")
	public Carrinho carrinho() {
		return new Carrinho();
	}
	
	@GetMapping(path = "/adicionar")
	public String adicionarItem(
			@RequestParam("itemId") Integer itemId,
			@RequestParam("quantidade") Integer quantidade,
			@RequestParam("observacoes") String observacoes,
			@ModelAttribute("carrinho") Carrinho carrinho,
			Model model) {

		ItemCardapio itemCardapio = itemCardapioRepository.findById(itemId).orElseThrow();
		
		try {
			carrinho.adicionarItem(itemCardapio, quantidade, observacoes);
		} catch (RestauranteDiferenteException e) {
			model.addAttribute("msg", "N�o � poss�vel adicionar no carrinho itens de restaurantes de pedidos diferentes!");
		}

		return "cliente-carrinho";
	}
	
	@GetMapping(path = "/remover")
	public String removerItem(
			@RequestParam("itemId") Integer itemId,
			@ModelAttribute("carrinho") Carrinho carrinho,
			SessionStatus sessionStatus,
			Model model) {

		ItemCardapio itemCardapio = itemCardapioRepository.findById(itemId).orElseThrow();
		
		carrinho.removerItem(itemCardapio);
		
		if(carrinho.vazio()) {
			sessionStatus.setComplete();
		}

		return "cliente-carrinho";
	}
	
	@GetMapping(path = "/visualizar")
	public String removerItem() {
		return "cliente-carrinho";
	}
	
	@GetMapping(path = "/refazerCarrinho")
	public String refazerCarrinho(
			@RequestParam("pedidoId") Integer pedidoId,
			@ModelAttribute("carrinho") Carrinho carrinho,
			Model model
			) {
		Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow();
		
		carrinho.limpar();
		
		for(ItemPedido itemPedido : pedido.getItens()) {
			carrinho.adicionarItem(itemPedido);
		}
		
		return "cliente-carrinho";
	}

}
