/*
 * BU Term project for cs622
 
  This interface defines the functionalities required for a database repository
 * @author  Harish Janardhanan * 
 * @since   21-jan-2022
 */
package com.helpfinder.repository;

import java.sql.*;
import java.util.function.Consumer;

import javax.transaction.NotSupportedException;

import org.springframework.stereotype.Component;

@Component
public interface DatabaseRepository {
	
	// executes select query and returns the results
	public ResultSet executeSelectQuery(String query)  throws SQLException;
	// executes an update query and returns the results
	public int executeUpdateQuery(String query)  throws SQLException;	
	// executes an insert query and returns the results
	public int executeInsertQuery(String query)  throws SQLException;
	// executes an delete query and returns the results
	public int executeDeleteQuery(String query)  throws SQLException;	
	// executes sproc
	public ResultSet executeSproc(String sprocName, Consumer<CallableStatement> addParams)  throws SQLException, NotSupportedException;

}
