package com.eleonardo.bluefood.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.eleonardo.bluefood.domain.cliente.Cliente;
import com.eleonardo.bluefood.domain.restaurante.Restaurante;
import com.eleonardo.bluefood.infraestructure.web.security.LoggedUser;

public class SecurityUtils {

  public static LoggedUser loggedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
    if(authentication instanceof AnonymousAuthenticationToken)
      return null; //N�o est� autenticado
    
    return (LoggedUser) authentication.getPrincipal();
  }
  
  public static Cliente loggedCliente() {
    LoggedUser loggedUser = loggedUser();
    
    if(loggedUser == null)
      throw new IllegalStateException("N�o existe um usu�rio logado");
    
    if(!(loggedUser.getUsuario() instanceof Cliente))
      throw new IllegalStateException("O usu�rio logado n�o � um Cliente!");
    
    return (Cliente) loggedUser.getUsuario();
  }
  
  public static Restaurante loggedRestaurante() {
    LoggedUser loggedUser = loggedUser();
    
    if(loggedUser == null)
      throw new IllegalStateException("N�o existe um usu�rio logado");
    
    if(!(loggedUser.getUsuario() instanceof Restaurante))
      throw new IllegalStateException("O usu�rio logado n�o � um Restaurante!");
    
    return (Restaurante) loggedUser.getUsuario();
  }
  
}
