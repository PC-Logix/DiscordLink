package pcl.bridgebot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pcl.bridgebot.DiscordLink;

class UpdateQuery {
	private int minVersion;
	private String updateQuery;

	UpdateQuery(int minVersion, String updateQuery) {
		this.minVersion = minVersion;
		this.updateQuery = updateQuery;
	}

	int getMinVersion() {
		return this.minVersion;
	}

	String getUpdateQuery() {
		return updateQuery;
	}
}

public class Database {
	public static Connection connection;
	//private static Connection connection;
	/**
	 * Updated automatically
	 */
	public static int DB_VER = 0;
	public final static Map<String, PreparedStatement> preparedStatements = new HashMap<>();
	static Statement statement;
	public static List<UpdateQuery> updateQueries = new ArrayList<>();

	public static void init() throws SQLException {
			connection = DriverManager.getConnection("jdbc:sqlite:discordlink.sqlite3");
			statement = connection.createStatement();
			statement.setPoolable(true);
			statement.setQueryTimeout(30);
	}

	public static boolean addStatement(String sql) {
		try {
			statement.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean addPreparedStatement(String name, String sql) {
		try {
			preparedStatements.put(name, connection.prepareStatement(sql));
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean addPreparedStatement(String name, String sql, int options) {
		try {
			preparedStatements.put(name, connection.prepareStatement(sql, options));
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Connection getConnection() {
		return connection;
	}

	public static PreparedStatement getPreparedStatement(String statement) throws Exception {
		if (!preparedStatements.containsKey(statement)) {
			throw new Exception("Invalid statement!");
		}
		return preparedStatements.get(statement);
	}

	public static int getDBVer() {
		try {
			ResultSet dbVerQuery = Database.getConnection().createStatement().executeQuery("PRAGMA user_version;");
			return dbVerQuery.getInt("user_version");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int setDBVer(int dbVer) {
		if (getDBVer() < dbVer) {
			try {
				return Database.getConnection().createStatement().executeUpdate("PRAGMA user_version = " + dbVer + ";");
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * @param minVersion int
	 * @param sql String
	 */
	public static void addUpdateQuery(int minVersion, String sql) {
		updateQueries.add(new UpdateQuery(minVersion, sql));
		if (minVersion > DB_VER) {
			DB_VER = minVersion;
		}
	}

	public static void updateDatabase() {
		int currentVer = getDBVer();
		DiscordLink.log.info("Updating database! Current version: " + currentVer);
		for (UpdateQuery query : updateQueries) {
			if (currentVer < query.getMinVersion()) {
				try {
					Database.getConnection().createStatement().executeUpdate(query.getUpdateQuery());
					setDBVer(query.getMinVersion());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		DiscordLink.log.info("Database update complete! New version: " + getDBVer());
		//DiscordLink.log.info("Database update ran " + counter + " queries");
	}

	public static boolean storeJsonData(String key, String data) {
//		try {
//			statement.executeQuery("CREATE TABLE IF NOT EXISTS JsonData (mykey VARCHAR(255) PRIMARY KEY NOT NULL, store TEXT DEFAULT NULL); CREATE UNIQUE INDEX JsonData_key_uindex ON JsonData (mykey)");
//		} catch (SQLException e) {
//			if (e.getErrorCode() != 101)
//				IRCBot.log.error("Exception is: ", e);
//				e.printStackTrace();
//		}
		try {
			DiscordLink.log.info("storeJsonData: ('" + key.toLowerCase() + "', '" + data + "')");
			PreparedStatement stmt = getPreparedStatement("storeJSON");
			stmt.setString(1, key);
			stmt.setString(2, data);
			stmt.executeUpdate();

			return true;
		} catch (SQLException e) {
			DiscordLink.log.error("Exception is: ", e);
			e.printStackTrace();
		} catch (Exception e) {
			DiscordLink.log.error("Exception is: ", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DiscordLink.log.error("storeJsonData false");
		return false;
	}

	public static String getJsonData(String key) {
//		try {
//			statement.executeQuery("CREATE TABLE IF NOT EXISTS JsonData (mykey VARCHAR(255) PRIMARY KEY NOT NULL, store TEXT DEFAULT NULL); CREATE UNIQUE INDEX JsonData_key_uindex ON JsonData (mykey)");
//		} catch (SQLException e) {
//			if (e.getErrorCode() != 101)
//				IRCBot.log.error("Exception is: ", e);
//				e.printStackTrace();
//		}
		try {
			PreparedStatement stmt = getPreparedStatement("retreiveJSON");
			stmt.setString(1, key);
			
			ResultSet theResult = stmt.executeQuery();
			if (theResult.next()) {
				String result = theResult.getString(1);
				DiscordLink.log.info("JsonData: " + result);
				return result;
			}
			DiscordLink.log.error("JsonData was empty, returning empty string");
			return "";
		} catch (SQLException e) {
			DiscordLink.log.error("Code: " + e.getErrorCode());
			DiscordLink.log.error("Exception is: ", e);
			e.printStackTrace();
		} catch (Exception e) {
			DiscordLink.log.error("Exception is: ", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DiscordLink.log.error("JsonData try/catch failed");
		return "";
	}

	public static ResultSet ExecuteQuery(String query) throws SQLException {
		return statement.executeQuery(query);
	}
}
