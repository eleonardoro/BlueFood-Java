package com.eleonardo.bluefood.infraestructure.web.converter;

import java.math.BigDecimal;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.eleonardo.bluefood.util.FormatUtils;

@Component
public class BigDecimalToStringConverter implements Converter<BigDecimal, String>{

	@Override
	public String convert(BigDecimal source) {
		return FormatUtils.formatCurrency(source);
	}

	
	
}
