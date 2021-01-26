package com.eleonardo.bluefood.application.service;

@SuppressWarnings("serial")
public class PagamentoException extends Exception {

	public PagamentoException() {
		super();
	}

	public PagamentoException(String message) {
		super(message);
	}

	public PagamentoException(Throwable cause) {
		super(cause);
	}

	public PagamentoException(String message, Throwable cause) {
		super(message, cause);
	}

}
