package ru.sberbank.service;

import ru.sberbank.service.entity.User;

import java.sql.*;

public class CrudOperationUserImpl extends CrudOperationImpl<Integer, User> {
	private static final String SQL_CREATE_NEW_USER = "insert into Users(id, name, lastName) values(?, ?, ?)";
	private static final String SQL_READ_USER = "select * from Users where id=?";
	private static final String SQL_UPDATE_USER = "update Users set name=?, lastName=? where id=?";
	private static final String SQL_DELETE_USER = "delete from Users where id=?";

	public CrudOperationUserImpl(Connection connection) {
		super(connection);
	}

	@Override
	public boolean create(User user) {
		if (user == null)
			return (false);
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_NEW_USER)) {
			preparedStatement.setInt(1, user.getId());
			preparedStatement.setString(2, user.getName());
			preparedStatement.setString(3, user.getLastName());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			return (false);
		}
		User testUser = read(user.getId());
		return (testUser.equals(user));
	}

	@Override
	public User read(Integer key) {
		if (key == null)
			return (null);
		User user = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_READ_USER)) {
			preparedStatement.setInt(1, key);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return (null);
			} else {
				user = new User(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("lastName"));
			}
		} catch (SQLException e) {
			return (null);
		}
		return (user);
	}

	@Override
	public boolean update(User user) {
		if (user == null)
			return (false);
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER)) {
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getLastName());
			preparedStatement.setInt(3, user.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			return (false);
		}
		User testUser = read(user.getId());
		return (user.equals(testUser));
	}

	@Override
	public boolean delete(Integer key) {
		if (key == null)
			return (false);
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_USER)) {
			preparedStatement.setInt(1, key);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			return (false);
		}
		User testUser = read(key);
		return (testUser == null);
	}
}
