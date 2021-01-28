package com.eleonardo.bluefood.application.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eleonardo.bluefood.domain.pedido.Pedido;
import com.eleonardo.bluefood.domain.pedido.PedidoRepository;
import com.eleonardo.bluefood.domain.pedido.RelatorioItemFaturamento;
import com.eleonardo.bluefood.domain.pedido.RelatorioItemFilter;
import com.eleonardo.bluefood.domain.pedido.RelatorioPedidoFilter;
import com.eleonardo.bluefood.util.CollectionUtils;

@Service
public class RelatorioService {

	@Autowired
	PedidoRepository pedidoRepository;

	public List<Pedido> listPedidos(Integer restauranteId, RelatorioPedidoFilter filter) {

		Integer pedidoId = filter.getPedidoId();

		if (pedidoId != null) {
			Pedido pedido = pedidoRepository.findByIdAndRestaurante_Id(pedidoId, restauranteId);
			return CollectionUtils.listOf(pedido);
		}

		LocalDate dataInicial = filter.getDataInicial();
		LocalDate dataFinal = filter.getDataFinal();

		if (dataInicial == null)
			return CollectionUtils.listOf();

		if (dataFinal == null)
			dataFinal = LocalDate.now();

		return pedidoRepository.findByDateInterval(restauranteId, dataInicial.atStartOfDay(), dataFinal.atTime(23, 59, 59));

	}

	public List<RelatorioItemFaturamento> calcularFaturamentoItens(Integer restauranteId, RelatorioItemFilter filter) {
		List<Object[]> itensObj;

		Integer itemId = filter.getItemId();

		LocalDate dataInicial = filter.getDataInicial();
		LocalDate dataFinal = filter.getDataFinal();

		if (dataInicial == null)
			return CollectionUtils.listOf();

		if (dataFinal == null)
			dataFinal = LocalDate.now();

		LocalDateTime dataHoraInicial = dataInicial.atStartOfDay();
		LocalDateTime dataHoraFinal = dataFinal.atTime(23, 59, 59);

		if (itemId != 0) {
			itensObj = pedidoRepository.findItensForFaturamento(restauranteId, itemId, dataHoraInicial, dataHoraFinal);
		} else {
			itensObj = pedidoRepository.findItensForFaturamento(restauranteId, dataHoraInicial, dataHoraFinal);
		}

		List<RelatorioItemFaturamento> itens = new ArrayList<RelatorioItemFaturamento>();

		for (Object[] item : itensObj) {
			String nome = (String) item[0];
			Long quantidade = (Long) item[1];
			BigDecimal valor = (BigDecimal) item[2];

			itens.add(new RelatorioItemFaturamento(nome, quantidade, valor));
		}

		return itens;
	}
}
