import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;
import org.junit.*;
import ru.sberbank.service.dao.Dao;
import ru.sberbank.service.dao.UserDao;
import ru.sberbank.service.common.wrappers.jdbc.JdbcWrapper;
import ru.sberbank.service.entity.User;

import java.sql.*;

public class UserDaoTest {
	private static final String URL = "jdbc:h2:~/test;AUTO_SERVER=TRUE;Mode=Oracle";
	private static final String USER = "sa";
	private static final String PASS = "";

	private UserDao userDao;
	private JdbcWrapper jdbcWrapper;
	private Server server;

	@Before
	public void init() throws SQLException {
		DeleteDbFiles.execute("~", "test", true);
		server = Server.createWebServer();
		server.start();
		jdbcWrapper = new JdbcWrapper(DriverManager.getConnection(URL, USER, PASS));
		userDao = new UserDao(jdbcWrapper);
		jdbcWrapper.executeUpdate("create table Users(id serial, name varchar(255), lastName varchar(255))");
	}

	@After
	public void stop() {
		jdbcWrapper.executeUpdate("DROP table Users");
		server.stop();
	}

	@Test
	public void testCreate() {
		User user = new User(1, "Alexandr", "Akinin");
		boolean returnCreate;

		returnCreate = userDao.create(user);
		Assert.assertTrue(returnCreate);
		User testUser = userDao.read(user.getId());
		Assert.assertEquals("Все плоха", user, testUser);
	}

	@Test
	public void testRead() {
		User user = new User(1, "Alexandr", "Akinin");

		userDao.create(user);
		User testUser = userDao.read(user.getId());
		Assert.assertEquals("Все плоха", user, testUser);
	}

	@Test
	public void testUpdate() {
		User user = new User(1, "Alexandr", "Akinin");
		boolean update;

		userDao.create(user);
		user.setName("Sergey");
		update = userDao.update(user);
		Assert.assertTrue(update);
		User testUser = userDao.read(user.getId());
		Assert.assertEquals("Все плохо", user, testUser);
	}

	@Test
	public void testDelete() {
		User user = new User(1, "Alexandr", "Akinin");
		boolean delete;

		userDao.create(user);
		delete = userDao.delete(user.getId());
		Assert.assertTrue(delete);
		user = userDao.read(user.getId());
		Assert.assertNull(user);
	}

	@Test
	public void testDuplicate() {
		User user = new User(1, "Alexandr", "Akinin");
		boolean create;

		userDao.create(user);
		create = userDao.create(user);
		Assert.assertFalse(create);
	}

	@Test(expected = NullPointerException.class)
	public void testNullException() {
		Dao<Integer, User> test = new UserDao(null);
	}
}
