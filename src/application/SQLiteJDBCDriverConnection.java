package application;

import java.sql.*;
import java.util.ArrayList;

public class SQLiteJDBCDriverConnection {
	Connection connection = null;

	public SQLiteJDBCDriverConnection(Connection conn) {
		this.connection = conn;

	}

	public SQLiteJDBCDriverConnection() {

	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Connect to the database, must call first
	 */
	public void connect() {
		try {
			String url = "jdbc:sqlite:resources/dict.sqlite";
			connection = DriverManager.getConnection(url);
			connection.setAutoCommit(false);
			System.out.println("Connection to SQLite has been established.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void commit() {
		try {
			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Disconnect to the data base, call lastest
	 */
	public void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Execute the sql query, suitable for INSERT and DELETE query
	 * 
	 * @param sql
	 */
	public void executeSQL(String sql) {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			connection.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Execute the sql query, suitable for SELECT query
	 * @param sql
	 * @return An ArrayList of word arcoding to 
	 */
	public ArrayList<Word> executeSQLSelectQuery(String sql) {
		ArrayList<Word> dictionary = new ArrayList<Word>();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String word = resultSet.getString("WORD_TARGET");
				String detail = resultSet.getString("WORD_MEANING");
				Word temp = new Word(word, detail);
				dictionary.add(temp);
		
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				resultSet.close();
				statement.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		
		return dictionary;
	}

	public static void main(String[] args) {
		SQLiteJDBCDriverConnection sqLiteJDBCDriverConnection = new SQLiteJDBCDriverConnection();
		sqLiteJDBCDriverConnection.connect();
		ArrayList<Word> dict = 	sqLiteJDBCDriverConnection.executeSQLSelectQuery("SELECT * FROM dictionary_e_v WHERE WORD_TARGET LIKE \'p%\';");
		for(Word w: dict) {
			System.out.println(w.getWordExplanain());
		}
		sqLiteJDBCDriverConnection.disconnect();
	}
}
