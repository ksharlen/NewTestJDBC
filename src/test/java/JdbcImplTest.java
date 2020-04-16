import org.h2.jdbc.JdbcException;
import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.sberbank.service.common.impl.JdbcImpl;
import ru.sberbank.service.common.impl.exceptions.JdbcImplException;
import ru.sberbank.service.common.impl.exceptions.JdbcTooManyObjectsException;
import ru.sberbank.service.entity.User;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TooManyListenersException;

public class JdbcImplTest {
	private static final String URL = "jdbc:h2:~/test;AUTO_SERVER=TRUE;Mode=Oracle";
	private static final String USER = "sa";
	private static final String PASS = "";

	private static final String SQL_INSERT = "insert into Users(id, name, lastName) values(?, ?, ?)";

	private JdbcImpl jdbcImpl;
	private Connection connection;
	private Server server;

	@Before
	public void init() throws SQLException {
		DeleteDbFiles.execute("~", "test", true);
		server = Server.createWebServer();
		server.start();
		connection = DriverManager.getConnection(URL, USER, PASS);
		jdbcImpl = new JdbcImpl(connection);
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
		jdbcImpl.executeQuery(null, null, null);
		jdbcImpl.executeQuery(null, null);
	}

	@Test
	public void testExecuteQuerySelectById() throws JdbcImplException {
		User user = new User(1, "Alexandr", "Akinin");
		User userTest;

		jdbcImpl.executeUpdate(SQL_INSERT, Arrays.asList(user.getId(), user.getName(), user.getLastName()));
		userTest = (User) jdbcImpl.executeQuery("select * from Users where id=?", Arrays.asList(user.getId()), user);
		Assert.assertEquals(user, userTest);
	}

	@Test(expected = JdbcTooManyObjectsException.class)
	public void testExecuteQueryException() throws JdbcTooManyObjectsException {
		List<Object> list = new LinkedList<>(Arrays.asList(
				new User(1, "Alexandr", "Akinin"),
				new User(2, "Sergey", "Akinin")
		));
		insertInToTableUsers(list);
		jdbcImpl.executeQuery("select * from Users", new User());
	}

	@Test
	public void testList() throws SQLException {
		boolean isCompare;
		List<Object> expectedList = new LinkedList<>(Arrays.asList(new User(1, "Alexandr", "Akinin"),
				new User(2, "Petr", "Petrov"),
				new User(3, "Ivan", "Ivanov")));
		insertInToTableUsers(expectedList);
		List<Object> testList = jdbcImpl.list("select * from Users", new User()); //todo: пока не придумал как new User поменять на что-то интересное
		isCompare = compareList(expectedList, testList);
		Assert.assertTrue(isCompare);
	}

	@Test
	public void testExecuteUpdate() throws JdbcImplException {
		boolean isSuccessInsert;
		User user = new User(1, "Alexandr", "Akinin");
		User testUser = null;

		isSuccessInsert = jdbcImpl.executeUpdate(SQL_INSERT, Arrays.asList(user.getId(), user.getName(), user.getLastName()));
		Assert.assertTrue(isSuccessInsert);
		testUser = (User) jdbcImpl.executeQuery("select * from Users", new User());
		Assert.assertEquals(testUser, user);
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
		jdbcImpl.executeUpdate(SQL_INSERT, Arrays.asList(user.getId(), user.getName(), user.getLastName()));
	}
}
