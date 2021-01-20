package com.eleonardo.bluefood.domain.restaurante;
import java.util.Comparator;

import com.eleonardo.bluefood.domain.restaurante.Restaurante;
import com.eleonardo.bluefood.domain.restaurante.SearchFilter;
import com.eleonardo.bluefood.domain.restaurante.SearchFilter.Order;

public class RestauranteComparator implements Comparator<Restaurante> {

	private SearchFilter searchFilter;
	private String cep;

	public RestauranteComparator(SearchFilter searchFilter, String cep) {
		super();
		this.searchFilter = searchFilter;
		this.cep = cep;
	}

	@Override
	public int compare(Restaurante r1, Restaurante r2) {
		int result;

		if (searchFilter.getOrder() == Order.Taxa) {
			result = r1.getTaxaEntrega().compareTo(r2.getTaxaEntrega());
			
		}else if(searchFilter.getOrder() == Order.Tempo) {
			result = r1.calcularTempoEntrega(cep).compareTo(r2.calcularTempoEntrega(cep)); 
			
		}else {
			throw new IllegalStateException("O valor de ordenação " + searchFilter.getOrder() + " não é válido!");
		}
		
		return searchFilter.isAsc() ? result : - result;

	}

}
