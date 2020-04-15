package ru.sberbank.service;

import java.sql.Connection;

public abstract class CrudOperationImpl<K, E> implements Crud<K, E> {
	protected final Connection connection;

	public CrudOperationImpl(Connection connection) throws NullPointerException {
		if (connection == null) {
			throw new NullPointerException();
		} else {
			this.connection = connection;
		}
	}
}
