

package com.eleonardo.bluefood.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eleonardo.bluefood.domain.cliente.ClienteRepository;
import com.eleonardo.bluefood.domain.restaurante.Restaurante;
import com.eleonardo.bluefood.domain.restaurante.Restaurante;
import com.eleonardo.bluefood.domain.restaurante.RestauranteRepository;
import com.eleonardo.bluefood.domain.restaurante.SearchFilter;

@Service
public class RestauranteService {

  @Autowired
  private RestauranteRepository restauranteRepository;
  
  @Autowired
  private ImageService imageService;
  
  @Autowired
  private ClienteRepository clienteRepository;
  
  @Transactional
  public void saveRestaurante(Restaurante restaurante) throws ValidationException{

    if(!validateEmail(restaurante.getEmail(), restaurante.getId())) {
      throw new ValidationException("E-mail já cadastrado!");
    }

    if(restaurante.getId() != null) {
      Restaurante restauranteDb = restauranteRepository.findById(restaurante.getId()).orElseThrow();
      restaurante.setSenha(restauranteDb.getSenha());
      
    }else {
      restaurante.encryptPassword();
      
      restaurante = restauranteRepository.save(restaurante);
      
      restaurante.setLogotipoFileName();

      imageService.uploadLogotipo(restaurante.getLogotipoFile(), restaurante.getLogotipo());
      
      
    }
    
  }
  
  private boolean validateEmail(String email, Integer id) {
    
    if(clienteRepository.findByEmail(email) != null) return false;
    
    Restaurante restaurante = restauranteRepository.findByEmail(email);
    
    if(restaurante != null) {
      if(id == null) {
        return false;
      }

      if(!restaurante.getId().equals(id)) {
        return false;
      }
    }
    
    return true;
    
  }
  
  public List<Restaurante> search(SearchFilter searchFilter){
    return restauranteRepository.findAll();
  }
}
