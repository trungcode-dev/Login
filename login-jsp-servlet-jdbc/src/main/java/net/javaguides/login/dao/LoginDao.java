package net.javaguides.login.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.javaguides.login.model.LoginModel;

public class LoginDao {
	private String jdbcURL = "jdbc:sqlserver://localhost:1433;databaseName=login_jsp_with_sqlserver;encrypt=false";
	private String jdbcUsername = "sa";
	private String jdbcPassword = "123";

	private static final String INSERT_LOGIN_SQL = "INSERT INTO users" + "  (username, email, password) values "
			+ " (?, ?, ?);";
	private static final String SELECT_LOGIN_BY_EMAIL = "select username, password from users where email =?";
	private static final String SELECT_ALL_LOGIN = "select * from users";

	public LoginDao() {
		// TODO Auto-generated constructor stub
	}

	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (connection == null) {
			System.out.println("Failed to create connection!");
		}
		return connection;
	}

	public void insertLogin(LoginModel login) throws SQLException {
		System.out.print(INSERT_LOGIN_SQL);

		try (Connection con = getConnection(); PreparedStatement stm = con.prepareStatement(INSERT_LOGIN_SQL)) {
			stm.setString(1, login.getUsername());
			stm.setString(2, login.getEmail());
			stm.setString(3, login.getPassword());
			System.out.println(stm);
			stm.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public LoginModel selectLogin(String email) {
		LoginModel login = null;
		try (Connection con = getConnection(); PreparedStatement stm = con.prepareStatement(SELECT_LOGIN_BY_EMAIL);) {
			stm.setString(1, email);
			System.out.println(stm);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				String username = rs.getString("username");
				String password = rs.getString("password");
				login = new LoginModel(username, email, password);
			}

		} catch (SQLException e) {
			printSQLException(e);
		}
		return login;
	}

	public boolean checkLogin(String email, String password) {
		boolean isValidUser = false;

		String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

		try (Connection con = getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {

			stm.setString(1, email);
			stm.setString(2, password);

			ResultSet rs = stm.executeQuery();

			if (rs.next()) {
				isValidUser = true;
			}

		} catch (SQLException e) {
			printSQLException(e);
		}

		return isValidUser;
	}

	public boolean isEmailExists(String email) {
		String sql = "SELECT 1 FROM users WHERE email = ?";
		try (Connection con = getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, email);
			ResultSet rs = stm.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			printSQLException(e);
		}
		return false;
	}

	public List<LoginModel> selectAllLogin() {
		List<LoginModel> login = new ArrayList<>();

		try (Connection con = getConnection(); PreparedStatement stm = con.prepareStatement(SELECT_ALL_LOGIN);) {
			System.out.println(stm);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				String username = rs.getString("username");
				String email = rs.getString("email");
				String password = rs.getString("password");
				login.add(new LoginModel(username, email, password));
			}

		} catch (SQLException e) {
			printSQLException(e);
		}
		return login;
	}

	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
}
