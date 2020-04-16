package ru.sberbank.service.common.impl.exceptions;

public class IsEmptyResultException extends JdbcImplException {
	public IsEmptyResultException() {
		super();
	}

	public IsEmptyResultException(String msg) {
		super(msg);
	}
}
