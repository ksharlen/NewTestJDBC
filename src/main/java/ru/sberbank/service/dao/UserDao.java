package ru.sberbank.service.dao;

import ru.sberbank.service.common.wrappers.jdbc.JdbcWrapper;
import ru.sberbank.service.common.wrappers.jdbc.exceptions.JdbcImplException;
import ru.sberbank.service.entity.User;

import java.util.Arrays;

public class UserDao implements Dao<Integer, User>{
	private static final String SQL_CREATE_NEW_USER = "insert into Users(id, name, lastName) values(?, ?, ?)";
	private static final String SQL_READ_USER = "select * from Users where id=?";
	private static final String SQL_UPDATE_USER = "update Users set name=?, lastName=? where id=?";
	private static final String SQL_DELETE_USER = "delete from Users where id=?";

	private final JdbcWrapper jdbcWrapper;

	public UserDao(JdbcWrapper jdbcWrapper) {
		if (jdbcWrapper == null) {
			throw new NullPointerException("jdbcImpl is null");
		} else {
			this.jdbcWrapper = jdbcWrapper;
		}
	}

	@Override
	public boolean create(User user) {
		boolean isCreated;
		if (user == null)
			return (false);
		isCreated = jdbcWrapper.executeUpdate(SQL_CREATE_NEW_USER, Arrays.asList(user.getId(), user.getName(), user.getLastName()));
		return (isCreated);
	}

	@Override
	public User read(Integer key) {
		User user = null;
		if (key == null)
			return (null);
		try {
			user = (User) jdbcWrapper.executeQuery(SQL_READ_USER, Arrays.asList(key), new User());
		} catch (JdbcImplException e) {
			e.printStackTrace();
		}
		return (user);
	}

	@Override
	public boolean update(User user) {
		if (user == null)
			return (false);
		boolean update = jdbcWrapper.executeUpdate(SQL_UPDATE_USER, Arrays.asList(user.getName(), user.getLastName(), user.getId()));
		return (update);
	}

	@Override
	public boolean delete(Integer key) {
		if (key == null)
			return (false);
		boolean delete = jdbcWrapper.executeUpdate(SQL_DELETE_USER, Arrays.asList(key));
		return (delete);
	}
}
