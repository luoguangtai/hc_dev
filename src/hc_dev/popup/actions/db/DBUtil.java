package hc_dev.popup.actions.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	private static Connection conn = null;
	private static Statement stmt = null;

	public static void init(String driver, String url, String user, String password) throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		conn = DriverManager.getConnection(url, user, password);
		stmt = conn.createStatement();
	}

	public static void execute(String sql) throws SQLException {
		stmt.execute(sql);
	}

	public static ResultSet executeQuery(String sql) throws SQLException {
		return stmt.executeQuery(sql);
	}

	public static void prepareCall(String sql) throws SQLException {
		CallableStatement cs = null;
		try {
			cs = conn.prepareCall(sql);
			cs.execute();
		} finally {
			try {
				cs.close();
			} catch (SQLException e) {
			}
		}
	}

	public static void close() {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

}
