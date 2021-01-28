package com.eleonardo.bluefood.application.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eleonardo.bluefood.domain.cliente.Cliente;
import com.eleonardo.bluefood.domain.cliente.ClienteRepository;
import com.eleonardo.bluefood.domain.restaurante.RestauranteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	RestauranteRepository restauranteRepository;

	@Transactional
	public void saveCliente(Cliente cliente) throws ValidationException {

		if (!validateEmail(cliente.getEmail(), cliente.getId())) {
			throw new ValidationException("E-mail já cadastrado!");
		}

		if (cliente.getId() != null) {
			Cliente clienteDb = clienteRepository.findById(cliente.getId()).orElseThrow(NoSuchElementException::new);
			cliente.setSenha(clienteDb.getSenha());

		} else {
			cliente.encryptPassword();
		}

		clienteRepository.save(cliente);
	}

	private boolean validateEmail(String email, Integer id) {

		if (restauranteRepository.findByEmail(email) != null)
			return false;

		Cliente cliente = clienteRepository.findByEmail(email);

		if (cliente != null) {
			if (id == null) {
				return false;
			}

			if (!cliente.getId().equals(id)) {
				return false;
			}
		}

		return true;
	}
}
