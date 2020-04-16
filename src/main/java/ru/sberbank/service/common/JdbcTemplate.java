package ru.sberbank.service.common;

import ru.sberbank.service.common.impl.exceptions.JdbcImplException;
import ru.sberbank.service.common.impl.exceptions.JdbcTooManyObjectsException;
import ru.sberbank.service.common.mapping.Mapping;
import ru.sberbank.service.entity.User;

import java.util.List;

//Создать объект (insert into) вставляет новый объект
//Получить объект (select from) возвращает объект
//Изменить объект (update table_name set) изменяет существующий
//достать несколько объектов
public interface JdbcTemplate {
	Object executeQuery(String sql, List<Object> params, Mapping map) throws JdbcImplException; //todo:select
	Object executeQuery(String sql, Mapping map) throws JdbcTooManyObjectsException;
	List<Object> list(String sql, List<Object> params, Mapping map); //todo:select
	List<Object> list(String sql, Mapping map);
	boolean executeUpdate(String sql);  //todo:общие запросы
	boolean executeUpdate(String sql, List<Object> params); //todo:update, insert
}
