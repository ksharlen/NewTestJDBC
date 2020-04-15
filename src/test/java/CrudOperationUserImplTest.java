import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;
import org.junit.*;
import ru.sberbank.service.Crud;
import ru.sberbank.service.CrudOperationUserImpl;
import ru.sberbank.service.entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CrudOperationUserImplTest {
	private static final String URL = "jdbc:h2:~/test;AUTO_SERVER=TRUE;Mode=Oracle";
	private static final String USER = "sa";
	private static final String PASS = "";

	private CrudOperationUserImpl crudOperationUserImpl;
	private Connection connection;
	private Server server;

	@Before
	public void init() throws SQLException {
		DeleteDbFiles.execute("~", "test", true);
		server = Server.createWebServer();
		server.start();
		connection = DriverManager.getConnection(URL, USER, PASS);
		crudOperationUserImpl = new CrudOperationUserImpl(connection);
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
	public void testCreate() {
		User user = new User(1, "Alexandr", "Akinin");
		User testUser = null;
		boolean returnCreate;

		returnCreate = crudOperationUserImpl.create(user);
		Assert.assertTrue(returnCreate);
		testUser = crudOperationUserImpl.read(user.getId());
		Assert.assertEquals("Все плоха", user, testUser);
	}

	@Test
	public void testRead() {
		User user = new User(1, "Alexandr", "Akinin");
		User testUser = null;

		crudOperationUserImpl.create(user);
		testUser = crudOperationUserImpl.read(user.getId());
		Assert.assertEquals("Все плоха", user, testUser);
	}

	@Test
	public void testUpdate() {
		User user = new User(1, "Alexandr", "Akinin");
		User testUser = null;
		boolean update;

		crudOperationUserImpl.create(user);
		user.setName("Sergey");
		update = crudOperationUserImpl.update(user);
		Assert.assertTrue(update);
		testUser = crudOperationUserImpl.read(user.getId());
		Assert.assertEquals("Все плохо", user, testUser);
	}

	@Test
	public void testDelete() {
		User user = new User(1, "Alexandr", "Akinin");
		boolean delete;

		crudOperationUserImpl.create(user);
		delete = crudOperationUserImpl.delete(user.getId());
		Assert.assertTrue(delete);
		user = crudOperationUserImpl.read(user.getId());
		Assert.assertNull(user);
	}

	@Test
	public void testDuplicate() {
		User user = new User(1, "Alexandr", "Akinin");
		boolean create;

		crudOperationUserImpl.create(user);
		create = crudOperationUserImpl.create(user);
		Assert.assertFalse(create);
	}

	@Test(expected = NullPointerException.class)
	public void testNullException() {
		Crud<Integer, User> test = new CrudOperationUserImpl(null);
	}
}
