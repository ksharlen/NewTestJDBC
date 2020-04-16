package ru.sberbank.service;

import ru.sberbank.service.common.JdbcTemplate;

import java.sql.Connection;

//todo: композиция
public abstract class CrudOperationImpl<K, E> implements Crud<K, E> {
//	protected final JdbcTemplate jdbcTemplate;
	Connection connection;

	public CrudOperationImpl(Connection connection) throws NullPointerException {
		if (connection == null) {
			throw new NullPointerException();
		} else {
			this.connection = connection;
		}
	}
}
