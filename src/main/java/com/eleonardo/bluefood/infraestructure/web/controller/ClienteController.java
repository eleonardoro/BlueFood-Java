package com.eleonardo.bluefood.infraestructure.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eleonardo.bluefood.application.service.ClienteService;
import com.eleonardo.bluefood.application.service.RestauranteService;
import com.eleonardo.bluefood.domain.cliente.Cliente;
import com.eleonardo.bluefood.domain.cliente.ClienteRepository;
import com.eleonardo.bluefood.domain.restaurante.CategoriaRestaurante;
import com.eleonardo.bluefood.domain.restaurante.CategoriaRestauranteRepository;
import com.eleonardo.bluefood.domain.restaurante.ItemCardapio;
import com.eleonardo.bluefood.domain.restaurante.ItemCardapioRepository;
import com.eleonardo.bluefood.domain.restaurante.Restaurante;
import com.eleonardo.bluefood.domain.restaurante.RestauranteRepository;
import com.eleonardo.bluefood.domain.restaurante.SearchFilter;
import com.eleonardo.bluefood.util.SecurityUtils;

@Controller
@RequestMapping(path = "/cliente")
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private CategoriaRestauranteRepository categoriaRestauranteRepository;
	
	@Autowired
	private ItemCardapioRepository itemCardapioRepository;

	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private RestauranteRepository restauranteRepository;

	@GetMapping(path = "/home")
	public String home(Model model) {

		List<CategoriaRestaurante> categorias = categoriaRestauranteRepository.findAll(Sort.by("nome"));
		model.addAttribute("categorias", categorias);
		model.addAttribute("searchFilter", new SearchFilter());

		return "cliente-home";
	}

	@GetMapping(path = "/edit")
	public String edit(Model model) {
		Integer clienteId = SecurityUtils.loggedCliente().getId();

		Cliente cliente = clienteRepository.findById(clienteId).orElseThrow();

		model.addAttribute("cliente", cliente);
		ControllerHelper.setEditMode(model, true);

		return "cliente-cadastro";
	}

	@PostMapping("/save")
	public String save(@ModelAttribute("cliente") @Valid Cliente cliente, Errors errors, Model model) {

		if (!errors.hasErrors()) {
			try {
				clienteService.saveCliente(cliente);
				model.addAttribute("msg", "Cliente gravado com sucesso!");
			} catch (Exception e) {
				errors.rejectValue("email", null, e.getMessage());
			}
		}

		ControllerHelper.setEditMode(model, true);
		return "cliente-cadastro";
	}

	@GetMapping(path = "/search")
	public String search(@ModelAttribute("searchFilter") @RequestParam(value = "cmd", required = false) String command,
			SearchFilter searchFilter, Model model) {
		searchFilter.processFilter(command);

		List<Restaurante> restaurantes = restauranteService.search(searchFilter);
		model.addAttribute("restaurantes", restaurantes);

		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);

		model.addAttribute("searchFilter", searchFilter);
		model.addAttribute("cep", SecurityUtils.loggedCliente().getCep());

		return "cliente-busca";
	}

	@GetMapping(path = "/restaurante")
	public String viewRestaurante(
			@RequestParam(value = "restauranteId") Integer restauranteId, 
			@RequestParam(value = "categoria", required = false) String categoria,
			Model model) {
		
		Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow();
		model.addAttribute("restaurante", restaurante);
		
		model.addAttribute("cep", SecurityUtils.loggedCliente().getCep());
		
		List<String> categorias = itemCardapioRepository.findCategorias(restauranteId);
		model.addAttribute("categorias", categorias);
		
		List<ItemCardapio> itensCardapioDestaque;
		List<ItemCardapio> itensCardapioNaoDestaque;
		
		if(categoria == null) {
			itensCardapioDestaque= itemCardapioRepository.findByRestaurante_IdAndDestaqueOrderByNome(restauranteId, true);
			itensCardapioNaoDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueOrderByNome(restauranteId, false);
		}else {
			itensCardapioDestaque= itemCardapioRepository.findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteId, true, categoria);
			itensCardapioNaoDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteId, false, categoria);
		}
		
		model.addAttribute("itensCardapioDestaque", itensCardapioDestaque);
		model.addAttribute("itensCardapioNaoDestaque", itensCardapioNaoDestaque);
		model.addAttribute("categoriaSelecionada", categoria);
		
		return "cliente-restaurante";
	}

}
