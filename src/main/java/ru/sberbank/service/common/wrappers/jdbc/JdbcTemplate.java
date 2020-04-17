package ru.sberbank.service.common.wrappers.jdbc;

import ru.sberbank.service.common.wrappers.jdbc.exceptions.JdbcImplException;
import ru.sberbank.service.common.wrappers.jdbc.exceptions.JdbcTooManyObjectsException;
import ru.sberbank.service.common.mapping.Mapping;

import java.util.List;

/**
 * executeQuery - получить информацию одной записи
 * list - получить информацию несоклких записей
 * executeUpdate - обновить информацию, манипуляция информацией
 */
public interface JdbcTemplate {
	Object executeQuery(String sql, List<Object> params, Mapping map) throws JdbcImplException;
	Object executeQuery(String sql, Mapping map) throws JdbcTooManyObjectsException;
	List<Object> list(String sql, List<Object> params, Mapping map);
	List<Object> list(String sql, Mapping map);
	boolean executeUpdate(String sql);
	boolean executeUpdate(String sql, List<Object> params);
}
