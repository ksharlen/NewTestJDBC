import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.sberbank.service.common.wrappers.jdbc.JdbcWrapper;
import ru.sberbank.service.common.wrappers.jdbc.exceptions.JdbcImplException;
import ru.sberbank.service.common.wrappers.jdbc.exceptions.JdbcTooManyObjectsException;
import ru.sberbank.service.entity.User;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JdbcWrapperTest {
	private static final String URL = "jdbc:h2:~/test;AUTO_SERVER=TRUE;Mode=Oracle";
	private static final String USER = "sa";
	private static final String PASS = "";

	private static final String SQL_INSERT = "insert into Users(id, name, lastName) values(?, ?, ?)";

	private JdbcWrapper jdbcWrapper;
	private Connection connection;
	private Server server;

	@Before
	public void init() throws SQLException {
		DeleteDbFiles.execute("~", "test", true);
		server = Server.createWebServer();
		server.start();
		connection = DriverManager.getConnection(URL, USER, PASS);
		jdbcWrapper = new JdbcWrapper(connection);
		//todo
		Statement statement = connection.createStatement();
		statement.executeUpdate("create table Users(id serial, name varchar(255), lastName varchar(255))");
		statement.close();
	}

	@After
	public void stop() throws SQLException {
		Statement statement = connection.createStatement();
		statement.execute("DROP table Users");
		server.stop();
	}

	@Test(expected = NullPointerException.class)
	public void testExecuteQueryNullPointerException() throws JdbcImplException {
		jdbcWrapper.executeQuery(null, null, null);
		jdbcWrapper.executeQuery(null, null);
	}

	@Test
	public void testExecuteQuerySelectById() throws JdbcImplException {
		User user = new User(1, "Alexandr", "Akinin");
		User userTest;

		jdbcWrapper.executeUpdate(SQL_INSERT, Arrays.asList(user.getId(), user.getName(), user.getLastName()));
		userTest = (User) jdbcWrapper.executeQuery("select * from Users where id=?", Arrays.asList(user.getId()), user);
		Assert.assertEquals(user, userTest);
	}

	@Test(expected = JdbcTooManyObjectsException.class)
	public void testExecuteQueryException() throws JdbcTooManyObjectsException {
		List<Object> list = new LinkedList<>(Arrays.asList(
				new User(1, "Alexandr", "Akinin"),
				new User(2, "Sergey", "Akinin")
		));
		insertInToTableUsers(list);
		jdbcWrapper.executeQuery("select * from Users", new User());
	}

	@Test
	public void testExecuteQueryTwoParamsSelectById() throws JdbcImplException {
		User user = new User(1, "Alexandr", "Akinin");
		List<Object> list = new LinkedList<>(Arrays.asList(
				user,
				new User(2, "Sergey", "Akinin")
		));
		insertInToTableUsers(list);
		User testUser = (User) jdbcWrapper.executeQuery("select * from Users where id=1", user);
		Assert.assertEquals(user, testUser);
	}
	//TODO: еще будут тесты для executeQuery


	@Test
	public void testListTwoParams() {
		List<Object> expectedList = new LinkedList<>(Arrays.asList(
				new User(1, "Alexandr", "Akinin"),
				new User(2, "Petr", "Petrov"),
				new User(3, "Ivan", "Ivanov")));
		insertInToTableUsers(expectedList);
		List<Object> testList = jdbcWrapper.list("select * from Users", new User());
		boolean isCompare = compareList(expectedList, testList);
		Assert.assertTrue(isCompare);
	}

	@Test
	public void testListThreeParams() {
		List<Object> expectedList = new LinkedList<>(Arrays.asList(
				new User(1, "Alexandr", "Akinin"),
				new User(2, "Sergey", "Akinin"),
				new User(3, "Dmitriy", "Akinin")
		));
		insertInToTableUsers(expectedList);
		List<Object> actualList = jdbcWrapper.list("select * from Users where lastName=?", Arrays.asList("Akinin"), new User());
		boolean isCompare = compareList(expectedList, actualList);
		Assert.assertTrue(isCompare);
	}
	//todo: Еще будут другие тесты для list

	@Test
	public void testExecuteUpdateTwoParamsInsertInToTable() throws JdbcImplException {
		User expectedUser = new User(1, "Alexandr", "Akinin");
		boolean isSuccessInsert = jdbcWrapper.executeUpdate("insert into Users(id, name, lastName)" +
				"values(1, 'Alexandr', 'Akinin')");
		Assert.assertTrue(isSuccessInsert);
		User actualUser = (User) jdbcWrapper.executeQuery("select * from Users", new User());
		Assert.assertEquals(expectedUser, actualUser);
	}

	@Test
	public void testExecuteUpdateThreeParamsInsertInToTable() throws JdbcImplException {
		User expectedUser = new User(1, "Alexandr", "Akinin");
		boolean isSuccessInsert = jdbcWrapper.executeUpdate(SQL_INSERT, Arrays.asList(expectedUser.getId(), expectedUser.getName(), expectedUser.getLastName()));
		Assert.assertTrue(isSuccessInsert);
		User actualUser = (User) jdbcWrapper.executeQuery("select * from Users", new User());
		Assert.assertEquals(expectedUser, actualUser);
	}

	private boolean compareList(List<Object> expected, List<Object> actual) {
		if (expected.size() != actual.size()) {
			return (false);
		} else {
			for (int i = 0; i < expected.size(); ++i) {
				if (!expected.get(i).equals(actual.get(i))) {
					return (false);
				}
			}
		}
		return (true);
	}

	private void insertInToTableUsers(List<Object> users) {
		for (Object user : users) {
			User user1 = (User) user;
			insertInToTableUser(user1);
		}
	}

	private void insertInToTableUser(User user) {
		jdbcWrapper.executeUpdate(SQL_INSERT, Arrays.asList(user.getId(), user.getName(), user.getLastName()));
	}
}
