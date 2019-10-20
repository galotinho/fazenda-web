package com.sistema.fazenda.config;

import java.math.BigDecimal;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ConversorMoeda implements Converter<String, BigDecimal> {
    
	public BigDecimal convert(final String valor) {
       
       Double moeda = Double.parseDouble(valor.replace(".", "").replace(",", "."));
       return BigDecimal.valueOf(moeda);
    }
}