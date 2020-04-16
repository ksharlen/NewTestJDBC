package ru.sberbank.service.common.impl;

import ru.sberbank.service.common.JdbcTemplate;
import ru.sberbank.service.common.impl.exceptions.JdbcImplException;
import ru.sberbank.service.common.impl.exceptions.JdbcTooManyObjectsException;
import ru.sberbank.service.common.mapping.Mapping;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
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
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			obj = createObject(resultSet, map, getColumns(resultSet));
			if (resultSet.next()) {
				throw new JdbcTooManyObjectsException();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (obj);
	}

	public Object executeQuery(String sql, Mapping map) throws JdbcTooManyObjectsException {
		Object newMap = null;
		validateParams(Arrays.asList(sql, map));
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			newMap = createObject(resultSet, map, getColumns(resultSet));
			if (resultSet.next()) {
				throw new JdbcTooManyObjectsException();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (newMap);
	}

	public List<Object> list(String sql, List<Object> params, Mapping map) {
		validateParams(Arrays.asList(sql, params, map));
		List<Object> list = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			int i = 0;
			for (Object param : params) {
				preparedStatement.setObject(++i, param);
			}
			list = createObjects(preparedStatement.executeQuery(), map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (list);
	}

	public List<Object> list(String sql, Mapping map) {
		validateParams(Arrays.asList(sql, map));
		List<Object> list = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			ResultSet resultSet = preparedStatement.executeQuery();
			list = createObjects(resultSet, map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (list);
	}

	public boolean executeUpdate(String sql) {
		boolean isSuccessUpdate = false;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			isSuccessUpdate = preparedStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (isSuccessUpdate);
	}

	public boolean executeUpdate(String sql, List<Object> params) {
		boolean isSuccessUpdate = false;
		validateParams(Arrays.asList(sql, params));
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			int i = 0;
			for (Object param : params) {
				preparedStatement.setObject(++i, param);
			}
			isSuccessUpdate = preparedStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (isSuccessUpdate);
	}

	private Object createObject(ResultSet resultSet, Mapping map, int numColumns) {
		Mapping newMap = (Mapping) map.createObject();
		try {
			int i = 0;
			while (i < numColumns) {
				newMap.setObject(resultSet.getObject(++i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (newMap);
	}

	private List<Object> createObjects(ResultSet resultSet, Mapping map) throws SQLException {
		int numColumns = getColumns(resultSet);
		List<Object> list = new LinkedList<>();

		while (resultSet.next()) {
			Object newMap = createObject(resultSet, map, numColumns);
			list.add(newMap);
		}
		return (list);
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
