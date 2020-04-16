package ru.sberbank.service.common.impl.exceptions;

public class JdbcImplException extends Exception {
	public JdbcImplException() {
		super();
	}

	public JdbcImplException(String msg) {
		super(msg);
	}
}
