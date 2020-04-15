package ru.sberbank.service;

import java.sql.*;

public class CrudOperationUserImpl implements Crud<Integer, User> {
	private static final String SQL_CREATE_NEW_USER = "insert into Users(id, name, lastName) values(?, ?, ?)";
	private static final String SQL_READ_USER = "select * from Users where id=?";
	private static final String SQL_UPDATE_USER = "update Users set name=?, lastName=? where id=?";
	private static final String SQL_DELETE_USER = "delete from Users where id=?";

	private final Connection connection;

	public CrudOperationUserImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public boolean create(User user) {
		try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_NEW_USER)) {
			statement.setInt(1, user.getId());
			statement.setString(2, user.getName());
			statement.setString(3, user.getLastName());
			statement.executeUpdate();
		} catch (SQLException e) {
			return (false);
		}
		User testUser = read(user.getId());
		return (testUser.equals(user));
	}

	@Override
	public User read(Integer key) {
		User user = null;
		try (PreparedStatement statement = connection.prepareStatement(SQL_READ_USER)) {
			statement.setInt(1, key);
			ResultSet resultSet = statement.executeQuery();
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
		try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_USER)) {
			statement.setString(1, user.getName());
			statement.setString(2, user.getLastName());
			statement.setInt(3, user.getId());
			statement.executeUpdate();
		} catch (SQLException e) {
			return (false);
		}
		User testUser = read(user.getId());
		return (user.equals(testUser));
	}

	@Override
	public boolean delete(Integer key) {
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
