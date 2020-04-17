package ru.sberbank.service.common.wrappers.jdbc.exceptions;

public class JdbcTooManyObjectsException extends JdbcImplException {
	public JdbcTooManyObjectsException() {
		super();
	}

	public JdbcTooManyObjectsException(String msg) {
		super(msg);
	}
}
