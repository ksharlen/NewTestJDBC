package ru.sberbank.service.common.impl;

import ru.sberbank.service.common.JdbcTemplate;
import ru.sberbank.service.common.impl.exceptions.JdbcImplException;
import ru.sberbank.service.common.impl.exceptions.TooManyObjectsException;
import ru.sberbank.service.common.mapping.Mapping;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class JdbcImpl implements JdbcTemplate {
	private final Connection connection;

	public JdbcImpl(Connection connection) {
		if (connection == null) {
			throw new NullPointerException("connection is null");
		} else {
			this.connection = connection;
		}
	}

	@Override
	public Object executeQuery(String sql, List<Object> params, Mapping map) throws JdbcImplException {
		validateParams(Arrays.asList(sql, params, map));
		Object obj = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			int i = 0;
			for (Object param : params) {
				preparedStatement.setObject(++i, param);
			}
			obj = createObject(preparedStatement.executeQuery(), map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (obj);
	}

	public List<Object> list(String sql, List<Object> params, Mapping map) {
		return (null);
	}

	public boolean executeUpdate(String sql) {
		return (false);
	}

	public boolean executeUpdate(String sql, List<Object> params) {
		return (false);
	}

	private Object createObject(ResultSet resultSet, Mapping map) throws TooManyObjectsException {
		try {
			int i = 0;
			int numColumns = getColumns(resultSet);
			resultSet.next();
			while (i < numColumns) {
				map.setObject(resultSet.getObject(++i));
			}
			if (resultSet.next()) {
				throw new TooManyObjectsException();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (map.map());
	}

	private void validateParams(List<Object> obj) {
		for (Object o : obj) {
			if (o == null) {
				throw new NullPointerException();
			}
		}
	}

	private int getColumns(ResultSet resultSet) {
		int numColumns = 0;
		try {
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			numColumns = resultSetMetaData.getColumnCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (numColumns);
	}
}
