package com.eleonardo.bluefood.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eleonardo.bluefood.domain.cliente.Cliente;
import com.eleonardo.bluefood.domain.cliente.ClienteRepository;

@Service
public class ClienteService {

  @Autowired
  private ClienteRepository clienteRepository;
  
  public void saveCliente(Cliente cliente) {
    clienteRepository.save(cliente);
  }
}
