
package com.eleonardo.bluefood.application.service;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eleonardo.bluefood.domain.cliente.ClienteRepository;
import com.eleonardo.bluefood.domain.restaurante.ItemCardapio;
import com.eleonardo.bluefood.domain.restaurante.ItemCardapioRepository;
import com.eleonardo.bluefood.domain.restaurante.Restaurante;
import com.eleonardo.bluefood.domain.restaurante.RestauranteComparator;
import com.eleonardo.bluefood.domain.restaurante.RestauranteRepository;
import com.eleonardo.bluefood.domain.restaurante.SearchFilter;
import com.eleonardo.bluefood.domain.restaurante.SearchFilter.SearchType;
import com.eleonardo.bluefood.util.SecurityUtils;

@Service
public class RestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private ImageService imageService;

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ItemCardapioRepository itemCardapioRepository;

	@Transactional
	public void saveRestaurante(Restaurante restaurante) throws ValidationException {

		if (!validateEmail(restaurante.getEmail(), restaurante.getId())) {
			throw new ValidationException("E-mail já cadastrado!");
		}

		if (restaurante.getId() != null) {
			Restaurante restauranteDb = restauranteRepository.findById(restaurante.getId()).orElseThrow(NoSuchElementException::new);
			restaurante.setSenha(restauranteDb.getSenha());
			restaurante.setLogotipo(restauranteDb.getLogotipo());
			
			restauranteRepository.save(restaurante);

		} else {
			restaurante.encryptPassword();

			restaurante = restauranteRepository.save(restaurante);

			restaurante.setLogotipoFileName();

			imageService.uploadLogotipo(restaurante.getLogotipoFile(), restaurante.getLogotipo());

		}

	}

	private boolean validateEmail(String email, Integer id) {

		if (clienteRepository.findByEmail(email) != null)
			return false;

		Restaurante restaurante = restauranteRepository.findByEmail(email);

		if (restaurante != null) {
			if (id == null) {
				return false;
			}

			if (!restaurante.getId().equals(id)) {
				return false;
			}
		}

		return true;

	}

	public List<Restaurante> search(SearchFilter searchFilter) {
		List<Restaurante> restaurantes;

		if (searchFilter.getSearchType() == SearchType.Texto) {
			restaurantes = restauranteRepository.findByNomeIgnoreCaseContaining(searchFilter.getTexto());
		} else if (searchFilter.getSearchType() == SearchType.Categoria) {
			restaurantes = restauranteRepository.findByCategorias_Id(searchFilter.getCategoriaId());
		} else {
			throw new IllegalStateException("O tipo de busca " + searchFilter.getSearchType() + " não é suportado!");
		}

		Iterator<Restaurante> it = restaurantes.iterator();

		while (it.hasNext()) {
			Restaurante restaurante = it.next();
			double taxaEntrega = restaurante.getTaxaEntrega().doubleValue();

			if (searchFilter.isEntregaGratis() && taxaEntrega > 0
					|| !searchFilter.isEntregaGratis() && taxaEntrega == 0) {
				it.remove();
			}
		}

		RestauranteComparator comparator = new RestauranteComparator(searchFilter,
				SecurityUtils.loggedCliente().getCep());
		restaurantes.sort(comparator);

		return restaurantes;
	}
	
	@Transactional
	public void saveItemCardapio(ItemCardapio itemCardapio) {
		
		itemCardapio = itemCardapioRepository.save(itemCardapio);
		
		itemCardapio.setImagemFileName();
		
		imageService.uploadComida(itemCardapio.getImagemFile(), itemCardapio.getImagem());
	}
}
