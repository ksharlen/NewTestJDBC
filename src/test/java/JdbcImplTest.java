import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.sberbank.service.CrudOperationUserImpl;
import ru.sberbank.service.common.impl.JdbcImpl;
import ru.sberbank.service.common.impl.exceptions.JdbcImplException;
import ru.sberbank.service.entity.User;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JdbcImplTest {
	private static final String URL = "jdbc:h2:~/test;AUTO_SERVER=TRUE;Mode=Oracle";
	private static final String USER = "sa";
	private static final String PASS = "";

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

	@Test
	public void executeSelect() throws SQLException, JdbcImplException {
		User user = new User(1, "Alexandr", "Akinin");
		User testUser = new User();

		PreparedStatement preparedStatement = connection.prepareStatement("insert into Users(id, name, lastName) values(?,?,?)");
		preparedStatement.setInt(1, user.getId());
		preparedStatement.setString(2, user.getName());
		preparedStatement.setString(3, user.getLastName());
		preparedStatement.executeUpdate();

		List<Object> list = new LinkedList<>();
		list.add(1);
		testUser = (User) jdbcImpl.executeQuery("select * from Users where id=?", list, testUser);
		Assert.assertEquals(user, testUser);
	}
}
