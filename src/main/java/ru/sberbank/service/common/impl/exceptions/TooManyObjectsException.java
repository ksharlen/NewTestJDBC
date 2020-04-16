package ru.sberbank.service.common.impl.exceptions;

public class TooManyObjectsException extends JdbcImplException {
	public TooManyObjectsException() {
		super();
	}

	public TooManyObjectsException(String msg) {
		super(msg);
	}
}
