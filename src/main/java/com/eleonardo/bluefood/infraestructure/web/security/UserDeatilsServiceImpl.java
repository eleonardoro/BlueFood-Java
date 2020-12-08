package com.eleonardo.bluefood.infraestructure.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eleonardo.bluefood.domain.cliente.ClienteRepository;
import com.eleonardo.bluefood.domain.cliente.Usuario;
import com.eleonardo.bluefood.domain.restaurante.RestauranteRepository;

@Service
public class UserDeatilsServiceImpl implements UserDetailsService{

  @Autowired
  private ClienteRepository clienteRepository;
  
  @Autowired
  private RestauranteRepository restauranteRepository;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
    Usuario usuario = clienteRepository.findByEmail(username);
    
    if(usuario == null)
      usuario = restauranteRepository.findByEmail(username);
    
    if(usuario == null)
      throw new UsernameNotFoundException(username);
      
    return new LoggedUser(usuario); 
  }

}
