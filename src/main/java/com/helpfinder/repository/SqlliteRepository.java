
/*
 * BU Term project for cs622
 
  This class implements the functionality required to create a sqllite repo
 * @author  Harish Janardhanan * 
 * @since   21-jan-2022
 */
package com.helpfinder.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

import javax.sql.DataSource;
import javax.transaction.NotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class SqlliteRepository implements DatabaseRepository {

	// datasource
	 @Autowired
	private DataSource dataSource;

	/**
	 * constructor
	 * 
	 * @param dataSource the data source used
	 */
	public SqlliteRepository() {		
	}

	/***
	 * get the sqllite connection
	 * 
	 * @return Connection connection to sql lite
	 * @throws ClassNotFoundException exception if the sql lite dirver is not found
	 * @throws SQLException           if connection cannot be established
	 */
	private Connection getConnection() throws ClassNotFoundException, SQLException {

		Connection connection = dataSource.getConnection();
		connection.setAutoCommit(false);
		System.out.println("Opened database successfully");
		return connection;

	}

	/***
	 * gets the results of update statement like, update, insert and delete queries
	 * 
	 * @param query the sql query
	 * @return int the number of rows effected
	 */
	private int executeUpdateStatement(String query) {
		Connection connection = null;
		Statement statement = null;
		// -1 if the execution is not successful
		int result = -1;
		try {
			// get connection and create statement to execute
			connection = getConnection();
			statement = connection.createStatement();
			// execute the query
			result = statement.executeUpdate(query);
		} catch (ClassNotFoundException | SQLException ex) {
			System.out.println("Error connecting to db " + ex.getMessage());
		} finally {
			// close the connection
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					/* Ignored */}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					/* Ignored */}
			}
		}
		// return the result
		return result;
	}

	/***
	 * gets result for a select statement
	 * 
	 * @param query the select query
	 * @return ResultSet the result of the select query
	 */

	@Override
	public ResultSet executeSelectQuery(String query) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		try {
			// get connection and create statement to execute
			connection = getConnection();
			statement = connection.createStatement();
			// execute the query
			result = statement.executeQuery(query);

		} catch (ClassNotFoundException | SQLException ex) {
			System.out.println("Error connecting to db " + ex.getMessage());
		} finally {
			// close the connection
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {
					/* Ignored */}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					/* Ignored */}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					/* Ignored */}
			}
		}
		// return the result
		return result;
	}

	/***
	 * executes the update statement
	 * 
	 * @param query update query
	 * @return int the number of rows effected
	 */
	@Override
	public int executeUpdateQuery(String query) {
		return executeUpdateStatement(query);
	}

	/***
	 * executes the insert statement
	 * 
	 * @param query insert query
	 * @return int the number of rows effected
	 */
	@Override
	public int executeInsertQuery(String query) {
		return executeUpdateStatement(query);
	}

	/***
	 * executes the delete statement
	 * 
	 * @param query delete query
	 * @return int the number of rows effected
	 */

	@Override
	public int executeDeleteQuery(String query) {
		return executeUpdateStatement(query);
	}

	/***
	 * not supported by sqllite
	 * 
	 * @throws NotSupportedException
	 */
	public java.sql.ResultSet executeSproc(String sprocName, Consumer<CallableStatement> addParams)
			throws SQLException, NotSupportedException {
		throw new NotSupportedException("Sprocs are not supported by sqllite");
	}
}
