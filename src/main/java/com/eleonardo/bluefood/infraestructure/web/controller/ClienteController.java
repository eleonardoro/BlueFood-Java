package com.eleonardo.bluefood.infraestructure.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eleonardo.bluefood.application.service.ClienteService;
import com.eleonardo.bluefood.application.service.RestauranteService;
import com.eleonardo.bluefood.domain.cliente.Cliente;
import com.eleonardo.bluefood.domain.cliente.ClienteRepository;
import com.eleonardo.bluefood.domain.restaurante.CategoriaRestaurante;
import com.eleonardo.bluefood.domain.restaurante.CategoriaRestauranteRepository;
import com.eleonardo.bluefood.domain.restaurante.Restaurante;
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
  private RestauranteService restauranteService;
  
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
  public String search(@ModelAttribute("searchFilter") SearchFilter searchFilter, Model model) {
    ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
    
    List<Restaurante> restaurantes = restauranteService.search(searchFilter);
    model.addAttribute("restaurantes", restaurantes);
    
    return "cliente-busca";
  }
}
